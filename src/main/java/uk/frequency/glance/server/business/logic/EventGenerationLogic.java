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
import uk.frequency.glance.server.model.Location;
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

	private final int TIME_WINDOW = 2 * 60 * 1000; //(in miliseconds) time window in which recent traces are evaluated
	private final double STAY_RADIUS = LatLngGeometryUtil.metersToDegrees(50);
	private final double TELEPORT_DISTANCE = 10 * STAY_RADIUS; //min distance to alow a "teleport" change from one stay event to another

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
		
		if(recent.isLongEnough){
			
			if (currentEvent == null) {

				if (recent.startedMoving) {
					//create a move event
					EventDataFinder finder = new EventDataFinder(recent.moveBegin.getPosition());
					MoveEvent newEvent = createMoveEvent(user, finder, recent.moveBegin.getTime());
					eventDal.save(newEvent);
					genInfo.setCurrentEvent(newEvent);

					/*DEBUG*/System.out.println("MOVE: " + DebugUtil.timeStr(recent.moveBegin.getTime()) + ", " + finder.getLocation().getPosition());
				}else if(recent.isStable){
					//create a stay event
					EventDataFinder finder = new EventDataFinder(recent.center);
					StayEvent newEvent = createStayEvent(user, finder, recent.first.getTime());
					eventDal.save(newEvent);
					genInfo.setCurrentEvent(newEvent);
					
					/*DEBUG*/System.out.println("STAY: " + DebugUtil.timeStr(recent.first.getTime()) + ", " + finder.getLocation().getPosition());
				}
					
			} else  if (currentEvent instanceof StayEvent) {
				StayEvent stay = ((StayEvent) currentEvent);
	
				if (recent.startedMoving) {

					// close stay Event
					PositionTrace previous = recent.moveBegin;
					closeStayEvent(stay, previous.getTime());
					eventDal.save(stay);
					
					//create a move event
					Event newEvent = createMoveEvent(user, stay, previous.getTime());
					eventDal.save(newEvent);
					genInfo.setCurrentEvent(newEvent);
					
					/*DEBUG*/System.out.println("STAY to MOVE: " + DebugUtil.timeStr(previous.getTime()));
				}else{
					
					boolean teleported = recent.isStable 
							&& LatLngGeometryUtil.distance(recent.center, stay.getLocation().getPosition()) 
								> TELEPORT_DISTANCE*2;
					if(teleported){
						
						// close stay Event
						PositionTrace previous = genInfo.getLastPositionTrace();
						closeStayEvent(stay, previous.getTime());
						eventDal.save(stay);
						
						// create stay Event
						EventDataFinder finder = new EventDataFinder(recent.center);
						Event newEvent = createStayEvent(user, finder, recent.first.getTime());
						eventDal.save(newEvent);
						genInfo.setCurrentEvent(newEvent);
						
						/*DEBUG*/System.out.println("STAY to STAY: " + DebugUtil.timeStr(recent.first.getTime()) + ", " + finder.getLocation().getPosition());
					}
				}

			}else if (currentEvent instanceof MoveEvent) {
				MoveEvent move = ((MoveEvent) currentEvent);
				move.getTrail().add(currentTrace.getPosition());
				
				if(recent.isStable){
					
					// close move Event
					EventDataFinder finder = new EventDataFinder(recent.center);
					closeMoveEvent(move, recent.first.getTime(), finder.getLocation());
					
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
			
		}else{
			/*DEBUG*/new RecentTraces(user);
		}
		
		//update event generation data and persist
		genInfo.setLastPositionTrace(currentTrace);
		userDal.merge(genInfo); //TODO shouldn't need this. figure out why is hibernate throwing NonUniqueObjectException
//		userDal.makePersistent(genInfo);
		
		// TODO notify client device
	}
	
	private StayEvent createStayEvent(User user, EventDataFinder finder, Date start) {
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
	
	private void closeStayEvent(StayEvent event, Date end){
		event.setEndTime(end);
		
		double hours = TimeUtil.getDurationInHours(event.getStartTime(), event.getEndTime());
		EventScore score = new EventScore();
		if(hours < 1){
			score.setRelevance(3f);
		}else if(hours < 4){
			score.setRelevance(2f);
		}else{
			score.setRelevance(1f);
		}
		//TODO new location: +2
		event.setScore(score);
	}
	
	private MoveEvent createMoveEvent(User user, EventDataFinder finder, Date start) {
		Location location = finder.getLocation();
		String imageUrl = finder.getImageUrl();
		return createMoveEvent(user, location, imageUrl, start);
	}
	
	private MoveEvent createMoveEvent(User user, StayEvent stay, Date start) {
		Location location = stay.getLocation();
		String imageUrl = stay.getMedia().get(0).getUrl();
		return createMoveEvent(user, location, imageUrl, start);
	}
	
	private MoveEvent createMoveEvent(User user, Location location, String imageUrl, Date start) {
		Media media = new Media();
		media.setType(MediaType.IMAGE);
		media.setUrl(imageUrl);
		List<Position> trail = new ArrayList<Position>();
		trail.add(location.getPosition());
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

	private void closeMoveEvent(MoveEvent event, Date end, Location location){
		event.setEndTime(end);
		event.setEndLocation(location);
		
		double hours = TimeUtil.getDurationInHours(event.getStartTime(), event.getEndTime());
		EventScore score = new EventScore();
		if(hours < .5){
			score.setRelevance(2f);
		}else if(hours < 2){
			score.setRelevance(3f);
		}else{
			score.setRelevance(4f);
		}
		event.setScore(score);
	}
	
	private class RecentTraces{
		
		PositionTrace first;
		PositionTrace moveBegin;
		Position center;
		boolean startedMoving;
		boolean isStable;
		boolean isLongEnough;
		
		RecentTraces(User user){
			Date timeWindowStart = TimeUtil.add(currentTrace.getTime(), -TIME_WINDOW);
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
					
					isStable = !box3.canContainCircle(STAY_RADIUS);
					
					boolean wasStable = !box1.canContainCircle(STAY_RADIUS);
					boolean changedPosition = wasStable && box2.canContainCircle(STAY_RADIUS);
					boolean keepsChangingPosition = changedPosition && box3.canContainCircle(STAY_RADIUS);
					
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
