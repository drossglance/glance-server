package uk.frequency.glance.server.model.trace;

import javax.persistence.Entity;

@Entity
public class SleepTrace extends Trace {

	Boolean begin; //false means is end

	public Boolean isBegin() {
		return begin;
	}

	public void setBegin(Boolean begin) {
		this.begin = begin;
	}

}
