package uk.frequency.glance.server.transfer.event;

import static uk.frequency.glance.server.business.logic.PresentationUtil.*;
import static uk.frequency.glance.server.business.logic.TimeUtil.*;

import java.util.Date;

import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.EventType;
import uk.frequency.glance.server.model.event.ListenEvent;
import uk.frequency.glance.server.model.event.MoveEvent;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.event.TellEvent;


public class EventViewDTO {

	public String imageUrl;
	
	public String[] lines; 

	public static EventViewDTO from(Event event){
		boolean isHappening = event.getEndTime() == null;
		
		EventViewDTO dto = new EventViewDTO();
		dto.lines = new String[4];
		if(event instanceof TellEvent){
			TellEvent tell = (TellEvent)event;
			dto.lines[1] = toUpperCase(tell.getLocation().getName());
		}else if(event instanceof StayEvent){
			StayEvent stay = (StayEvent)event;
			if(event.getType() == EventType.SLEEP){
				String userName = event.getUser().getProfile().getFirstName();
				dto.lines[1] = toUpperCase(userName) + " IS AWAKE";
			}else if(event.getType() == EventType.JOIN){
				String userName = event.getUser().getProfile().getFirstName();
				dto.lines[0] = toUpperCase(userName) + " JOINED";
				dto.lines[1] = "GLANCE";
			}else{
				dto.lines[0] = isHappening? "ARRIVED AT" : null;
				dto.lines[1] = toUpperCase(stay.getLocation().getName());
			}
		}else if(event instanceof MoveEvent){
			MoveEvent move = (MoveEvent)event;
			if(isHappening){
				dto.lines[0] = "TRAVELING FROM";
				dto.lines[1] = toUpperCase(move.getStartLocation().getName());
			}else{
				String originStr = toUpperCase(move.getStartLocation().getName());
				String destStr = toUpperCase(move.getEndLocation().getName());
				dto.lines[0] = String.format("%s", originStr);
				dto.lines[1] = String.format("%s", destStr);
			}
		}else if(event instanceof ListenEvent){
			ListenEvent listen = (ListenEvent)event;
			dto.lines[1] = toUpperCase(listen.getSongMetadata());
		}else{
			throw new AssertionError();
		}

		
		Date start = event.getStartTime();
		Date end = event.getEndTime();
		if(event instanceof StayEvent && event.getType() == EventType.JOIN){
			dto.lines[3] = dateText(start);
		}else if(event instanceof StayEvent && event.getType() == EventType.JOIN){
			int duration = (int)getDurationInHours(start, end);
			dto.lines[2] = timeText(end);
			dto.lines[3] = String.format("Slept for %d hours", duration);
		}else{
			if(isBeforeToday(start)){
				dto.lines[2] = timePastTextDayPrecision(start); 
			}
			if(isHappening){
				dto.lines[3] = timeText(start);
			}else{
				String startStr = timeText(start);
				String endStr = timeText(end);
				if(startStr.equals(endStr)){
					dto.lines[3] = startStr;
				}else if(!isInSameDay(start, end)){
					dto.lines[2] = String.format("%s, %s", timePastTextDayPrecision(start), startStr); 
					dto.lines[3] = String.format("%s, %s", timePastTextDayPrecision(end), endStr);
				}else{
					dto.lines[3] = String.format("%s - %s", startStr, endStr);
				}
			}
		}
		
		if(event.getMedia() != null && event.getMedia().size() > 0){
			dto.imageUrl = event.getMedia().get(0).getUrl();
		}
		
		return dto;
	}
	
}
