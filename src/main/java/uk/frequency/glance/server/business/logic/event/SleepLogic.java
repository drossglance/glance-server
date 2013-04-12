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
		int index = genInfo.getSleepStaticImageIndex();
		User user = genInfo.getUser();
		Date begin = currentTrace.getBegin();
		Date end = currentTrace.getTime();
		
		Event newEvent = createSleepEvent(user, begin, end, index);
		eventDal.save(newEvent);
		
		genInfo.setSleepStaticImageIndex(index+1);
		userDal.merge(genInfo);
	}
	
	private Event createSleepEvent(User user, Date begin, Date end, int imageIndex){
		String imageUrl = StaticResourcesLoader.getImageUrl("sleep", imageIndex);
		
		StayEvent event = new StayEvent();
		event.setType(EventType.SLEEP);
		event.setUser(user);
		event.setStartTime(begin);
		event.setEndTime(begin);
		event.setSingleImage(imageUrl);
		
		EventScore score = EventScoreLogic.assignScore(event);
		event.setScore(score);
		return event;
	}
	
}
