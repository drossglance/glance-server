package uk.frequency.glance.server.transfer.event;

import javax.xml.bind.annotation.XmlRootElement;

import uk.frequency.glance.server.model.component.Location;

@XmlRootElement
@SuppressWarnings("serial")
public class StayEventDTO extends EventDTO{

	Location location;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
