package uk.frequency.glance.server.transfer.trace;

import javax.xml.bind.annotation.XmlRootElement;

import uk.frequency.glance.server.model.component.Position;

@XmlRootElement
@SuppressWarnings("serial")
public class PositionTraceDTO extends TraceDTO {

	public Position position;
	
	public double speed;

}
