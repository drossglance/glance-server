package uk.frequencymobile.flow.server.service;

import javax.ws.rs.Path;

import uk.frequencymobile.flow.server.data.UserDAO;
import uk.frequencymobile.flow.server.model.User;


@Path("/user")
public class UserService extends GenericService<User>{

	UserDAO userDao = new UserDAO();

	public UserService() {
		super(new UserDAO());
		userDao = (UserDAO)dao;
	}
	
}