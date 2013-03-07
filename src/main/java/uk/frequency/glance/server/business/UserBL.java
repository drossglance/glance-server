package uk.frequency.glance.server.business;

import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;

import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.model.user.EventGenerationInfo;
import uk.frequency.glance.server.model.user.User;

public class UserBL extends GenericBL<User>{

	UserDAL userDal;

	public UserBL() {
		super(new UserDAL());
		userDal = (UserDAL)dal;
	}
	
	@Override
	public User create(User user) throws ConstraintViolationException, TransientObjectException {
		user = super.create(user);
		if(user.getEventGenerationInfo() == null){
			EventGenerationInfo gen = new EventGenerationInfo();
			gen.setUser(user);
			userDal.makePersistent(gen);
			user.setEventGenerationInfo(gen);
		}
		return user;
	}
	
}
