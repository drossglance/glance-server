package uk.frequencymobile.flow.server.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Comment extends GenericEntity {

	String text;
	
	@OneToOne
	User author;
	
	@OneToOne
	Event event;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
}
