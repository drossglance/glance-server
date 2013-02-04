package uk.frequencymobile.flow.server.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Likes extends GenericEntity {

	@OneToOne
	User user;
	
	@OneToOne
	Event event;
	
}
