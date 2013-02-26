package uk.frequency.glance.server.transfer;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class GenericDTO implements Serializable {
	
	long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
