package uk.frequency.glance.server.model.event;

import javax.persistence.Embeddable;

@Embeddable
public class EventScore {

	Float work = 0f;
	
	Float fun = 0f;
	
	Float health = 0f;
	
	Float relevance = 0f;
	
	public Float getWork() {
		return work;
	}

	public void setWork(Float work) {
		this.work = work;
	}

	public Float getFun() {
		return fun;
	}

	public void setFun(Float fun) {
		this.fun = fun;
	}

	public Float getHealth() {
		return health;
	}

	public void setHealth(Float health) {
		this.health = health;
	}

	public Float getRelevance() {
		return relevance;
	}

	public void setRelevance(Float relevance) {
		this.relevance = relevance;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + work
				+ " , " + fun
				+ " , " + health;
	}

}