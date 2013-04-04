package uk.frequency.glance.server.transfer.trace;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@SuppressWarnings("serial")
public class SleepTraceDTO extends TraceDTO {

	public boolean begin; //false means is end

}
