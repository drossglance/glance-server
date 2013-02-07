package uk.frequencymobile.flow.server.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Feeling extends UserExpression {

	public enum Type{ LIKES, DISLIKES }
	
	Type type;
	
}
