package uk.frequency.glance.server.data_access;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import uk.frequency.glance.server.model.event.Event;

@SuppressWarnings("unchecked")
public class EventDAL extends GenericDAL<Event>{

	//TODO order by time
	public List<Event> findByUser(long authorId){
		Query q = getSession().createQuery("from Event where user.id = :userId")
			.setParameter("userId", authorId);
		return q.list();
	}
	
	public List<Event> findByTimeRange(Date start, Date end){
		Query q = getSession().createQuery("from Event e where " +
				"and e.startTime > :start or e.startTime < :end")
			.setParameter("start", start)
			.setParameter("end", end);
		return q.list();
	}
	
	public List<Event> findByTimeRange(long userId, Date start, Date end){
		Query q = getSession().createQuery("from Event e where " +
				"e.user.id = :userId " + 
				"and (e.startTime > :start or e.startTime < :end)")
			.setParameter("userId", userId)
			.setParameter("start", start)
			.setParameter("end", end);
		return q.list();
	}
	
	public List<Event> findCreatedAfter(long userId, Date time) {
        return findByCriteria(
        		Restrictions.eq("user.id", userId),
        		Restrictions.gt("creationTime", time));
    }
	
	public List<Event> findByUser(long authorId, int startPage, int endPage){
//		Query q = getSession().createQuery("from Event where user.id = :userId")
//			.setParameter("userId", authorId);
//		return q.list();
//		
//		Criteria c = getSession().createCriteria(Event.class)
//				.add()
		//TODO pagination
		throw new UnsupportedOperationException();
	}
	
}