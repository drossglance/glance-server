package uk.frequency.glance.server.data_access;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import uk.frequency.glance.server.debug.LogEntry;
import uk.frequency.glance.server.model.user.EventGenerationInfo;
import uk.frequency.glance.server.model.user.Friendship;
import uk.frequency.glance.server.model.user.FriendshipStatus;
import uk.frequency.glance.server.model.user.User;

@SuppressWarnings("unchecked")
public class UserDAL extends GenericDAL<User>{
	
	public User findByFacebookId(String facebookId){
		Query q = getSession().createQuery("from User u where " +
				"u.facebookId = :facebookId")
				.setParameter("facebookId", facebookId);
		return (User)q.uniqueResult();
	}
	
	
	
	//EVENT GENERATION INFO
	
	public EventGenerationInfo save(EventGenerationInfo entity) {
    	getSession().save(entity);
        return entity;
    }
	
	//FIXME temporary workaround
	public EventGenerationInfo merge(EventGenerationInfo entity) {
    	getSession().merge(entity);
        return entity;
    }
	
	
	
	//FRIENDSHIP
	
	public Friendship saveOrUpdate(Friendship entity) {
		
		{//TODO use @PrePersist in the GenericEntity instead
	    	if(entity.getCreationTime() == null){
	    		entity.setCreationTime(new Date());
	    	}
	    	entity.setUpdateTime(new Date());
	    	entity.setDeleted(false);
    	}
		
    	getSession().saveOrUpdate(entity);
        return entity;
    }
	
	public List<Friendship> findFriendships(User user) {
		Query q = getSession().createQuery("from Friendship f where " +
				"f.user = :user " +
				"order by f.friend.id")
			.setParameter("user", user);
        return (List<Friendship>)q.list();
    }
	
	public Friendship findFriendship(User user, User friend) {
		Query q = getSession().createQuery("from Friendship f where " +
				"f.user = :user " +
				"and f.friend = :friend")
			.setParameter("user", user)
			.setParameter("friend", friend);
        return (Friendship)q.uniqueResult();
    }
	
	public Friendship findReciprocal(Friendship friendship){
        return findFriendship(friendship.getFriend(), friendship.getUser());
	}
	
	public List<User> findFriends(User user, FriendshipStatus status){
		Query q = getSession().createQuery("select f.friend from Friendship f where " +
				"f.user = :user " +
				"and f.status = :status " +
				"order by f.friend.id")
			.setParameter("user", user)
			.setParameter("status", status);
        return (List<User>)q.list(); 
	}
	
	public void removeFriendship(Friendship friendship){
		getSession().delete(friendship);
	}
	
	
	
	//USER LOCATION
	//TODO
	
	
	//DEBUG
	
	public void saveLogEntry(LogEntry log){
		log.creationTime = new Date();
		getSession().save(log);
	}
	
	public List<LogEntry> findAllLogEntries(){
		Query q = getSession().createQuery("from LogEntry d " +
				"order by d.time desc");
        return (List<LogEntry>)q.list(); 
	}
	
	public List<LogEntry> findLogEntry(long userId){
		Query q = getSession().createQuery("from LogEntry d where " +
				"d.userId = :userId " + 
				"order by d.time desc")
				.setParameter("userId", userId);
        return (List<LogEntry>)q.list(); 
	}
	
	public List<LogEntry> findAllLogEntriesAfter(Date time){
		Query q = getSession().createQuery("from LogEntry d where " +
				"d.time >= :time " + 
				"order by d.time desc")
				.setParameter("time", time);
        return (List<LogEntry>)q.list(); 
	}
	
	public List<LogEntry> findLogEntriesAfter(long userId, Date time){
		Query q = getSession().createQuery("from LogEntry d where " +
				"d.userId = :userId " +
				"and d.time >= :time " + 
				"order by d.time desc")
				.setParameter("userId", userId)
				.setParameter("time", time);
        return (List<LogEntry>)q.list(); 
	}
}
