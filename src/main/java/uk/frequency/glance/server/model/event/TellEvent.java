package uk.frequency.glance.server.model.event;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import uk.frequency.glance.server.model.Location;

@Entity
public class TellEvent extends Event {

	Date time;
	
	@Embedded
	Location location;
	
	String text;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

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
				+ " | " + time
				+ " | " + location.getName()
				+ " | " + text;
	}

}