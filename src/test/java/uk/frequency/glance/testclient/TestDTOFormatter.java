package uk.frequency.glance.testclient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.frequency.glance.server.transfer.GenericDTO;
import uk.frequency.glance.server.transfer.event.EventDTO;
import uk.frequency.glance.server.transfer.event.MoveEventDTO;
import uk.frequency.glance.server.transfer.event.StayEventDTO;
import uk.frequency.glance.server.transfer.trace.PositionTraceDTO;
import uk.frequency.glance.server.transfer.trace.TraceDTO;
import uk.frequency.glance.server.transfer.user.UserDTO;

public class TestDTOFormatter {

	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public static String format(Long time){
		if(time == null){
			return "";
		}else{
			return dateFormat.format(new Date(time));
		}
	}
	
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
				+ "\t" + format(event.getStartTime())
				+ "\t" + format(event.getEndTime());
		
		if(event instanceof StayEventDTO){
			StayEventDTO stay = (StayEventDTO) event;
			str += "\t" + stay.getLocation().getName()
					+ "\t" + stay.getLocation().getAddress();
		}else if(event instanceof MoveEventDTO){
				MoveEventDTO move = (MoveEventDTO) event;
				str += "\t" + move.getStartLocation().getName()
						+ "\t" + move.getStartLocation().getAddress();
				if(move.getEndLocation() != null){
					str +=  "\t" + move.getEndLocation().getName()
							+ "\t" + move.getEndLocation().getAddress();
				}
		} else {
			throw new AssertionError();
		}
		
		return str;
	}
	
	public static String format(TraceDTO trace){
		String str = "TRACE"
				+ "\t" + trace.getId()
				+ "\t" + trace.getUserId()
				+ "\t" + format(trace.getTime());
		
		if(trace instanceof PositionTraceDTO){
			PositionTraceDTO stay = (PositionTraceDTO) trace;
			str += "\t" + stay.getPosition();
		} else {
			throw new AssertionError();
		}
		
		return str;
	}
	
	public static String format(UserDTO trace){
		String str = "USER"
				+ "\t" + trace.getId()
				+ "\t" + trace.getUsername();
		return str;
	}
	
}
