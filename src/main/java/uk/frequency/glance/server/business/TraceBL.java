package uk.frequency.glance.server.business;

import java.util.Date;
import java.util.List;

import uk.frequency.glance.server.data_access.TraceDAL;
import uk.frequency.glance.server.model.trace.PositionTrace;
import uk.frequency.glance.server.model.trace.Trace;

public class TraceBL extends GenericBL<Trace>{

	TraceDAL traceDal;
	EventBL eventBl;

	public TraceBL() {
		super(new TraceDAL());
		traceDal = (TraceDAL)dal;
		eventBl = new EventBL();
	}
	
	public List<Trace> findByUser(long userId) {
		return traceDal.findByUser(userId);
	}
	
	public List<Trace> findRecent(long userId){
		return traceDal.findRecent(userId, 50);
	}
	
	public PositionTrace findMostRecentPositionTrace(long userId){
		return traceDal.findMostRecentPositionTrace(userId);
	}
	
	public List<Trace> find(long userId, Date begin, Date end){
		return traceDal.find(userId, begin, end);
	}
	
	@Override
	public Trace create(Trace entity) {
		Trace trace = super.create(entity);
		eventBl.onTraceReceived(trace);
		return trace;
	}
	
}
