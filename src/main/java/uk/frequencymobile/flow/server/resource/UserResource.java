package uk.frequencymobile.flow.server.resource;

import javax.ws.rs.Path;

import uk.frequencymobile.flow.server.data.UserDAO;
import uk.frequencymobile.flow.server.model.User;


@Path("/user")
public class UserResource extends GenericResource<User>{

	UserDAO userDao = new UserDAO();

	public UserResource() {
		super(new UserDAO());
		userDao = (UserDAO)dao;
	}
	
}