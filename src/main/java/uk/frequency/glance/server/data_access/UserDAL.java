package uk.frequency.glance.server.data_access;

import uk.frequency.glance.server.model.user.EventGenerationInfo;
import uk.frequency.glance.server.model.user.User;


public class UserDAL extends GenericDAL<User>{
	
	public EventGenerationInfo makePersistent(EventGenerationInfo entity) {
    	getSession().saveOrUpdate(entity);
        return entity;
    }
	
}
