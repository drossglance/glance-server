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
	
	public String recentLocationName;
	
	public String recentLocationTime;

}