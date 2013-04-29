package uk.frequency.glance.server.model.event;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Proxy;

import uk.frequency.glance.server.model.Comment;
import uk.frequency.glance.server.model.UserExpression;
import uk.frequency.glance.server.model.component.Media;
import uk.frequency.glance.server.model.component.Media.MediaType;
import uk.frequency.glance.server.model.user.User;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Proxy(lazy=false)
public class Event extends UserExpression {

	@Enumerated(EnumType.STRING)
	EventType type;
	
	@Index(name="time_index")
	Date startTime;
	
	Date endTime;

	EventScore score;
	
	@Cascade(value=CascadeType.DELETE)
	@LazyCollection(LazyCollectionOption.FALSE)
	@ElementCollection
	List<Media> media;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	List<User> participants;

	@Cascade(value=CascadeType.DELETE)
	@OneToMany(mappedBy = "subject")
	List<Comment> comments;

	public void setSingleImage(String imageUrl){
		Media media = new Media();
		media.setType(MediaType.IMAGE);
		media.setUrl(imageUrl);
		setMedia(media);
	}
	
	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	@Override
	public String toString() {
		return super.toString()
				+ " | " + type
				+ " | " + startTime;
	}

}