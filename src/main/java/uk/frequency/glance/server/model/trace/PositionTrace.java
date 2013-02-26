package uk.frequency.glance.server.model.trace;

import javax.persistence.Entity;

import uk.frequency.glance.server.model.component.Position;

@Entity
public class PositionTrace extends Trace {

	Position position;
	
	double speed;

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
}
