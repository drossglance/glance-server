package uk.frequency.glance.server.business;

import java.util.Date;
import java.util.List;

import org.hibernate.ObjectNotFoundException;

import uk.frequency.glance.server.business.logic.EventGeneration;
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
	
	public void onTraceReceived(Trace trace){
		new EventGeneration(trace, eventDal, traceDal, userDal).start();
		
		/*PROVISORY*/
//		if(trace instanceof PositionTrace){
//			createStayEvent(trace.getUser(), ((PositionTrace) trace).getPosition(), trace.getTime(), trace.getTime());
//		}
	}
	
	/*PROVISORY*/
//	private Event createStayEvent(User user, Position pos, Date start, Date end) {
//		String imageUrl = GoogleAPIs.getStreetViewImageUrl(pos); // TODO get more from google places and also streetview with different headings, choose best image somehow
//		String name = GoogleAPIs.getLocationName(pos); // TODO get from google places
//		String address = GoogleAPIs.getLocationName(pos);
//
//		Location location = new Location();
//		location.setPosition(pos);
//		location.setAddress(address);
//		location.setName(name);
//		Media media = new Media();
//		media.setType(MediaType.IMAGE);
//		media.setUrl(imageUrl);
//		StayEvent event = new StayEvent();
//		event.setType(EventType.STAY);
//		event.setUser(user);
//		event.setStartTime(start);
//		event.setEndTime(end);
//		event.setLocation(location);
//		event.setMedia(media);
//		event.setScore(new EventScore());
//
//		return eventDal.makePersistent(event);
//	}
	
}
