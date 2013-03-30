package uk.frequency.glance.test;

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

	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:S");

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
				+ "\t" + event.id
				+ "\t" + event.type
				+ "\t" + format(event.startTime)
				+ "\t" + format(event.endTime);
		
		if(event instanceof StayEventDTO){
			StayEventDTO stay = (StayEventDTO) event;
			str += "\t" + stay.location.getName()
					+ "\t" + stay.location.getAddress();
		}else if(event instanceof MoveEventDTO){
				MoveEventDTO move = (MoveEventDTO) event;
				str += "\t" + move.startLocation.getName()
						+ "\t" + move.startLocation.getAddress();
				if(move.endLocation != null){
					str +=  "\t" + move.endLocation.getName()
							+ "\t" + move.endLocation.getAddress();
				}
		} else {
			throw new AssertionError();
		}
		
		return str;
	}
	
	public static String format(TraceDTO trace){
		String str = "TRACE"
				+ "\t" + trace.id
				+ "\t" + trace.userId
				+ "\t" + format(trace.time);
		
		if(trace instanceof PositionTraceDTO){
			PositionTraceDTO stay = (PositionTraceDTO) trace;
			str += "\t" + stay.position;
		} else {
			throw new AssertionError();
		}
		
		return str;
	}
	
	public static String format(UserDTO trace){
		String str = "USER"
				+ "\t" + trace.id
				+ "\t" + trace.username;
		return str;
	}
	
}
