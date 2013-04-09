package uk.frequency.glance.server.business.logic.event;

import java.util.Date;

import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.data_access.StaticResourcesLoader;
import uk.frequency.glance.server.data_access.TraceDAL;
import uk.frequency.glance.server.data_access.UserDAL;
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
			int index = genInfo.getSleepStaticImageIndex();
			genInfo.setSleepStaticImageIndex(index+1);
			
			Event newEvent = createSleepEvent(user, new Date(), index);
			eventDal.save(newEvent);
			genInfo.setCurrentSleepEvent(newEvent);
		}else{
			StayEvent currentEvent = (StayEvent) genInfo.getCurrentSleepEvent();
			if(currentEvent != null && currentEvent.getType() == EventType.SLEEP){
				closeSleepEvent(currentEvent, new Date());
				eventDal.save(currentEvent);
				genInfo.setCurrentSleepEvent(currentEvent);
				
				int index = genInfo.getWakeStaticImageIndex();
				genInfo.setWakeStaticImageIndex(index+1);
				Event newEvent = createWakeEvent(user, new Date(), index);
				eventDal.save(newEvent);
				genInfo.setCurrentSleepEvent(newEvent);
			}else{
				System.err.println("WARNING: attempting to close innexistent sleep event."); //FIXME shouldn't happen
				throw new AssertionError();
			}
		}
		
		userDal.merge(genInfo);
	}
	
	private Event createSleepEvent(User user, Date start, int imageIndex){
		String imageUrl = StaticResourcesLoader.getImageUrl("sleep", imageIndex);
		
		StayEvent event = new StayEvent();
		event.setType(EventType.SLEEP);
		event.setUser(user);
		event.setStartTime(start);
		event.setSingleImage(imageUrl);
		event.setScore(new EventScore());
		return event;
	}
	
	private void closeSleepEvent(StayEvent event, Date end){
		event.setEndTime(end);
		EventScore score = EventScoreLogic.assignScore(event);
		event.setScore(score);
	}
	
	private Event createWakeEvent(User user, Date time, int imageIndex){
		String imageUrl = StaticResourcesLoader.getImageUrl("wake", imageIndex);
		
		StayEvent event = new StayEvent();
		event.setType(EventType.WAKE);
		event.setUser(user);
		event.setStartTime(time);
		event.setEndTime(time);
		event.setSingleImage(imageUrl);
		event.setScore(new EventScore());
		return event;
	}
	
}
