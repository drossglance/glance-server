package uk.frequency.glance.server.model.event;

import javax.persistence.Embeddable;

@Embeddable
public class EventScore {

	int work;
	
	int fun;
	
	int health;
	
	public int getWork() {
		return work;
	}

	public void setWork(int work) {
		this.work = work;
	}

	public int getFun() {
		return fun;
	}

	public void setFun(int fun) {
		this.fun = fun;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
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