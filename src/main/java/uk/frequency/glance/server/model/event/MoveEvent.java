package uk.frequency.glance.server.model.event;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OrderColumn;

import uk.frequency.glance.server.model.component.Location;
import uk.frequency.glance.server.model.component.Position;

@Entity
public class MoveEvent extends Event {

	Date startTime;
	
	Date endTime;
	
	@AttributeOverrides({
		@AttributeOverride(name="name", column=@Column(name="start_loc_name")),
		@AttributeOverride(name="address", column=@Column(name="start_loc_address")),
		@AttributeOverride(name="position.lat", column=@Column(name="start_loc_lat")),
		@AttributeOverride(name="position.lng", column=@Column(name="start_loc_lng"))
	})
	Location startLocation;
	
	@AttributeOverrides({
		@AttributeOverride(name="name", column=@Column(name="end_loc_name")),
		@AttributeOverride(name="address", column=@Column(name="end_loc_address")),
		@AttributeOverride(name="position.lat", column=@Column(name="end_loc_lat")),
		@AttributeOverride(name="position.lng", column=@Column(name="end_loc_lng"))
	})
	Location endLocation;
	
	@ElementCollection
	@OrderColumn
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