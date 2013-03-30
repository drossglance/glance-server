package uk.frequency.glance.server.transfer.event;

import java.util.List;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import uk.frequency.glance.server.model.component.Media;
import uk.frequency.glance.server.model.event.EventScore;
import uk.frequency.glance.server.model.event.EventType;
import uk.frequency.glance.server.transfer.GenericDTO;

@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="@class")
@SuppressWarnings("serial")
public abstract class EventDTO extends GenericDTO{

	public Long userId;
	
	public EventType type;
	
	public Long startTime;
	
	public Long endTime;
	
	public EventScore score;
	
	public List<Media> media;
	
	public List<Long> participantIds;
	
	public List<Long> commentIds;
	
}
