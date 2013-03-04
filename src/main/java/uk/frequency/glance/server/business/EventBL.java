package uk.frequency.glance.server.business;

import java.util.Date;
import java.util.List;

import org.hibernate.ObjectNotFoundException;

import uk.frequency.glance.server.business.remote.GoogleAPIs;
import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.model.component.Location;
import uk.frequency.glance.server.model.component.Media;
import uk.frequency.glance.server.model.component.Media.MediaType;
import uk.frequency.glance.server.model.component.Position;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.EventScore;
import uk.frequency.glance.server.model.event.EventType;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.trace.PositionTrace;
import uk.frequency.glance.server.model.trace.Trace;
import uk.frequency.glance.server.model.user.User;

public class EventBL extends GenericBL<Event>{

	private final EventDAL eventDal;

	public EventBL() {
		super(new EventDAL());
		eventDal = (EventDAL)dal;
	}
	
	public List<Event> findByUser(long userId) throws ObjectNotFoundException {
		List<Event> list = eventDal.findByUser(userId);
		return list;
	}
	
	public List<Event> findByTimeRange(Date start, Date end){
		return eventDal.findByTimeRange(start, end);
	}
	
	public void onTraceReceived(Trace trace){
//		new EventGeneration(trace, eventDal, traceDal, userDal).start();
		if(trace instanceof PositionTrace){
			createStayEvent(trace.getUser(), ((PositionTrace) trace).getPosition(), trace.getTime(), trace.getTime());
		}
	}
	
	/*PROVISORY*/
	private Event createStayEvent(User user, Position pos, Date start, Date end) {
		String imageUrl = GoogleAPIs.getStreetViewImageUrl(pos); // TODO get more from google places and also streetview with different headings, choose best image somehow
		String name = GoogleAPIs.getLocationName(pos); // TODO get from google places
		String address = GoogleAPIs.getLocationName(pos);

		Location location = new Location();
		location.setPosition(pos);
		location.setAddress(address);
		location.setName(name);
		Media media = new Media();
		media.setType(MediaType.IMAGE);
		media.setUrl(imageUrl);
		StayEvent event = new StayEvent();
		event.setType(EventType.STAY);
		event.setUser(user);
		event.setStartTime(start);
		event.setEndTime(end);
		event.setLocation(location);
		event.setMedia(media);
		event.setScore(new EventScore());

		return eventDal.makePersistent(event);
	}
	
}
