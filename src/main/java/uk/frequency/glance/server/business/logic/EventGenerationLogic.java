package uk.frequency.glance.server.business.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import uk.frequency.glance.server.DebugUtil;
import uk.frequency.glance.server.business.remote.EventDataFinder;
import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.data_access.TraceDAL;
import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.data_access.util.HibernateUtil;
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

public class EventGenerationLogic extends Thread {

	Trace currentTrace;
	EventGenerationInfo genInfo;

	EventDAL eventDal;
	TraceDAL traceDal;
	UserDAL userDal;

	private final int MIN_STAY_TIME = 2 * 60 * 1000; //in miliseconds
	private final double MAX_STAY_RADIUS = LatLngGeometryUtil.metersToDegrees(35);

	public EventGenerationLogic(Trace currentTrace, EventDAL eventDal, TraceDAL traceDal, UserDAL userDal) {
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
		}catch(ConstraintViolationException e){
			//probably traces with repeated time
		}catch(RuntimeException e){
			e.printStackTrace();
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
				if (recent.startedMoving) {
					//create a move event
					EventDataFinder finder = new EventDataFinder(recent.moveBegin.getPosition());
					newEvent = createMoveEvent(user, finder, recent.moveBegin.getTime());
					/*DEBUG*/System.out.println("MOVE: " + DebugUtil.timeStr(recent.moveBegin.getTime()) + ", " + finder.getLocation().getPosition());
					eventDal.save(newEvent);
					genInfo.setCurrentEvent(newEvent);

				}else if(recent.isStable){
					//create a stay event
					EventDataFinder finder = new EventDataFinder(recent.center);
					newEvent = createStayEvent(user, finder, recent.first.getTime());
					/*DEBUG*/System.out.println("STAY: " + DebugUtil.timeStr(recent.first.getTime()) + ", " + finder.getLocation().getPosition());
					eventDal.save(newEvent);
					genInfo.setCurrentEvent(newEvent);
				}
			}
		} else {
			//update current event 
			
			if (currentEvent instanceof StayEvent) {
				StayEvent stay = ((StayEvent) currentEvent);

				if (recent.startedMoving) {

					// close stay Event
					PositionTrace previous = recent.moveBegin;
					/*DEBUG*/System.out.println("STAY to MOVE: " + DebugUtil.timeStr(previous.getTime()));
					stay.setEndTime(previous.getTime());
					eventDal.save(stay);
					
					//create a move event
					Event newEvent = createMoveEvent(user, stay, previous.getTime());
					eventDal.save(newEvent);
					genInfo.setCurrentEvent(newEvent);
				}else{
					if(recent.isLongEnough){ //TODO shouldn't need
						boolean isStableInNewLocation = LatLngGeometryUtil.distance(recent.center, stay.getLocation().getPosition()) > MAX_STAY_RADIUS*2;
						if(recent.isStable && isStableInNewLocation){
							
							// close stay Event
							PositionTrace previous = genInfo.getLastPositionTrace();
							stay.setEndTime(previous.getTime());
							eventDal.save(stay);
							
							// create stay Event
							/*DEBUG*/System.out.println("STAY to STAY: " + DebugUtil.timeStr(currentTrace.getTime()) + ", " + recent.center);
							EventDataFinder finder = new EventDataFinder(recent.center);
							Event newEvent = createStayEvent(user, finder, currentTrace.getTime());
							eventDal.save(newEvent);
							genInfo.setCurrentEvent(newEvent);
						}
					}
				}

			}else if (currentEvent instanceof MoveEvent) {
				MoveEvent move = ((MoveEvent) currentEvent);
				move.getTrail().add(currentTrace.getPosition());
				
				if(recent.isStable){
					
					// close move Event
					EventDataFinder finder = new EventDataFinder(recent.center);
					move.setEndLocation(finder.getLocation());
					move.setEndTime(recent.first.getTime());
					
					//create a stay event
					Event newEvent = createStayEvent(user, finder, recent.first.getTime());
					eventDal.save(newEvent);
					genInfo.setCurrentEvent(newEvent);
					/*DEBUG*/System.out.println("MOVE to STAY: " + DebugUtil.timeStr(recent.first.getTime()) + ", " + finder.getLocation().getPosition());
				}
				
				eventDal.save(move);
				
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
	
	private Event createStayEvent(User user, EventDataFinder finder, Date start) {
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
	
	private Event createMoveEvent(User user, EventDataFinder finder, Date start) {
		Location location = finder.getLocation();
		String imageUrl = finder.getImageUrl();

		Media media = new Media();
		media.setType(MediaType.IMAGE);
		media.setUrl(imageUrl);
		List<Position> trail = new ArrayList<Position>();
		trail.add(finder.getLocation().getPosition());
		MoveEvent event = new MoveEvent();
		event.setType(EventType.TRAVEL);
		event.setUser(user);
		event.setStartTime(start);
		event.setStartLocation(location);
		event.setTrail(trail);
		event.setMedia(media);
		event.setScore(new EventScore());
		return event;
	}
	
	private Event createMoveEvent(User user, StayEvent stay, Date start) {
		Location location = stay.getLocation();
		String imageUrl = stay.getMedia().get(0).getUrl();

		Media media = new Media();
		media.setType(MediaType.IMAGE);
		media.setUrl(imageUrl);
		List<Position> trail = new ArrayList<Position>();
		trail.add(stay.getLocation().getPosition());
		MoveEvent event = new MoveEvent();
		event.setType(EventType.TRAVEL);
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
		PositionTrace moveBegin;
		Position center;
		boolean startedMoving;
		boolean isStable;
		boolean isLongEnough;
		
		RecentTraces(User user){
			Date timeWindowStart = TimeUtil.add(currentTrace.getTime(), -MIN_STAY_TIME);
			first = traceDal.findRightBefore(user, timeWindowStart); //include the last trace right before the time window starts
			isLongEnough = first != null;
			if(isLongEnough){
				
				List<PositionTrace> traces = traceDal.findAfter(user, timeWindowStart);
				traces.add(0, first);
				isLongEnough = traces.size() > 2;
				if(isLongEnough){

					BoundingBox box1 = BoundingBox.fromTraces(traces.subList(0, traces.size()-2)); //2 traces ago
					BoundingBox box2 = BoundingBox.fromTraces(traces.subList(0, traces.size()-1)); //1 trace ago
					BoundingBox box3 = BoundingBox.fromTraces(traces); //current
					center = box3.findCenter();
					
					isStable = !box3.canContainCircle(MAX_STAY_RADIUS);
					
					boolean wasStable = !box1.canContainCircle(MAX_STAY_RADIUS);
					boolean changedPosition = wasStable && box2.canContainCircle(MAX_STAY_RADIUS);
					boolean keepsChangingPosition = changedPosition && box3.canContainCircle(MAX_STAY_RADIUS);
					
					Position p1 = traces.get(traces.size()-2).getPosition(); //last position but one
					Position p2 = traces.get(traces.size()-1).getPosition(); //last position
					double distP1C = LatLngGeometryUtil.distance(center, p1);
					double distP2C = LatLngGeometryUtil.distance(center, p2);
					double distP1P2 = LatLngGeometryUtil.distance(p1, p2);
					boolean isDistanceGrowing = distP2C > distP1C && distP1P2 < distP1C;
					
					startedMoving = keepsChangingPosition && isDistanceGrowing;
					moveBegin = startedMoving? traces.get(traces.size()-3) : null;
				}
			}
		}
		
	}
}
