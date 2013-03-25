package uk.frequency.glance.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import uk.frequency.glance.server.model.component.Position;

@Entity
public class Location extends GenericEntity{

	Position position;
	
	String name;
	
	String address;
	
	String placesReference;
	
	@Column(unique=true)
	String placesId;
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPlacesReference() {
		return placesReference;
	}

	public void setPlacesReference(String placesReference) {
		this.placesReference = placesReference;
	}

	public String getPlacesId() {
		return placesId;
	}

	public void setPlacesId(String placesId) {
		this.placesId = placesId;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + name;
	}
	
}
