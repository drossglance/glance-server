package uk.frequency.glance.server.transfer.user;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import uk.frequency.glance.server.model.user.FriendshipStatus;
import uk.frequency.glance.server.model.user.UserProfile;
import uk.frequency.glance.server.transfer.GenericDTO;
import uk.frequency.glance.server.transfer.event.EventDTO;
import uk.frequency.glance.server.transfer.event.EventViewDTO;

@XmlRootElement
@SuppressWarnings("serial")
public class UserDTO extends GenericDTO{

	public String username;
	
	public String facebookId;
	
	public UserProfile profile;
	
	public List<Long> eventsIds;
	
	public List<EventDTO> events;
	
	public List<EventViewDTO> eventViews;
	
	public FriendshipStatus friendshipStatus;
	
	public int[] wavelineIndex;
	
	public String wavelineImageUrl;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public List<EventDTO> getEvents() {
		return events;
	}

	public void setEvents(List<EventDTO> events) {
		this.events = events;
	}

	public List<Long> getEventsIds() {
		return eventsIds;
	}

	public void setEventsIds(List<Long> eventsIds) {
		this.eventsIds = eventsIds;
	}

	public FriendshipStatus getFriendshipStatus() {
		return friendshipStatus;
	}

	public void setFriendshipStatus(FriendshipStatus friendshipStatus) {
		this.friendshipStatus = friendshipStatus;
	}

	public int[] getWavelineIndex() {
		return wavelineIndex;
	}

	public void setWavelineIndex(int[] wavelineIndex) {
		this.wavelineIndex = wavelineIndex;
	}

	public List<EventViewDTO> getEventViews() {
		return eventViews;
	}

	public void setEventViews(List<EventViewDTO> eventViews) {
		this.eventViews = eventViews;
	}
	
}
