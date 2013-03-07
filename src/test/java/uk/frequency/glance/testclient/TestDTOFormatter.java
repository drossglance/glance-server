package uk.frequency.glance.testclient;

import uk.frequency.glance.server.transfer.GenericDTO;
import uk.frequency.glance.server.transfer.UserDTO;
import uk.frequency.glance.server.transfer.event.EventDTO;
import uk.frequency.glance.server.transfer.event.StayEventDTO;
import uk.frequency.glance.server.transfer.trace.PositionTraceDTO;
import uk.frequency.glance.server.transfer.trace.TraceDTO;

public class TestDTOFormatter {

	public static String format(GenericDTO dto) {
		if (dto instanceof EventDTO)
			return format((EventDTO) dto);
		else if (dto instanceof TraceDTO)
			return format((TraceDTO) dto);
		else if (dto instanceof UserDTO)
			return format((UserDTO) dto);
		else
			throw new AssertionError();
	}
	
	public static String format(EventDTO event){
		String str = "EVENT"
				+ "\t" + event.getId()
				+ "\t" + event.getType()
				+ "\t" + event.getStartTime()
				+ "\t" + event.getEndTime();
		
		if(event instanceof StayEventDTO){
			StayEventDTO stay = (StayEventDTO) event;
			str += "\t" + stay.getLocation().getName()
					+ "\t" + stay.getLocation().getPosition().getLat()
					+ "\t" + stay.getLocation().getPosition().getLng();
		} else {
			throw new AssertionError();
		}
		
		return str;
	}
	
	public static String format(TraceDTO trace){
		String str = "TRACE"
				+ "\t" + trace.getId()
				+ "\t" + trace.getUserId()
				+ "\t" + trace.getTime();
		
		if(trace instanceof PositionTraceDTO){
			PositionTraceDTO stay = (PositionTraceDTO) trace;
			str += "\t" + stay.getPosition().getLat()
					+ "\t" + stay.getPosition().getLng();
		} else {
			throw new AssertionError();
		}
		
		return str;
	}
	
	public static String format(UserDTO trace){
		String str = "USER"
				+ "\t" + trace.getId()
				+ "\t" + trace.getProfile().getUserName();
		return str;
	}
	
}
