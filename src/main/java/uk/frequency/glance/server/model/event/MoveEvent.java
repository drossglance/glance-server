package uk.frequency.glance.server.model.event;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.component.Position;

@Entity
public class MoveEvent extends Event {

//	@AttributeOverrides({
//		@AttributeOverride(name="name", column=@Column(name="start_loc_name")),
//		@AttributeOverride(name="address", column=@Column(name="start_loc_address")),
//		@AttributeOverride(name="position.lat", column=@Column(name="start_loc_lat")),
//		@AttributeOverride(name="position.lng", column=@Column(name="start_loc_lng"))
//	})
	@ManyToOne
	@JoinColumn
	@Cascade({CascadeType.SAVE_UPDATE})
	Location startLocation;
	
//	@AttributeOverrides({
//		@AttributeOverride(name="name", column=@Column(name="end_loc_name")),
//		@AttributeOverride(name="address", column=@Column(name="end_loc_address")),
//		@AttributeOverride(name="position.lat", column=@Column(name="end_loc_lat")),
//		@AttributeOverride(name="position.lng", column=@Column(name="end_loc_lng"))
//	})
	@ManyToOne
	@JoinColumn
	@Cascade({CascadeType.SAVE_UPDATE})
	Location endLocation;
	
	@ElementCollection
	@OrderColumn
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

	@Override
	public String toString() {
		return super.toString()
				+ " | " + startTime
				+ " | " + startLocation.getName();
	}

}