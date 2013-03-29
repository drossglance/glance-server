package uk.frequency.glance.server.transfer.event;

import static uk.frequency.glance.server.business.logic.PresentationUtil.timePastTextDayPrecision;
import static uk.frequency.glance.server.business.logic.PresentationUtil.timeText;
import static uk.frequency.glance.server.business.logic.PresentationUtil.toUpperCase;

import java.util.Date;

import uk.frequency.glance.server.business.logic.TimeUtil;
import uk.frequency.glance.server.model.event.Event;
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
			dto.preTitle = isHappening? "ARRIVED AT" : null;
			dto.title = toUpperCase(stay.getLocation().getName());
		}else if(event instanceof MoveEvent){
			MoveEvent move = (MoveEvent)event;
			if(isHappening){
				dto.preTitle = "LEFT";
				dto.title = toUpperCase(move.getStartLocation().getName());
			}else{
				String originStr = toUpperCase(move.getStartLocation().getName());
				dto.preTitle = String.format("%s TO", originStr);
				dto.title = toUpperCase(move.getEndLocation().getName());
			}
		}else if(event instanceof ListenEvent){
			ListenEvent listen = (ListenEvent)event;
			dto.title = toUpperCase(listen.getSongMetadata());
		}else{
			throw new AssertionError();
		}

		Date start = event.getStartTime();
		if(TimeUtil.isBeforeToday(start)){
			dto.subtitle1 = timePastTextDayPrecision(start); 
		}
		if(isHappening){
			dto.subtitle2 = timeText(start);
		}else{
			Date end = event.getEndTime();
			String startStr = timeText(start);
			String endStr = timeText(end);
			dto.subtitle2 = String.format("%s - %s", startStr, endStr);
		}
		
		if(event.getMedia() != null && event.getMedia().size() > 0){
			dto.imageUrl = event.getMedia().get(0).getUrl();
		}
		
		return dto;
	}
	
}
