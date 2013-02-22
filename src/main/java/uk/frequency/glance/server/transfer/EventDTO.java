package uk.frequency.glance.server.transfer;

import java.util.List;

import uk.frequency.glance.server.model.Feeling;
import uk.frequency.glance.server.model.Media;
import uk.frequency.glance.server.model.event.Action;
import uk.frequency.glance.server.model.event.Event.EventType;
import uk.frequency.glance.server.model.event.EventScore;

@SuppressWarnings("serial")
public class EventDTO extends GenericDTO{

	long authorId;
	
	EventType type;
	
	EventScore score;
	
	List<Media> media;
	
	List<Action> actions;
	
	List<Long> participantIds;
	
	List<Long> commentIds;
	
	List<Feeling> feelings;
	
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

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
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

	public List<Feeling> getFeelings() {
		return feelings;
	}

	public void setFeelings(List<Feeling> feelings) {
		this.feelings = feelings;
	}

}
