package uk.frequency.glance.server.data_access;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

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
				"(e.class = TellEvent  " +
					"and (e.time > :start or e.time < :end ))" +
				"or ((e.class = StayEvent or e.class = MoveEvent or e.class = ListenEvent) " +
					"and (e.endTime > :start or e.startTime < :end ))")
			.setParameter("start", start)
			.setParameter("end", end);
		return q.list();
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