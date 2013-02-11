package uk.frequency.glance.server.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Location{

	double lat;
	
	double lng;
	
	@Column(name="location_name")
	String name;
	
	@Column(name="location_address")
	String address;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
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
				+ " | " + lat
				+ " | " + lng
				+ " | " + name
				+ " | " + address;
	}
	
}
