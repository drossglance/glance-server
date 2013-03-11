package uk.frequency.glance.server.business.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Transaction;

import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.data_access.TraceDAL;
import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.data_access.util.HibernateUtil;
import uk.frequency.glance.server.business.remote.EventDataFinder;
import uk.frequency.glance.server.model.component.Location;
import uk.frequency.glance.server.model.component.Media;
import uk.frequency.glance.server.model.component.Media.MediaType;
import uk.frequency.glance.server.model.component.Position;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.EventScore;
import uk.frequency.glance.server.model.event.EventType;
import uk.frequency.glance.server.model.event.MoveEvent;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.trace.PositionTrace;
import uk.frequency.glance.server.model.trace.Trace;
import uk.frequency.glance.server.model.user.EventGenerationInfo;
import uk.frequency.glance.server.model.user.User;

public class EventGeneration extends Thread {

	Trace currentTrace;
	EventGenerationInfo genInfo;

	EventDAL eventDal;
	TraceDAL traceDal;
	UserDAL userDal;

	private final int MIN_STAY_TIME = 2 * 60 * 1000; //in miliseconds
	private final double MAX_STAY_RADIUS = GeometryUtil.metersToDegrees(20); //in degrees

	public EventGeneration(Trace currentTrace, EventDAL eventDal, TraceDAL traceDal, UserDAL userDal) {
		this.currentTrace = currentTrace;
		this.eventDal = eventDal;
		this.traceDal = traceDal;
		this.userDal = userDal;
	}

	@Override
	public void run() {
		Transaction tr = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		try{
			init();
			if (currentTrace instanceof PositionTrace) {
				handleTrace();
			}
			tr.commit();
		}catch(RuntimeException e){
			if(e instanceof ObjectNotFoundException){
				//failed because the currentTrace wasn't persisted, no worries
			}else{
				e.printStackTrace();
			}
            if (tr.isActive()) {
                tr.rollback();
            }
		}
	}
	
	/**
	 * contructor code, but runs on the new thread.
	 */
	private void init(){
		User user = userDal.findById(currentTrace.getUser().getId()); //unproxy from hibernate TODO: better way to do this?
		this.genInfo = user.getEventGenerationInfo();
	}
	
	private void handleTrace(){
		if (currentTrace instanceof PositionTrace) {
			handlePositionTrace((PositionTrace) currentTrace);
		} else if (currentTrace instanceof PositionTrace) {
			//TODO handle ListenTrace
		} else {
			throw new AssertionError();
		}
	}

	private void handlePositionTrace(PositionTrace currentTrace) {

		User user = genInfo.getUser();
		Event currentEvent = genInfo.getCurrentEvent();
		RecentTraces recent = new RecentTraces(user);
		
		if (currentEvent == null) {
			if(recent.isLongEnough){
				Event newEvent;
				if (recent.isNotMoving) {
					//create a stay event
					newEvent = createStayEvent(user, recent.center, recent.first.getTime());
					/*DEBUG*/System.out.println("STAY EVENT CREATED");
				}else{
					//create a move event
					newEvent = createMoveEvent(user, recent.lastButOne.getPosition(), recent.lastButOne.getTime());
					/*DEBUG*/System.out.println("MOVE EVENT CREATED");
				}
				eventDal.makePersistent(newEvent);
				genInfo.setCurrentEvent(newEvent);
			}
		} else {
			//update current event 
			
			if (currentEvent instanceof StayEvent) {
				StayEvent stay = ((StayEvent) currentEvent);
				double distance = GeometryUtil.distance(stay.getLocation().getPosition(), currentTrace.getPosition());
				if (distance > 2 * MAX_STAY_RADIUS) {
					// close stay Event
					PositionTrace previous = genInfo.getLastPositionTrace();
					stay.setEndTime(previous.getTime());
					genInfo.setCurrentEvent(null);
					eventDal.makePersistent(stay);
					/*DEBUG*/System.out.println("STAY EVENT CLOSED");
					
					//create a move event
					Event newEvent = createMoveEvent(user, previous.getPosition(), previous.getTime());
					eventDal.makePersistent(newEvent);
					genInfo.setCurrentEvent(newEvent);
					/*DEBUG*/System.out.println("MOVE EVENT CREATED");
				}

			}else if (currentEvent instanceof MoveEvent) {
				MoveEvent move = ((MoveEvent) currentEvent);
				move.getTrail().add(currentTrace.getPosition());
				
				if(recent.isNotMoving){
					// close move Event
					EventDataFinder finder = new EventDataFinder(recent.center);
					move.setEndLocation(finder.getLocation());
					move.setEndTime(recent.first.getTime());
					genInfo.setCurrentEvent(null);
					/*DEBUG*/System.out.println("MOVE EVENT CLOSED");
					
					//create a stay event
					Event newEvent = createStayEvent(user, recent.center, recent.first.getTime());
					eventDal.makePersistent(newEvent);
					genInfo.setCurrentEvent(newEvent);
					/*DEBUG*/System.out.println("STAY EVENT CREATED");
				}
				
				eventDal.makePersistent(move);
				
			} else {
				// TODO handle other types of events
				throw new AssertionError();
			}
		}

		//update event generation data and persist
		genInfo.setLastPositionTrace(currentTrace);
		userDal.merge(genInfo); //TODO shouldn't need this. figure out why is hibernate throwing NonUniqueObjectException
//		userDal.makePersistent(genInfo);
		
		// TODO notify client device
	}
	
	private Event createStayEvent(User user, Position pos, Date start) {
		EventDataFinder finder = new EventDataFinder(pos);
		Location location = finder.getLocation();
		String imageUrl = finder.getImageUrl();

		Media media = new Media();
		media.setType(MediaType.IMAGE);
		media.setUrl(imageUrl);
		StayEvent event = new StayEvent();
		event.setType(EventType.STAY);
		event.setUser(user);
		event.setStartTime(start);
		event.setLocation(location);
		event.setMedia(media);
		event.setScore(new EventScore());
		return event;
	}
	
	private Event createMoveEvent(User user, Position startPos, Date start) {
		EventDataFinder finder = new EventDataFinder(startPos);
		Location location = finder.getLocation();
		String imageUrl = finder.getImageUrl();

		Media media = new Media();
		media.setType(MediaType.IMAGE);
		media.setUrl(imageUrl);
		List<Position> trail = new ArrayList<Position>();
		trail.add(startPos);
		MoveEvent event = new MoveEvent();
		event.setType(EventType.STAY);
		event.setUser(user);
		event.setStartTime(start);
		event.setStartLocation(location);
		event.setTrail(trail);
		event.setMedia(media);
		event.setScore(new EventScore());
		return event;
	}
	
	private class RecentTraces{
		
		PositionTrace first;
		PositionTrace lastButOne;
		Position center;
		boolean isNotMoving;
		boolean isLongEnough;
		
		RecentTraces(User user){
			Date timeWindowStart = TimeUtil.add(currentTrace.getTime(), -MIN_STAY_TIME);
			first = traceDal.findRightBefore(user, timeWindowStart); //include the last trace right before the time window starts
			isLongEnough = first != null;
			if(isLongEnough){
				List<PositionTrace> traces = traceDal.findAfter(user, timeWindowStart);
				traces.add(0, first);
				BoundingBox box = BoundingBox.from(traces);
				isNotMoving = box.canContainCircle(MAX_STAY_RADIUS);
				center = box.findCenter();
				lastButOne = traces.get(traces.size()-2);
			}
		}
		
	}
}
