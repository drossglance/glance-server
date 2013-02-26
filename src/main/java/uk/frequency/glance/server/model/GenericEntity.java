package uk.frequency.glance.server.model;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class GenericEntity {

	@Id
	@GeneratedValue //TODO create different generators for each hierarchy
	protected long id;
	
	protected Date creationTime;
	
	protected boolean deleted;

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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() 
				+ ": " + id;
	}
	
}
