package uk.frequency.glance.server.data_access;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import uk.frequency.glance.server.model.trace.PositionTrace;
import uk.frequency.glance.server.model.trace.Trace;
import uk.frequency.glance.server.model.user.User;

@SuppressWarnings("unchecked")
public class TraceDAL extends GenericDAL<Trace>{

	public List<Trace> findByUser(User user){
		Query q = getSession().createQuery("from Event where user = :user")
			.setEntity("user", user);
		return q.list();
	}
	
	public List<Trace> findByUser(long userId){
		Query q = getSession().createQuery("from Trace where user.id = :userId")
			.setParameter("userId", userId);
		return q.list();
	}
	
	public List<Trace> find(User user, Date start, Date end){
		Query q = getSession().createQuery("from Trace where " +
				"user = :user " +
				"and (time >= :start or time < :end)")
			.setParameter("user", user)
			.setParameter("start", start)
			.setParameter("end", end);
		return q.list();
	}
	
	public List<PositionTrace> findAfter(User user, Date time){
		Query q = getSession().createQuery("from Trace where " +
				"trace.class = PositionTrace " +
				"and user.id = :user " +
				"and time > :time")
			.setParameter("user", user)
			.setParameter("time", time);
		return q.list();
	}
	
	public PositionTrace findRightBefore(User user, Date time){
		Query q = getSession().createQuery("from Trace where " +
				"time = (select max(time) from Trace where " +
				"trace.class = PositionTrace " +
				"and user.id = :user " +
				"and time < :time)")
			.setParameter("user", user)
			.setParameter("time", time);
		return (PositionTrace)q.uniqueResult();
	}
	
}
