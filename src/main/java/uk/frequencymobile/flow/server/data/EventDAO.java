package uk.frequencymobile.flow.server.data;

import java.util.List;

import org.hibernate.Query;

import uk.frequencymobile.flow.server.model.Event;
import uk.frequencymobile.flow.server.model.User;

@SuppressWarnings("unchecked")
public class EventDAO extends GenericDAO<Event>{

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
