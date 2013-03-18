package uk.frequency.glance.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import uk.frequency.glance.server.business.EventBL;
import uk.frequency.glance.server.business.UserBL;
import uk.frequency.glance.server.business.exception.MissingFieldException;
import uk.frequency.glance.server.model.component.Location;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.MoveEvent;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.user.Friendship;
import uk.frequency.glance.server.model.user.User;
import uk.frequency.glance.server.model.user.UserProfile;
import uk.frequency.glance.server.transfer.event.EventDTO;
import uk.frequency.glance.server.transfer.event.MoveEventDTO;
import uk.frequency.glance.server.transfer.event.StayEventDTO;
import uk.frequency.glance.server.transfer.user.FriendshipDTO;
import uk.frequency.glance.server.transfer.user.UserDTO;


@Path("/user")
public class UserSL extends GenericSL<User, UserDTO>{

	UserBL userBl;
	EventBL eventBl;

	public UserSL() {
		super(new UserBL());
		userBl = (UserBL)business;
		eventBl = new EventBL();
	}
	
	@Override
	@POST
	@Consumes("application/json")
	public Response create(UserDTO dto) {
		if(dto.getUsername() == null && dto.getFacebookId() == null){
			throw new MissingFieldException("username or facebookId");
		}
		return super.create(dto);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/addFriendsPage")
	public List<UserDTO> buildAddFriendsPage(
			@PathParam("id") long userId){
		List<User> entities = userBl.findAll();
		List<UserDTO> dtoList = new ArrayList<UserDTO>();
		List<Friendship> friendsips = userBl.findFriendships(userId);
		int cur = 0;
		for(User user : entities){
			long id = user.getId();
			if(id == userId) continue;
			
			UserDTO dto = new UserDTO();
			dto.setId(user.getId());
			
			if(!user.getProfileHistory().isEmpty()){
				UserProfile recentProfile = user.getProfileHistory().get(0); 
				UserProfile profile = new UserProfile();
				profile.setFirstName(recentProfile.getFirstName());
				profile.setMiddleName(recentProfile.getMiddleName());
				profile.setFullName(recentProfile.getFullName());
				profile.setImageUrl(recentProfile.getImageUrl());
				dto.setProfile(profile);
			}
			
			Friendship friendship = friendsips.get(cur);
			if (friendsips.size() > cur && id == friendship.getFriend().getId()) { //assumes friendships is ordered by friends' ids
				cur++;
				dto.setFriendshipStatus(friendship.getStatus());
			}
			
			dtoList.add(dto);
		}
		business.flush();
		return dtoList;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/glancePage")
	public List<UserDTO> buildGlancePage(
			@PathParam("id") long userId){
		List<User> friends = userBl.findFriends(userId);
		List<UserDTO> dtoList = new ArrayList<UserDTO>();
		for(User friend : friends){
			Event recentEvent = eventBl.findMostRecent(friend.getId());
			
			UserDTO dto = new UserDTO();
			dto.setId(friend.getId());
			
			if(!friend.getProfileHistory().isEmpty()){
				UserProfile recentProfile = friend.getProfileHistory().get(0); 
				UserProfile profile = new UserProfile();
				profile.setFirstName(recentProfile.getFirstName());
				profile.setMiddleName(recentProfile.getMiddleName());
				profile.setFullName(recentProfile.getFullName());
				profile.setImageUrl(recentProfile.getImageUrl());
				dto.setProfile(profile);
			}
			
			if(recentEvent != null){
				EventDTO eventDto;
				if(recentEvent instanceof StayEvent){
					StayEvent stay = (StayEvent) recentEvent;
					StayEventDTO stayDto = new StayEventDTO();
					stayDto.setStartTime(stay.getStartTime().getTime());
					Location location = new Location();
					location.setName(stay.getLocation().getName());
					stayDto.setLocation(location);
					eventDto = stayDto;
				}else if(recentEvent instanceof MoveEvent){
					MoveEvent move = (MoveEvent) recentEvent;
					MoveEventDTO moveDto = new MoveEventDTO();
					moveDto.setStartTime(move.getStartTime().getTime());
					Location location = new Location();
					location.setName(move.getStartLocation().getName());
					moveDto.setStartLocation(location);
					eventDto = moveDto;
				}else{
					throw new AssertionError();
				}
				List<EventDTO> events = new ArrayList<EventDTO>();
				events.add(eventDto);
				dto.setEvents(events);
			}
			
			dto.setWavelinePreviewUrl("http://aconcepti.com/images/stream_graph.png"); //TODO replace this dummy image w/ actual calculated waveline
			
			dtoList.add(dto);
		}
		business.flush();
		return dtoList;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/friendship/full")
	public List<FriendshipDTO> findFriendships(
			@PathParam("id") long userId){
		List<Friendship> entities = userBl.findFriendships(userId);
		List<FriendshipDTO> dto = listToDTO(entities);
		business.flush();
		return dto;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/friendship/received")
	public List<UserDTO> findFriendshipRequests(
			@PathParam("id") long userId){
		List<User> entities = userBl.findFriendshipRequestsReceived(userId);
		List<UserDTO> dtoList = new ArrayList<UserDTO>();

		for(User user : entities){
			long id = user.getId();
			if(id == userId) continue;
			
			UserDTO dto = new UserDTO();
			dto.setId(user.getId());
			
			if(!user.getProfileHistory().isEmpty()){
				UserProfile recentProfile = user.getProfileHistory().get(0); 
				UserProfile profile = new UserProfile();
				profile.setFirstName(recentProfile.getFirstName());
				profile.setMiddleName(recentProfile.getMiddleName());
				profile.setFullName(recentProfile.getFullName());
				profile.setImageUrl(recentProfile.getImageUrl());
				dto.setProfile(profile);
			}

			dtoList.add(dto);
		}
		business.flush();
		return dtoList;
		
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/friendship/request-{friend}")
	public FriendshipDTO requestFriendship(
			@PathParam("id") long userId,
			@PathParam("friend") long friendId){
		Friendship entity = userBl.createFriendshipRequest(userId, friendId);
		FriendshipDTO dto = toDTO(entity);
		business.flush();
		return dto;
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/friendship/accept-{friend}")
	public FriendshipDTO acceptFriendship(
			@PathParam("id") long userId,
			@PathParam("friend") long friendId){
		Friendship entity = userBl.acceptFriendshipRequest(userId, friendId);
		FriendshipDTO dto = toDTO(entity);
		business.flush();
		return dto;
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/friendship/deny-{friend}")
	public FriendshipDTO denyFriendship(
			@PathParam("id") long userId,
			@PathParam("friend") long friendId){
		Friendship entity = userBl.declineFriendshipRequest(userId, friendId);
		FriendshipDTO dto = toDTO(entity);
		business.flush();
		return dto;
	}
	
	@DELETE
	@Path("/{id}/friendship/{friend}")
	public Response removeFriendship(
			@PathParam("id") long userId,
			@PathParam("friend") long friendId){
		userBl.removeFriendship(userId, friendId);
		business.flush();
		return Response.status(Status.NO_CONTENT).build();
	}
	
	@Override
	protected UserDTO toDTO(User user){
		UserDTO dto = new UserDTO();
		initToDTO(user, dto);
		
		dto.setUsername(user.getUsername());
		dto.setFacebookId(user.getFacebookId());
		
		if(user.getProfileHistory() != null && !user.getProfileHistory().isEmpty()){
			dto.setProfile(user.getProfileHistory().get(0)); //TODO get most recent profile
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

		user.setUsername(dto.getUsername());
		user.setFacebookId(dto.getFacebookId());
		
		if (dto.getProfile() != null) {
			List<UserProfile> profiles = new ArrayList<UserProfile>();
			profiles.add(dto.getProfile());
			user.setProfileHistory(profiles);
		}

		return user;
	}
	
	protected FriendshipDTO toDTO(Friendship friendship){
		FriendshipDTO dto = new FriendshipDTO();
		initToDTO(friendship, dto);
		
		dto.setUserId(friendship.getUser().getId());
		dto.setFriendId(friendship.getFriend().getId());
		dto.setStatus(friendship.getStatus());
		
		return dto;
	}
	
	protected List<FriendshipDTO> listToDTO(List<Friendship> list){
		List<FriendshipDTO> dto = new ArrayList<FriendshipDTO>();
		for(Friendship entity : list){
			dto.add(toDTO(entity));
		}
		return dto;
	}
	
}