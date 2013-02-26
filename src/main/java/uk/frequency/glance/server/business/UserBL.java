package uk.frequency.glance.server.business;

import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.model.user.User;

public class UserBL extends GenericBL<User>{

	UserDAL userDal;

	public UserBL() {
		super(new UserDAL());
		userDal = (UserDAL)dal;
	}
	
}
