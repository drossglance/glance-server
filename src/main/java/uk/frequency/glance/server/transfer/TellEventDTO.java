package uk.frequency.glance.server.transfer;

import uk.frequency.glance.server.model.Location;

@SuppressWarnings("serial")
public class TellEventDTO extends EventDTO{

	long time;
	
	Location location;
	
	String text;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
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
	
}
