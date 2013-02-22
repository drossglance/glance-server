package uk.frequency.glance.server.transfer;

import uk.frequency.glance.server.model.Location;

@SuppressWarnings("serial")
public class StayEventDTO extends EventDTO{

	long startTime;
	
	long endTime;
	
	Location location;

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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
