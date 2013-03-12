package uk.frequency.glance.server.business;

import java.util.Date;
import java.util.List;

import org.hibernate.ObjectNotFoundException;

import uk.frequency.glance.server.business.logic.EventGenerationLogic;
import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.data_access.TraceDAL;
import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.trace.Trace;

public class EventBL extends GenericBL<Event>{

	private final EventDAL eventDal;
	private final TraceDAL traceDal;
	private final UserDAL userDal;

	public EventBL() {
		super(new EventDAL());
		eventDal = (EventDAL)dal;
		traceDal = new TraceDAL();
		userDal = new UserDAL();
	}
	
	public List<Event> findByUser(long userId) throws ObjectNotFoundException {
		List<Event> list = eventDal.findByUser(userId);
		return list;
	}
	
	public List<Event> findByTimeRange(Date start, Date end){
		return eventDal.findByTimeRange(start, end);
	}
	
	public List<Event> findByTimeRange(long userId, Date start, Date end){
		return eventDal.findByTimeRange(userId, start, end);
	}
	
	public List<Event> findCreatedAfter(long userId, Date time) throws ObjectNotFoundException {
		List<Event> entities = eventDal.findCreatedAfter(userId, time);
		return entities; 
	}
	
	public void onTraceReceived(Trace trace){
		new EventGenerationLogic(trace, eventDal, traceDal, userDal).start();
	}
	
}
