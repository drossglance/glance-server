package uk.frequency.glance.server.business;

import java.util.List;

import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;

import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.model.user.EventGenerationInfo;
import uk.frequency.glance.server.model.user.Friendship;
import uk.frequency.glance.server.model.user.User;

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
		
		Friendship f = new Friendship();
		f.setUser(user);
		f.setFriend(friend);
		f.setStatus(Friendship.Status.REQUEST_SENT);
		userDal.makePersistent(f);
		
		Friendship f2 = new Friendship();
		f2.setUser(friend);
		f2.setFriend(user);
		f2.setStatus(Friendship.Status.REQUEST_RECEIVED);
		userDal.makePersistent(f2);
		
		return f;
	}
	
	public Friendship acceptFriendshipRequest(long userId, long friendId){
		User user = new User();
		user.setId(userId);
		User friend = new User();
		friend.setId(friendId);
		
		Friendship f = userDal.findFriendship(user, friend);
		f.setStatus(Friendship.Status.FRIENDS);
		userDal.makePersistent(f);
		
		Friendship f2 = userDal.findReciprocal(f);
		f2.setStatus(Friendship.Status.FRIENDS);
		userDal.makePersistent(f2);
		
		return f;
	}
	
	public Friendship denyFriendshipRequest(long userId, long friendId){
		User user = new User();
		user.setId(userId);
		User friend = new User();
		friend.setId(friendId);
		
		Friendship f = userDal.findFriendship(user, friend);
		f.setStatus(Friendship.Status.DENIED);
		userDal.makePersistent(f);
		
		Friendship f2 = userDal.findReciprocal(f);
		f2.setStatus(Friendship.Status.DENIED);
		userDal.makePersistent(f2);
		
		return f;
	}
	
	
}
