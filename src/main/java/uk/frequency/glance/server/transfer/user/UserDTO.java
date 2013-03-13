package uk.frequency.glance.server.transfer.user;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import uk.frequency.glance.server.model.user.UserProfile;
import uk.frequency.glance.server.transfer.GenericDTO;
import uk.frequency.glance.server.transfer.event.EventDTO;

@XmlRootElement
@SuppressWarnings("serial")
public class UserDTO extends GenericDTO{

	String username;
	
	String facebookId;
	
	UserProfile profile;
	
	List<Long> eventsIds;
	
	List<EventDTO> events;
	
	Boolean isMyFriend;
	
	String wavelinePreviewUrl;

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

	public Boolean isMyFriend() {
		return isMyFriend;
	}

	public void setMyFriend(Boolean isMyFriend) {
		this.isMyFriend = isMyFriend;
	}

	public String getWavelinePreviewUrl() {
		return wavelinePreviewUrl;
	}

	public void setWavelinePreviewUrl(String wavelinePreviewUrl) {
		this.wavelinePreviewUrl = wavelinePreviewUrl;
	}
	
}
