package uk.frequency.glance.server.model.event;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import uk.frequency.glance.server.model.Location;

@Entity
public class StayEvent extends Event {

	Date startTime;
	
	Date endTime;
	
	@Embedded
	Location location;
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + startTime
				+ " | " + location.getName();
	}

}