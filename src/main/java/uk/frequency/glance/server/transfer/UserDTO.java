package uk.frequency.glance.server.transfer;

import java.util.List;

import uk.frequency.glance.server.model.UserProfile;

@SuppressWarnings("serial")
public class UserDTO extends GenericDTO{

	UserProfile profile;
	
	List<Long> friendsIds;

	List<Long> eventsIds;


	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public List<Long> getFriendsIds() {
		return friendsIds;
	}

	public void setFriendsIds(List<Long> friendsIds) {
		this.friendsIds = friendsIds;
	}

	public List<Long> getEventsIds() {
		return eventsIds;
	}

	public void setEventsIds(List<Long> eventsIds) {
		this.eventsIds = eventsIds;
	}
	
}
