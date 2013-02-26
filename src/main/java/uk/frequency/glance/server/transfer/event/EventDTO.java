package uk.frequency.glance.server.transfer.event;

import java.util.List;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import uk.frequency.glance.server.model.component.Media;
import uk.frequency.glance.server.model.event.Event.EventType;
import uk.frequency.glance.server.model.event.EventScore;
import uk.frequency.glance.server.transfer.GenericDTO;

@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="@class")
@SuppressWarnings("serial")
public abstract class EventDTO extends GenericDTO{

	long authorId;
	
	EventType type;
	
	EventScore score;
	
	List<Media> media;
	
	List<Long> participantIds;
	
	List<Long> commentIds;
	
	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public EventScore getScore() {
		return score;
	}

	public void setScore(EventScore score) {
		this.score = score;
	}

	public List<Media> getMedia() {
		return media;
	}

	public void setMedia(List<Media> media) {
		this.media = media;
	}

	public List<Long> getParticipantIds() {
		return participantIds;
	}

	public void setParticipantIds(List<Long> participantIds) {
		this.participantIds = participantIds;
	}

	public List<Long> getCommentIds() {
		return commentIds;
	}

	public void setCommentIds(List<Long> commentIds) {
		this.commentIds = commentIds;
	}

}
