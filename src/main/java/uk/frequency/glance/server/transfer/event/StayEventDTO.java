package uk.frequency.glance.server.transfer.event;

import javax.xml.bind.annotation.XmlRootElement;

import uk.frequency.glance.server.model.Location;

@XmlRootElement
@SuppressWarnings("serial")
public class StayEventDTO extends EventDTO{

	public Location location;

}
