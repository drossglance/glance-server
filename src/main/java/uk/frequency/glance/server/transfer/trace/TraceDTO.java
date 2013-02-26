package uk.frequency.glance.server.transfer.trace;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import uk.frequency.glance.server.transfer.GenericDTO;

@JsonTypeInfo(use=Id.CLASS)
@SuppressWarnings("serial")
public abstract class TraceDTO extends GenericDTO {

	long userId;
	
	long time;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + time;
	}
	
}
