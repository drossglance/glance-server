package uk.frequency.glance.server.transfer;

import java.io.Serializable;

import uk.frequency.glance.server.model.GenericEntity;

@SuppressWarnings("serial")
public abstract class GenericDTO implements Serializable {

	public Long id;

	public Long creationTime;

	public Long updateTime;

	public void initFromEntity(GenericEntity entity){
		id = entity.getId();
		creationTime = entity.getCreationTime().getTime();
		updateTime = entity.getUpdateTime().getTime();
	}
	
	public void initEntity(GenericEntity entity){
		entity.setId(id);
	}
	
}
