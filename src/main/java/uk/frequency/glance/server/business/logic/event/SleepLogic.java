package uk.frequency.glance.server.business.logic.event;

import java.util.Date;

import uk.frequency.glance.server.business.remote.EventDataFinder;
import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.data_access.TraceDAL;
import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.component.Position;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.EventScore;
import uk.frequency.glance.server.model.event.EventType;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.trace.SleepTrace;
import uk.frequency.glance.server.model.user.EventGenerationInfo;
import uk.frequency.glance.server.model.user.User;

public class SleepLogic extends Thread {

	EventGenerationInfo genInfo;

	EventDAL eventDal;
	TraceDAL traceDal;
	UserDAL userDal;

	public SleepLogic(EventDAL eventDal, TraceDAL traceDal, UserDAL userDal, EventGenerationInfo genInfo) {
		this.genInfo = genInfo;
		this.eventDal = eventDal;
		this.traceDal = traceDal;
		this.userDal = userDal;
	}

	public void handleTrace(SleepTrace currentTrace) {
		
		User user = genInfo.getUser();
		
		if(currentTrace.isBegin()){
			Position pos = currentTrace.getPosition();
			EventDataFinder finder = new EventDataFinder(pos);
			
			Event newEvent = createSleepEvent(user, new Date(), finder.getLocation());
			eventDal.save(newEvent);
			genInfo.setCurrentSleepEvent(newEvent);
		}else{
			StayEvent currentEvent = (StayEvent) genInfo.getCurrentSleepEvent();
			if(currentEvent != null && currentEvent.getType() == EventType.SLEEP){
				closeSleepEvent(currentEvent, new Date());
				eventDal.save(currentEvent);
				genInfo.setCurrentSleepEvent(currentEvent);
				
				Event newEvent = createWakeEvent(user, new Date(), currentEvent.getLocation());
				eventDal.save(newEvent);
				genInfo.setCurrentSleepEvent(newEvent);
			}else{
				System.err.println("WARNING: attempting to close innexistent sleep event."); //FIXME shouldn't happend
				throw new AssertionError();
			}
		}
		
		userDal.merge(genInfo);
	}
	
	private Event createSleepEvent(User user, Date start, Location location){
		String imageUrl = "http://cdn.ebaumsworld.com/mediaFiles/picture/1984035/81487125.jpg"; //TODO pick from fixed set of sleep images on server
		
		StayEvent event = new StayEvent();
		event.setType(EventType.SLEEP);
		event.setUser(user);
		event.setStartTime(start);
		event.setLocation(location);
		event.setSingleImage(imageUrl);
		event.setScore(new EventScore());
		return event;
	}
	
	private void closeSleepEvent(StayEvent event, Date end){
		event.setEndTime(end);
		EventScore score = EventScoreLogic.assignScore(event);
		event.setScore(score);
	}
	
	private Event createWakeEvent(User user, Date time, Location location){
		String imageUrl = "http://janeannethorne.files.wordpress.com/2013/02/sunshine.jpg"; //TODO pick from fixed set of sleep images on server
		
		StayEvent event = new StayEvent();
		event.setType(EventType.WAKE);
		event.setUser(user);
		event.setStartTime(time);
		event.setEndTime(time);
		event.setLocation(location);
		event.setSingleImage(imageUrl);
		event.setScore(new EventScore());
		return event;
	}
	
}
