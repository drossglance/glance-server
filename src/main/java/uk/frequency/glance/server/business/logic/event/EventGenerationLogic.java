package uk.frequency.glance.server.business.logic.event;

import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import uk.frequency.glance.server.business.logic.geometry.LatLngGeometryUtil;
import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.data_access.TraceDAL;
import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.data_access.util.HibernateConfig;
import uk.frequency.glance.server.model.trace.PositionTrace;
import uk.frequency.glance.server.model.trace.SleepTrace;
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
			handleTrace();
			tr.commit();
		}catch(ConstraintViolationException e){
			//probably  traces with repeated time
			System.err.println(e.getMessage());
			if (tr.isActive()) {
				tr.rollback();
			}
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
			PositionTrace pos = (PositionTrace) currentTrace;
			new StayMoveLogic(eventDal, traceDal, userDal, genInfo).handleTrace(pos);
		} else if (currentTrace instanceof SleepTrace) {
			SleepTrace sleep = (SleepTrace) currentTrace;
			new SleepLogic(eventDal, traceDal, userDal, genInfo).handleTrace(sleep);
		} else {
			throw new AssertionError();
		}
	}

}
