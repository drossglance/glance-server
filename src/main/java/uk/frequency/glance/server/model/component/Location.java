package uk.frequency.glance.server.model.component;

import javax.persistence.Embeddable;

@Embeddable
public class Location{

	Position position;
	
	String name;
	
	String address;

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
	
	@Override
	public String toString() {
		return super.toString()
				+ " | " + name;
	}
	
}
