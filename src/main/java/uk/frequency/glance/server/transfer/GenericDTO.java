package uk.frequency.glance.server.transfer;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class GenericDTO implements Serializable {
	
	long id;

	long creationTime;//TODO sort out Date convertion to JSON between Jersey and Gson

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	
}
