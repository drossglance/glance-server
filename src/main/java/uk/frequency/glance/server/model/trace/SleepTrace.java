package uk.frequency.glance.server.model.trace;

import javax.persistence.Entity;

import uk.frequency.glance.server.model.component.Position;

@Entity
public class SleepTrace extends Trace {

	Boolean begin; //false means it's end
	
	Position position;

	public Boolean isBegin() {
		return begin;
	}

	public void setBegin(Boolean begin) {
		this.begin = begin;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

}
