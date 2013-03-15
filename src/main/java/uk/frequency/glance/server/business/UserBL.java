package uk.frequency.glance.server.business;

import java.util.List;

import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;

import uk.frequency.glance.server.business.exception.WrongStateException;
import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.model.user.EventGenerationInfo;
import uk.frequency.glance.server.model.user.Friendship;
import uk.frequency.glance.server.model.user.User;

import static uk.frequency.glance.server.model.user.Friendship.Status.*;

public class UserBL extends GenericBL<User>{

	UserDAL userDal;

	public UserBL() {
		super(new UserDAL());
		userDal = (UserDAL)dal;
	}
	
	@Override
	public User create(User user) throws ConstraintViolationException, TransientObjectException {
		user = super.create(user);
		if(user.getEventGenerationInfo() == null){
			EventGenerationInfo gen = new EventGenerationInfo();
			gen.setUser(user);
			userDal.makePersistent(gen);
			user.setEventGenerationInfo(gen);
		}
		return user;
	}
	
	public List<Friendship> findFriendships(long userId){
		User user = new User();
		user.setId(userId);
		return userDal.findFriendships(user);
	}
	
	public Friendship createFriendshipRequest(long userId, long friendId){
		User user = new User();
		user.setId(userId);
		User friend = new User();
		friend.setId(friendId);
		
		if(userId == friendId){
			throw new WrongStateException("Can't send a friendship request to yourself.");
		}
		Friendship existing = userDal.findFriendship(user, friend);
		if(existing != null){
			throw new WrongStateException("Friendship request already exists. Status is " + existing.getStatus());
		}
		
		Friendship f = new Friendship();
		f.setUser(user);
		f.setFriend(friend);
		f.setStatus(REQUEST_SENT);
		userDal.makePersistent(f);
		
		Friendship f2 = new Friendship();
		f2.setUser(friend);
		f2.setFriend(user);
		f2.setStatus(REQUEST_RECEIVED);
		userDal.makePersistent(f2);
		
		return f;
	}
	
	public Friendship acceptFriendshipRequest(long userId, long friendId){
		User user = new User();
		user.setId(userId);
		User friend = new User();
		friend.setId(friendId);
		
		Friendship f = userDal.findFriendship(user, friend);
		
		if(f == null){
			throw new WrongStateException("Friendship request doesn't exist.");
		}else if(f.getStatus() == REQUEST_SENT){
			throw new WrongStateException("Friendship request can't be accepted by the same user who sent it.");
		}else if(f.getStatus() != REQUEST_RECEIVED){
			throw new WrongStateException("Friendship state is: " + f.getStatus() + ". Can only accept in state: " + REQUEST_RECEIVED + ".");
		}
		
		f.setStatus(ACCEPTED);
		userDal.makePersistent(f);
		
		Friendship f2 = userDal.findReciprocal(f);
		f2.setStatus(ACCEPTED);
		userDal.makePersistent(f2);
		
		return f;
	}
	
	public Friendship denyFriendshipRequest(long userId, long friendId){
		User user = new User();
		user.setId(userId);
		User friend = new User();
		friend.setId(friendId);
		
		Friendship f = userDal.findFriendship(user, friend);
		
		if(f == null){
			throw new WrongStateException("Friendship request doesn't exist.");
		}else if(f.getStatus() == REQUEST_SENT){
			throw new WrongStateException("Friendship request can't be denied by the same user who sent it.");
		}else if(f.getStatus() != REQUEST_RECEIVED){
			throw new WrongStateException("Friendship state is: " + f.getStatus() + ". Can only deny in state: " + REQUEST_RECEIVED + ".");
		}
		
		f.setStatus(DENIED);
		userDal.makePersistent(f);
		
		Friendship f2 = userDal.findReciprocal(f);
		f2.setStatus(DENIED);
		userDal.makePersistent(f2);
		
		return f;
	}
	
	public List<Long> findFriends(long userId){
		User user = new User();
		user.setId(userId);
		return userDal.findFriendsIds(user, ACCEPTED);
	}
	
	public List<User> findFriendshipRequests(long userId){
		User user = new User();
		user.setId(userId);
		return userDal.findFriends(user, REQUEST_RECEIVED);
	}
}
