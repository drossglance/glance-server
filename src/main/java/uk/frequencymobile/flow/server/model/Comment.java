package uk.frequencymobile.flow.server.model;

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

}
