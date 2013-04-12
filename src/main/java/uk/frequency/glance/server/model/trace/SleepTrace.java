package uk.frequency.glance.server.model.trace;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class SleepTrace extends Trace {

	Date begin;

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}
	
}
