package uk.frequency.glance.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import uk.frequency.glance.server.business.UserBL;
import uk.frequency.glance.server.business.exception.MissingFieldException;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.user.User;
import uk.frequency.glance.server.model.user.UserProfile;
import uk.frequency.glance.server.transfer.UserDTO;


@Path("/user")
public class UserSL extends GenericSL<User, UserDTO>{

	UserBL userBl;

	public UserSL() {
		super(new UserBL());
		userBl = (UserBL)business;
	}
	
	@Override
	protected UserDTO toDTO(User user){
		UserDTO dto = new UserDTO();
		initToDTO(user, dto);
		dto.setProfile(user.getProfileHistory().get(0)); //TODO get most recent profile
		
		List<Long> friendIds = new ArrayList<Long>();
		if(user.getFriends() != null){
			for(User friend : user.getFriends()){
				friendIds.add(friend.getId());
			}
			dto.setFriendsIds(friendIds);
		}
		
		if(user.getEvents() != null){
			List<Long> eventIds = new ArrayList<Long>();
			for(Event event : user.getEvents()){
				eventIds.add(event.getId());
			}
			dto.setEventsIds(eventIds);
		}
		
		return dto;
	}

	@Override
	protected User fromDTO(UserDTO dto) {
		User user = new User();
		initFromDTO(dto, user);

		if (dto.getProfile() != null) {
			List<UserProfile> profiles = new ArrayList<UserProfile>();
			profiles.add(dto.getProfile());
			user.setProfileHistory(profiles);
		} else {
			throw new MissingFieldException("profile");
		}

		return user;
	}
	
}