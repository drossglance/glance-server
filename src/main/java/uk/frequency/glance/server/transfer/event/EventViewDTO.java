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
	
	public String preTitle;
	
	public String title;
	
	public String subtitle1;
	
	public String subtitle2;

	public static EventViewDTO from(Event event){
		boolean isHappening = event.getEndTime() == null;
		
		EventViewDTO dto = new EventViewDTO();
		if(event instanceof TellEvent){
			TellEvent tell = (TellEvent)event;
			dto.title = toUpperCase(tell.getLocation().getName());
		}else if(event instanceof StayEvent){
			StayEvent stay = (StayEvent)event;
			if(event.getType() == EventType.SLEEP){
				String userName = event.getUser().getProfile().getFirstName();
				dto.title = toUpperCase(userName) + " IS AWAKE";
			}else if(event.getType() == EventType.JOIN){
				String userName = event.getUser().getProfile().getFirstName();
				dto.preTitle = toUpperCase(userName) + " JOINED";
				dto.title = "GLANCE";
			}else{
				dto.preTitle = isHappening? "ARRIVED AT" : null;
				dto.title = toUpperCase(stay.getLocation().getName());
			}
		}else if(event instanceof MoveEvent){
			MoveEvent move = (MoveEvent)event;
			if(isHappening){
				dto.preTitle = "TRAVELING FROM";
				dto.title = toUpperCase(move.getStartLocation().getName());
			}else{
				String originStr = toUpperCase(move.getStartLocation().getName());
				String destStr = toUpperCase(move.getEndLocation().getName());
				dto.preTitle = String.format("%s", originStr);
				dto.title = String.format("%s", destStr);
			}
		}else if(event instanceof ListenEvent){
			ListenEvent listen = (ListenEvent)event;
			dto.title = toUpperCase(listen.getSongMetadata());
		}else{
			throw new AssertionError();
		}

		
		Date start = event.getStartTime();
		Date end = event.getEndTime();
		if(event instanceof StayEvent && event.getType() == EventType.JOIN){
			dto.subtitle2 = dateText(start);
		}else if(event instanceof StayEvent && event.getType() == EventType.JOIN){
			int duration = (int)getDurationInHours(start, end);
			dto.subtitle1 = timeText(end);
			dto.subtitle2 = String.format("Slept for %d hours", duration);
		}else{
			if(isBeforeToday(start)){
				dto.subtitle1 = timePastTextDayPrecision(start); 
			}
			if(isHappening){
				dto.subtitle2 = timeText(start);
			}else{
				String startStr = timeText(start);
				String endStr = timeText(end);
				if(startStr.equals(endStr)){
					dto.subtitle2 = startStr;
				}else if(!isInSameDay(start, end)){
					dto.subtitle1 = String.format("%s, %s", timePastTextDayPrecision(start), startStr); 
					dto.subtitle2 = String.format("%s, %s", timePastTextDayPrecision(end), endStr);
				}else{
					dto.subtitle2 = String.format("%s - %s", startStr, endStr);
				}
			}
		}
		
		if(event.getMedia() != null && event.getMedia().size() > 0){
			dto.imageUrl = event.getMedia().get(0).getUrl();
		}
		
		return dto;
	}
	
}
