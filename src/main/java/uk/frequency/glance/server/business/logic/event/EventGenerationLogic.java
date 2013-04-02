package uk.frequency.glance.server.business.logic.event;

import static uk.frequency.glance.server.business.logic.PresentationUtil.moveEventMapImageUrl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import uk.frequency.glance.server.business.logic.TimeUtil;
import uk.frequency.glance.server.business.logic.geometry.BoundingBox;
import uk.frequency.glance.server.business.logic.geometry.LatLngGeometryUtil;
import uk.frequency.glance.server.business.remote.EventDataFinder;
import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.data_access.TraceDAL;
import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.data_access.util.HibernateConfig;
import uk.frequency.glance.server.debug.DebugUtil;
import uk.frequency.glance.server.model.Location;
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

	public static final int TIME_WINDOW = 2 * 60 * 1000; //(in miliseconds) time window in which recent traces are evaluated
	public static final double BIG_RADIUS = LatLngGeometryUtil.metersToDegrees(50); //more tolerant for detecting stability
	public static final double SMALL_RADIUS = LatLngGeometryUtil.metersToDegrees(20); //more tolerant for detecting movement
	public static final double TELEPORT_DISTANCE = 10 * BIG_RADIUS; //min distance to alow a "teleport" change from one stay event to another
//	private static final int MAX_TRACE_TIME_GAP = 30 * 60 * 1000; //max time without receiving traces, for which a previous stay event is considered to be connected to the traces received after the gap

	public EventGenerationLogic(Trace currentTrace, EventDAL eventDal, TraceDAL traceDal, UserDAL userDal) {
		this.currentTrace = currentTrace;
		this.eventDal = eventDal;
		this.traceDal = traceDal;
		this.userDal = userDal;
	}

	@Override
	public void run() {
		Transaction tr = HibernateConfig.getSessionFactory().getCurrentSession().beginTransaction();
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
		double radius = currentEvent instanceof MoveEvent ? SMALL_RADIUS : BIG_RADIUS;
		RecentTraces recent = new RecentTraces(user, currentEvent, TIME_WINDOW, radius);
		
		if(recent.isLongEnough){
			
			if (currentEvent == null) {

				if (recent.isMoving) {
					//create a move event
					EventDataFinder finder = new EventDataFinder(recent.moveBeginPos);
					MoveEvent newEvent = createMoveEvent(user, finder, recent.moveBeginTime);
					eventDal.save(newEvent);
					genInfo.setCurrentEvent(newEvent);

					/*DEBUG*/System.out.println("MOVE: " + DebugUtil.timeStr(recent.moveBeginTime) + ", " + finder.getLocation().getPosition());
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
	
				if (recent.isMoving) {

					// close stay Event
					closeStayEvent(stay, recent.moveBeginTime);
					eventDal.save(stay);
					
					//create a move event
					Event newEvent = createMoveEvent(user, stay, recent.moveBeginTime);
					eventDal.save(newEvent);
					genInfo.setCurrentEvent(newEvent);
					
					/*DEBUG*/System.out.println("STAY to MOVE: " + DebugUtil.timeStr(recent.moveBeginTime));
					
				}else if(recent.hasTeleported(stay)){ // changed from one stable position to another without movement in-between
						
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

			}else if (currentEvent instanceof MoveEvent) {
				MoveEvent move = ((MoveEvent) currentEvent);
				updateMoveEvent(move, currentTrace.getPosition());
				
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

		StayEvent event = new StayEvent();
		event.setType(EventType.STAY);
		event.setUser(user);
		event.setStartTime(start);
		event.setLocation(location);
		event.setSingleImage(imageUrl);
		event.setScore(new EventScore());
		return event;
	}
	
	private void closeStayEvent(StayEvent event, Date end){
		event.setEndTime(end);
		EventScore score = EventScoreLogic.assignScore(event);
		event.setScore(score);
	}
	
	private MoveEvent createMoveEvent(User user, EventDataFinder finder, Date start) {
		Location location = finder.getLocation();
//		String imageUrl = finder.getImageUrl();
		return createMoveEvent(user, location, start);
	}
	
	private MoveEvent createMoveEvent(User user, StayEvent stay, Date start) {
		Location location = stay.getLocation();
//		String imageUrl = stay.getMedia().get(0).getUrl();
		return createMoveEvent(user, location, start);
	}
	
	private MoveEvent createMoveEvent(User user, Location location, Date start) {
		List<Position> trail = new ArrayList<Position>();
		trail.add(location.getPosition());
		MoveEvent event = new MoveEvent();
		event.setType(EventType.TRAVEL);
		event.setUser(user);
		event.setStartTime(start);
		event.setStartLocation(location);
		event.setTrail(trail);
		event.setScore(new EventScore());
		
		String imageUrl = moveEventMapImageUrl(event);
		event.setSingleImage(imageUrl);
		
		return event;
	}

	private void closeMoveEvent(MoveEvent event, Date end, Location location){
		event.setEndTime(end);
		event.setEndLocation(location);
		EventScore score = EventScoreLogic.assignScore(event);
		event.setScore(score);
		
		String imageUrl = moveEventMapImageUrl(event);
		event.setSingleImage(imageUrl);
	}
	
	private void updateMoveEvent(MoveEvent event, Position position){
		event.getTrail().add(position);
		String imageUrl = moveEventMapImageUrl(event);
		event.setSingleImage(imageUrl);
	}
	
	private class RecentTraces{
		
		//params
		Event currentEvent;
		User user;
		int timeWindow;
		double radius;
		
		//loaded data
		PositionTrace first;
		private List<PositionTrace> traces;
		boolean isLongEnough;
		
		//analysis
		Position center;
		Position moveBeginPos;
		Date moveBeginTime;
		boolean isMoving;
		boolean isStable;
		
		RecentTraces(User user, Event currentEvent, int timeWindow, double radius){
			this.currentEvent = currentEvent;
			this.user = user;
			this.timeWindow = timeWindow;
			this.radius = radius;
			load();
			if(isLongEnough){
				analyse();
			}
		}
		
		private void load(){
			Date timeWindowStart = TimeUtil.add(currentTrace.getTime(), -timeWindow);
			first = traceDal.findRightBefore(user, timeWindowStart); //include the last trace right before the time window starts
			isLongEnough = first != null;
			if(isLongEnough){
				traces = traceDal.findAfter(user, timeWindowStart);
				traces.add(0, first);
				isLongEnough = traces.size() > 2;
			}
		}
		
		private void analyse(){
			BoundingBox box1 = BoundingBox.fromTraces(traces.subList(0, traces.size()-2)); //2 traces ago
			BoundingBox box2 = BoundingBox.fromTraces(traces.subList(0, traces.size()-1)); //1 trace ago
			BoundingBox box3 = BoundingBox.fromTraces(traces); //current
			center = box3.findCenter();
			
			isStable = !box3.canContainCircle(radius);
			
			boolean wasStable = !box1.canContainCircle(radius);
			boolean changedPosition = box2.canContainCircle(radius);
			boolean keepsChangingPosition = changedPosition && box3.canContainCircle(radius);
			
			Position p1 = traces.get(traces.size()-2).getPosition(); //last position but one
			Position p2 = traces.get(traces.size()-1).getPosition(); //last position
			double distP1C = LatLngGeometryUtil.distance(center, p1);
			double distP2C = LatLngGeometryUtil.distance(center, p2);
			double distP1P2 = LatLngGeometryUtil.distance(p1, p2);
			boolean isDistanceGrowing = distP2C > distP1C && distP1P2 < distP1C;
			
			isMoving = keepsChangingPosition && isDistanceGrowing;
			if(isMoving){
				if(wasStable){
					PositionTrace moveBegin = traces.get(traces.size()-3);
					moveBeginPos = moveBegin.getPosition();
					moveBeginTime = moveBegin.getTime();
				}else{
					if(currentEvent instanceof StayEvent){
						StayEvent stay = (StayEvent) currentEvent;
						moveBeginPos = stay.getLocation().getPosition();
					}else{
						moveBeginPos = first.getPosition();
					}
					moveBeginTime = first.getTime();
				}
			}
		}
		
		// changed from one stable position to another without movement in-between
		boolean hasTeleported(StayEvent currentStay){
			return isStable 
					&& LatLngGeometryUtil.distance(center, currentStay.getLocation().getPosition()) 
						> TELEPORT_DISTANCE;
		}
		
	}
	
}
