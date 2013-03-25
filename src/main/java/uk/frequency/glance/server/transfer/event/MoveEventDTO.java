package uk.frequency.glance.server.transfer.event;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.component.Position;

@XmlRootElement
@SuppressWarnings("serial")
public class MoveEventDTO extends EventDTO{

	Location startLocation;
	
	Location endLocation;
	
	List<Position> trail;

	public Location getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	public Location getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Location endLocation) {
		this.endLocation = endLocation;
	}

	public List<Position> getTrail() {
		return trail;
	}

	public void setTrail(List<Position> trail) {
		this.trail = trail;
	}

}
