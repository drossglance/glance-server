package uk.frequency.glance.server.model.event;

import java.util.Arrays;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import uk.frequency.glance.server.model.Comment;
import uk.frequency.glance.server.model.Feeling;
import uk.frequency.glance.server.model.Media;
import uk.frequency.glance.server.model.User;
import uk.frequency.glance.server.model.UserExpression;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.JOINED)
public class Event extends UserExpression {

	public enum EventType {STAY, WORK, PUB, EXERCISE, SLEEP, WALK, CYCLING, COMUTE, TRAVEL, MEETING, MUSIC, MOVIE} //TODO define this right
	
	@OneToOne
	EventType type;
	
	@ElementCollection
	EventScore score;
	
	@ElementCollection
	List<Media> media;
	
	@ElementCollection
	List<Action> actions;
	
	@OneToMany
	List<User> participants;

	@OneToMany(mappedBy = "subject")
	List<Comment> comments;

	@OneToMany(mappedBy = "subject")
	List<Feeling> feelings;

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
	
	public void setMedia(Media... media) {
		this.media = Arrays.asList(media);
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public List<User> getParticipants() {
		return participants;
	}

	public void setParticipants(List<User> participants) {
		this.participants = participants;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Feeling> getFeelings() {
		return feelings;
	}

	public void setFeelings(List<Feeling> feelings) {
		this.feelings = feelings;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + author.getId();
	}

}