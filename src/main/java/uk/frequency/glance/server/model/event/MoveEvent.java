package uk.frequency.glance.server.model.event;

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.Position;

@Entity
public class MoveEvent extends Event {

	Date startTime;
	
	Date endTime;
	
	@Embedded
	Location startLocation;
	
	@Embedded
	Location endLocation;
	
	@ElementCollection
	List<Position> trail;
	
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

	@Override
	public String toString() {
		return super.toString()
				+ " | " + startTime
				+ " | " + startLocation.getName();
	}

}