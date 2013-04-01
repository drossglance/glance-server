package uk.frequency.glance.server.business.logic.event;

import uk.frequency.glance.server.business.logic.TimeUtil;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.EventScore;
import uk.frequency.glance.server.model.event.EventType;
import uk.frequency.glance.server.model.event.MoveEvent;
import uk.frequency.glance.server.model.event.StayEvent;

public class EventScoreLogic {

	public static EventScore assignScore(Event event){
		if(event instanceof StayEvent){
			StayEvent stay = (StayEvent) event;
			if(stay.getType() == EventType.SLEEP){
				return sleepScore(stay);
			}else if(stay.getType() == EventType.WAKE){
				return wakeScore(stay);
			}else{
				return stayScore(stay);
			}
		}else if(event instanceof MoveEvent){
			MoveEvent move = (MoveEvent) event;
			return moveScore(move);
		}else{
			return null;
		}
	}
	
	private static EventScore stayScore(StayEvent event){
		double hours = TimeUtil.getDurationInHours(event.getStartTime(), event.getEndTime());
		EventScore score = new EventScore();
		if(hours < 1){
			score.setRelevance(3f);
		}else if(hours < 4){
			score.setRelevance(2f);
		}else{
			score.setRelevance(1f);
		}
		//TODO new location: +2
		return score;
	}
	
	private static EventScore moveScore(MoveEvent event){
		double hours = TimeUtil.getDurationInHours(event.getStartTime(), event.getEndTime());
		EventScore score = new EventScore();
		if(hours < .5){
			score.setRelevance(2f);
		}else if(hours < 2){
			score.setRelevance(3f);
		}else{
			score.setRelevance(4f);
		}
		return score;
	}
	
	private static EventScore sleepScore(StayEvent event){
		double hours = TimeUtil.getDurationInHours(event.getStartTime(), event.getEndTime());
		EventScore score = new EventScore();
		if(hours < 3){
			score.setRelevance(0f);
		}else if(hours < 5){
			score.setRelevance(3f);
		}else if(hours < 7){
			score.setRelevance(2f);
		}else if(hours < 9){
			score.setRelevance(1f);
		}else{
			score.setRelevance(0f);
		}
		return score;
	}
	
	private static EventScore wakeScore(StayEvent event){
		double hours = TimeUtil.getHoursInTheDay(event.getStartTime());
		EventScore score = new EventScore();
		if(hours < 5){
			score.setRelevance(0f);
		}else if(hours < 6){
			score.setRelevance(3f);
		}else if(hours < 7){
			score.setRelevance(2.75f);
		}else if(hours < 8){
			score.setRelevance(2.5f);
		}else if(hours < 9){
			score.setRelevance(2.25f);
		}else{
			score.setRelevance(2f);
		}
		return score;
	}
	
}
