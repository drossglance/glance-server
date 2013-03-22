package uk.frequency.glance.server.model.event;

import javax.persistence.Embeddable;

@Embeddable
public class EventScore {

	Float work = 0f;
	
	Float fun = 0f;
	
	Float health = 0f;
	
	public float getWork() {
		return work;
	}

	public void setWork(float work) {
		this.work = work;
	}

	public float getFun() {
		return fun;
	}

	public void setFun(float fun) {
		this.fun = fun;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + work
				+ " , " + fun
				+ " , " + health;
	}

}