package uk.frequency.glance.server.model.event;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;

import uk.frequency.glance.server.model.component.Location;

@Entity
public class StayEvent extends Event {

	@AttributeOverrides({
		@AttributeOverride(name="name", column=@Column(name="loc_name")),
		@AttributeOverride(name="address", column=@Column(name="loc_address")),
		@AttributeOverride(name="position.lat", column=@Column(name="loc_lat")),
		@AttributeOverride(name="position.lng", column=@Column(name="loc_lng"))
	})
	Location location;
	
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