package uk.frequency.glance.server.data_access;

import java.util.List;

import org.hibernate.Query;

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
	
}
