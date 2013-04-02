package uk.frequency.glance.server.service;

import static uk.frequency.glance.server.model.user.FriendshipStatus.ACCEPTED;
import static uk.frequency.glance.server.model.user.FriendshipStatus.DECLINED;
import static uk.frequency.glance.server.model.user.FriendshipStatus.REQUEST_RECEIVED;
import static uk.frequency.glance.server.model.user.FriendshipStatus.REQUEST_SENT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import uk.frequency.glance.server.business.EventBL;
import uk.frequency.glance.server.business.UserBL;
import uk.frequency.glance.server.business.exception.MissingFieldException;
import uk.frequency.glance.server.debug.LogEntry;
import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.MoveEvent;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.user.Friendship;
import uk.frequency.glance.server.model.user.FriendshipStatus;
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
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(UserDTO dto) {
		if(dto.username == null && dto.facebookId == null){
			throw new MissingFieldException("username or facebookId");
		}
		return super.create(dto);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/facebookLogin")
	public UserDTO facebookLogin(
			@QueryParam("fbId") String facebookId,
			@QueryParam("token") String accessToken){
		User user = userBl.facebookLogin(facebookId, accessToken);
		UserDTO dto = toDTO(user);
		business.flush();
		return dto;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/glancePage")
	public List<UserDTO> buildGlancePage(
			@PathParam("id") long userId){
		List<UserDTO> dtoList = new ArrayList<UserDTO>();
		
		List<User> requestsReceived = userBl.findFriendshipRequestsReceived(userId);
		for (User friend : requestsReceived) {

			UserDTO dto = new UserDTO();
			dto.id = friend.getId();
			if (!friend.getProfileHistory().isEmpty()) {
				UserProfile recentProfile = friend.getProfileHistory().get(0);
				UserProfile profile = new UserProfile();
				profile.setFirstName(recentProfile.getFirstName());
				profile.setMiddleName(recentProfile.getMiddleName());
				profile.setFullName(recentProfile.getFullName());
				profile.setImageUrl(recentProfile.getImageUrl());
				dto.profile = profile;
			}

			dto.friendshipStatus = REQUEST_RECEIVED;
			dtoList.add(dto);
		}
		
		List<User> friends = userBl.findFriends(userId);
		for(User friend : friends){
			
			UserDTO dto = new UserDTO();
			dto.id = friend.getId();
			if(!friend.getProfileHistory().isEmpty()){
				UserProfile recentProfile = friend.getProfileHistory().get(0); 
				UserProfile profile = new UserProfile();
				profile.setFirstName(recentProfile.getFirstName());
				profile.setMiddleName(recentProfile.getMiddleName());
				profile.setFullName(recentProfile.getFullName());
				profile.setImageUrl(recentProfile.getImageUrl());
				dto.profile = profile;
			}
			
			Event recentEvent = eventBl.findMostRecent(friend.getId());
			if(recentEvent != null){
				EventDTO eventDto;
				if(recentEvent instanceof StayEvent){
					StayEvent stay = (StayEvent) recentEvent;
					StayEventDTO stayDto = new StayEventDTO();
					stayDto.startTime = stay.getStartTime().getTime();
					Location location = new Location();
					location.setName(stay.getLocation().getName());
					stayDto.location = location;
					eventDto = stayDto;
				}else if(recentEvent instanceof MoveEvent){
					MoveEvent move = (MoveEvent) recentEvent;
					MoveEventDTO moveDto = new MoveEventDTO();
					moveDto.startTime = move.getStartTime().getTime();
					Location location = new Location();
					location.setName(move.getStartLocation().getName());
					moveDto.startLocation = location;
					eventDto = moveDto;
				}else{
					throw new AssertionError();
				}
				List<EventDTO> events = new ArrayList<EventDTO>();
				events.add(eventDto);
				dto.events = events;
			}
			
			dto.friendshipStatus = ACCEPTED;
			dtoList.add(dto);
		}
		
		business.flush();
		return dtoList;
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
			dto.id = id;
			
			if(friendsips.size() > cur){
				Friendship friendship = friendsips.get(cur);
				if (id == friendship.getFriend().getId()) { //assumes friendships is ordered by friends' ids
					cur++;
					dto.friendshipStatus = friendship.getStatus();
				}
			}
			
			FriendshipStatus status = dto.friendshipStatus;
			if(status == null || status == ACCEPTED || status == REQUEST_SENT || status == DECLINED) {
				
				if(!user.getProfileHistory().isEmpty()){
					UserProfile recentProfile = user.getProfileHistory().get(0); 
					UserProfile profile = new UserProfile();
					profile.setFirstName(recentProfile.getFirstName());
					profile.setMiddleName(recentProfile.getMiddleName());
					profile.setFullName(recentProfile.getFullName());
					profile.setImageUrl(recentProfile.getImageUrl());
					dto.profile = profile;
				}
			
				dtoList.add(dto);
			}
		}
		business.flush();
		return dtoList;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/friendship")
	public List<FriendshipDTO> findFriendships(
			@PathParam("id") long userId){
		List<Friendship> entities = userBl.findFriendships(userId);
		List<FriendshipDTO> dto = FriendshipDTO.from(entities);
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
			dto.id = user.getId();
			
			if(!user.getProfileHistory().isEmpty()){
				UserProfile recentProfile = user.getProfileHistory().get(0); 
				UserProfile profile = new UserProfile();
				profile.setFirstName(recentProfile.getFirstName());
				profile.setMiddleName(recentProfile.getMiddleName());
				profile.setFullName(recentProfile.getFullName());
				profile.setImageUrl(recentProfile.getImageUrl());
				dto.profile = profile;
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
		FriendshipDTO dto = FriendshipDTO.from(entity);
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
		FriendshipDTO dto = FriendshipDTO.from(entity);
		business.flush();
		return dto;
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/friendship/decline-{friend}")
	public FriendshipDTO declineFriendship(
			@PathParam("id") long userId,
			@PathParam("friend") long friendId){
		Friendship entity = userBl.declineFriendshipRequest(userId, friendId);
		FriendshipDTO dto = FriendshipDTO.from(entity);
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
	
	@POST
	@Path("/log")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveLogEntry(LogEntry debug) {
		try{
			userBl.saveLogEntry(debug);
			return Response.ok().build();
		}catch(Exception e){
			throw new WebApplicationException(e);
		}
	}
	
	@GET
	@Path("/log")
	@Produces(MediaType.APPLICATION_JSON)
	public List<LogEntry> findLogEntries(
			@QueryParam("after") Long start) {
		try{
			if(start != null){
				return userBl.findAllLogEntriesAfter(new Date(start));
			}else{
				return userBl.findAllLogEntries();
			}
		}catch(Exception e){
			throw new WebApplicationException(e);
		}
	}
	
	@GET
	@Path("{id}/log")
	@Produces(MediaType.APPLICATION_JSON)
	public List<LogEntry> findLogEntries(
			@PathParam("id") long userId,
			@QueryParam("after") Long start) {
		try{
			if(start != null){
				return userBl.findLogEntriesAfter(userId, new Date(start));
			}else{
				return userBl.findLogEntries(userId);
			}
		}catch(Exception e){
			throw new WebApplicationException(e);
		}
	}
	
	@Override
	protected UserDTO toDTO(User user){
		UserDTO dto = new UserDTO();
		dto.initFromEntity(user);
		
		dto.username = user.getUsername();
		dto.facebookId = user.getFacebookId();
		
		if(user.getProfileHistory() != null && !user.getProfileHistory().isEmpty()){
			dto.profile = user.getProfileHistory().get(0); //TODO get most recent profile
		}
		
		return dto;
	}

	@Override
	protected User fromDTO(UserDTO dto) {
		User user = new User();
		dto.initEntity(user);

		user.setUsername(dto.username);
		user.setFacebookId(dto.facebookId);
		
		if (dto.profile != null) {
			List<UserProfile> profiles = new ArrayList<UserProfile>();
			profiles.add(dto.profile);
			user.setProfileHistory(profiles);
		}

		return user;
	}
	
}