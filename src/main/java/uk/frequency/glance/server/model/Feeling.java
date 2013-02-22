package uk.frequency.glance.server.model;

import javax.persistence.Embeddable;

@Embeddable
public abstract class Feeling extends UserExpression {

	public enum FeelingValue{ LIKES, DISLIKES }
	
	FeelingValue value;
	
}
