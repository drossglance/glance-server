package uk.frequency.glance.server.model.event;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import uk.frequency.glance.server.model.Location;

@Entity
public class TellEvent extends Event {

//	@AttributeOverrides({
//		@AttributeOverride(name="name", column=@Column(name="loc_name")),
//		@AttributeOverride(name="address", column=@Column(name="loc_address")),
//		@AttributeOverride(name="position.lat", column=@Column(name="loc_lat")),
//		@AttributeOverride(name="position.lng", column=@Column(name="loc_lng"))
//	})
	@ManyToOne
	@JoinColumn
	@Cascade({CascadeType.SAVE_UPDATE})
	Location location;
	
	String text;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + location.getName()
				+ " | " + text;
	}

}