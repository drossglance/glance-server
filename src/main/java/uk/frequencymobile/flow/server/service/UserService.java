package uk.frequencymobile.flow.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import uk.frequencymobile.flow.server.data.UserDAO;
import uk.frequencymobile.flow.server.model.Event;
import uk.frequencymobile.flow.server.model.User;
import uk.frequencymobile.flow.server.model.UserProfile;
import uk.frequencymobile.flow.server.transfer.UserDTO;


@Path("/user")
public class UserService extends GenericService<User, UserDTO>{

	UserDAO userDao = new UserDAO();

	public UserService() {
		super(new UserDAO());
		userDao = (UserDAO)dao;
	}
	
	@Override
	protected UserDTO toDTO(User user){
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setProfile(user.getProfileHistory().get(0)); //TODO get most recent profile
		
		List<Long> friendIds = new ArrayList<Long>();
		for(User friend : user.getFriends()){
			friendIds.add(friend.getId());
		}
		dto.setFriendsIds(friendIds);
		
		List<Long> eventIds = new ArrayList<Long>();
		for(Event event : user.getEvents()){
			eventIds.add(event.getId());
		}
		dto.setEventsIds(eventIds);
		
		return dto;
	}

	@Override
	protected User fromDTO(UserDTO dto) {

		User user = new User();
		List<UserProfile> profiles = new ArrayList<UserProfile>();
		profiles.add(dto.getProfile());
		user.setProfileHistory(profiles);
		
		return user;
	}
	
}