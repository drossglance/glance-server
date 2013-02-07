package uk.frequencymobile.flow.server.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class EventFeeling extends Feeling{

	@ManyToOne
	@JoinColumn
	Event subject;
	
}
