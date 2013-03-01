package uk.frequency.glance.server.business.logic;

import java.util.Date;
import java.util.List;

import uk.frequency.glance.server.business.remote.GoogleAPIs;
import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.data_access.TraceDAL;
import uk.frequency.glance.server.data_access.UserDAL;
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
import uk.frequency.glance.server.model.user.EventGenerationInfo;
import uk.frequency.glance.server.model.user.User;

public class EventGeneration extends Thread{

	Trace currentTrace;
	EventGenerationInfo genInfo;
	
	EventDAL eventDal;
	TraceDAL traceDal;
	UserDAL userDal;
	
	private final int MIN_STAY_TIME = 10*60*1000;
	private final int MAX_STAY_RADIUS = 60;
	
	public EventGeneration(Trace currentTrace, EventDAL eventDal, TraceDAL traceDal, UserDAL userDal) {
		this.currentTrace = currentTrace;
		this.genInfo = currentTrace.getUser().getEventGenerationInfo();
		this.eventDal = eventDal;
		this.traceDal = traceDal;
		this.userDal = userDal;
	}

	@Override
	public void run() {
		if(currentTrace instanceof PositionTrace){
			examineTraces();
		}
	}
	
	private void examineTraces(){
		
		User user = genInfo.getUser();
		Event currentEvent = genInfo.getCurrentEvent();
		if(currentEvent == null){
			Date timeWindowStart = TimeUtil.add(currentTrace.getTime(), -MIN_STAY_TIME);
			List<PositionTrace> traces = traceDal.findAfter(user, timeWindowStart);
			PositionTrace previous = traceDal.findRightBefore(user, timeWindowStart);
			traces.add(0, previous);
			
			double minLat = Double.MAX_VALUE;
			double minLng = Double.MAX_VALUE;
			double maxLat = Double.MIN_VALUE;
			double maxLng = Double.MIN_VALUE;
			for(PositionTrace trace : traces){
				minLat = Math.min(minLat, trace.getPosition().getLat());
				maxLat = Math.min(maxLat, trace.getPosition().getLng());
				minLng = Math.min(minLng, trace.getPosition().getLat());
				maxLng = Math.min(maxLng, trace.getPosition().getLng());
			}
			if(maxLat-minLat < 2*MAX_STAY_RADIUS && maxLng-minLng < 2*MAX_STAY_RADIUS){
				double avgLat = (minLat+maxLat)/2;
				double avgLng = (minLng+maxLng)/2;
				Position pos = new Position();
				pos.setLat(avgLat);
				pos.setLng(avgLng);
				Date start = previous.getTime();
				Event event = createStayEvent(user, pos, start, null);
				
				genInfo.setCurrentEvent(event);
				genInfo.setLastUsedTrace(currentTrace);
				userDal.makePersistent(genInfo);
			}
		}else{
			//TODO update current event
		}
		
		//TODO notify
	}
	
	private Event createStayEvent(User user, Position pos, Date start, Date end){
		String imageUrl = GoogleAPIs.getStreetViewImageUrl(pos); //TODO get more from google places and also streetview with different headings, choose best image somehow
		String name = GoogleAPIs.getLocationName(pos); //TODO get from google places
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
