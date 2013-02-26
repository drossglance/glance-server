package uk.frequency.glance.server.transfer.trace;

import javax.xml.bind.annotation.XmlRootElement;

import uk.frequency.glance.server.model.component.Position;

@XmlRootElement
@SuppressWarnings("serial")
public class PositionTraceDTO extends TraceDTO {

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
