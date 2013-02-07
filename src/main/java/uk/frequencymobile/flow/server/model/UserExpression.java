package uk.frequencymobile.flow.server.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class UserExpression extends GenericEntity{

	@ManyToOne
	@JoinColumn
	User author;

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
	
}
