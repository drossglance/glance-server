package uk.frequency.glance.server.transfer.trace;

import javax.xml.bind.annotation.XmlRootElement;

import uk.frequency.glance.server.model.component.Position;


@XmlRootElement
@SuppressWarnings("serial")
public class SleepTraceDTO extends TraceDTO {

	public boolean begin; //false means is end
	
	public Position position;

}
