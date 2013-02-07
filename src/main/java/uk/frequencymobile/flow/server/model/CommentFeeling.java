package uk.frequencymobile.flow.server.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CommentFeeling extends Feeling{

	@ManyToOne
	@JoinColumn
	Comment subject;
	
}
