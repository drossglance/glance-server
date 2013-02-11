package uk.frequency.glance.server.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Comment extends UserContent {

	@ManyToOne
	@JoinColumn
	Event subject;
	
	@OneToMany(mappedBy="subject")
	List<CommentFeeling> feelings;

	public Event getSubject() {
		return subject;
	}

	public void setSubject(Event subject) {
		this.subject = subject;
	}

	public List<CommentFeeling> getFeelings() {
		return feelings;
	}

	public void setFeelings(List<CommentFeeling> feelings) {
		this.feelings = feelings;
	}

}
