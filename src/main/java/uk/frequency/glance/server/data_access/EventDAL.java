package uk.frequency.glance.server.data_access;

import java.util.List;

import org.hibernate.Query;

import uk.frequency.glance.server.model.Event;
import uk.frequency.glance.server.model.User;

@SuppressWarnings("unchecked")
public class EventDAL extends GenericDAL<Event>{

	public List<Event> findByAuthor(User author){
		Query q = session.createQuery("from Event where author = :user")
			.setEntity("user", author);
		return q.list();
	}
	
	public List<Event> findByAuthor(long authorId){
		Query q = session.createQuery("from Event where author.id = :userId")
			.setParameter("userId", authorId);
		return q.list();
	}
	
}
