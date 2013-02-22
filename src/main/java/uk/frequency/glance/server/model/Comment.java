package uk.frequency.glance.server.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import uk.frequency.glance.server.model.event.Event;

@Entity
public class Comment extends UserExpression {

	@ManyToOne
	@JoinColumn
	Event subject;
	
	String text;
	
	@Embedded
	Location location;

	@ElementCollection
	List<Media> media;
	
	@OneToMany(mappedBy="subject")
	List<Feeling> feelings;

	public Event getSubject() {
		return subject;
	}

	public void setSubject(Event subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<Media> getMedia() {
		return media;
	}

	public void setMedia(List<Media> media) {
		this.media = media;
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
				+ " | " + author.id
				+ " | " + text
				+ " | " + location;
	}

}
