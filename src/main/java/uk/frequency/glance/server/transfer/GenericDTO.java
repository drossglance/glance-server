package uk.frequency.glance.server.transfer;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class GenericDTO implements Serializable {

	long id;

	protected long creationTime;

	protected long updateTime;
	
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

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}
