package uk.frequency.glance.server.transfer.event;

import javax.xml.bind.annotation.XmlRootElement;

import uk.frequency.glance.server.model.component.Location;

@XmlRootElement
@SuppressWarnings("serial")
public class TellEventDTO extends EventDTO{

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
	
}
