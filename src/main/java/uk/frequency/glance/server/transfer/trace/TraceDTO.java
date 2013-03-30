package uk.frequency.glance.server.transfer.trace;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import uk.frequency.glance.server.transfer.GenericDTO;

@JsonTypeInfo(use=Id.CLASS)
@SuppressWarnings("serial")
public abstract class TraceDTO extends GenericDTO {

	public long userId;
	
	public long time;

	@Override
	public String toString() {
		return super.toString()
				+ " | " + time;
	}
	
}
