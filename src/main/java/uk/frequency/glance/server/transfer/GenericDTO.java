package uk.frequency.glance.server.transfer;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public abstract class GenericDTO implements Serializable {
	
	long id;

	Date creationTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
}
