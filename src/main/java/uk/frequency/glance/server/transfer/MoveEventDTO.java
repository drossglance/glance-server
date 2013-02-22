package uk.frequency.glance.server.transfer;

import java.util.List;

import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.Position;

@SuppressWarnings("serial")
public class MoveEventDTO extends EventDTO{

	long startTime;
	
	long endTime;
	
	Location startLocation;
	
	Location endLocation;
	
	List<Position> trail;

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

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
