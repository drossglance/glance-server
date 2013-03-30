package uk.frequency.glance.server.transfer.event;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.component.Position;

@XmlRootElement
@SuppressWarnings("serial")
public class MoveEventDTO extends EventDTO{

	public Location startLocation;
	
	public Location endLocation;
	
	public List<Position> trail;

}
