package uk.frequency.glance.server.business;

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
		List<Trace> list = traceDal.findByUser(userId);
		return list;
	}
	
	@Override
	public Trace create(Trace entity) {
		Trace trace = super.create(entity);
		if (trace instanceof PositionTrace) { //TODO find better place/trigger for the event generation logic 
			eventBl.generateStayEvent((PositionTrace)trace);
		}
		return trace;
	}
	
}
