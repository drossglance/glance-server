package uk.frequency.glance.server.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.ObjectNotFoundException;

import uk.frequency.glance.server.business.EventBL;
import uk.frequency.glance.server.model.Comment;
import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.User;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.MoveEvent;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.event.TellEvent;
import uk.frequency.glance.server.transfer.EventDTO;
import uk.frequency.glance.server.transfer.MoveEventDTO;
import uk.frequency.glance.server.transfer.StayEventDTO;
import uk.frequency.glance.server.transfer.TellEventDTO;


@Path("/event")
public class EventSL extends GenericSL<Event, EventDTO>{

	EventBL eventBl;

	public EventSL() {
		super(new EventBL());
		eventBl = (EventBL)business;
	}
	
	@GET
	@Path("/user-{id}")
	public Response findByAuthor(@PathParam("id") long userId) {
		try {
			List<Event> list = eventBl.findByAuthor(userId);
			return Response.ok(toDTO(list)).build();
		} catch (ObjectNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
	}
	
	@PUT
	@Path("/user-{id}/auto-generated")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response generateEvent(Location location, 
			@PathParam("id") long userId){
		/*DEBUG*/System.out.println("Request received. user=" + userId + ", position=" + location.getPosition());
		long id = eventBl.generateEvent(location, userId);
		URI uri = uriInfo.getBaseUriBuilder().path(""+id).build(); //TODO missing the enitity type in path
		return Response.created(uri).build();
	}
	
	@Override
	protected EventDTO toDTO(Event event){
		
		EventDTO dto;
		if(event instanceof StayEvent){
			StayEvent stay = (StayEvent)event;
			StayEventDTO stayDto = new StayEventDTO();
			stayDto.setStartTime(stay.getStartTime().getTime());
			stayDto.setEndTime(stay.getEndTime().getTime());
			stayDto.setLocation(stay.getLocation());
			dto = stayDto;
		}else if(event instanceof MoveEvent){
			MoveEvent move = (MoveEvent)event;
			MoveEventDTO moveDto = new MoveEventDTO();
			moveDto.setStartTime(move.getStartTime().getTime());
			moveDto.setEndTime(move.getEndTime().getTime());
			moveDto.setStartLocation(move.getStartLocation());
			moveDto.setEndLocation(move.getEndLocation());
			moveDto.setTrail(move.getTrail());
			dto = moveDto;
		}else if(event instanceof TellEvent){
			TellEvent tell = (TellEvent)event;
			TellEventDTO tellDto = new TellEventDTO();
			tellDto.setTime(tell.getTime().getTime());
			tellDto.setLocation(tell.getLocation());
			tellDto.setText(tell.getText());
			dto = tellDto;
		}else{
			throw new AssertionError();
		}
		
		dto.setId(event.getId());
		dto.setCreationTime(event.getCreationTime().getTime());
		dto.setAuthorId(event.getAuthor().getId());
		dto.setType(event.getType());
		dto.setScore(event.getScore());
		dto.setMedia(event.getMedia());
		dto.setActions(event.getActions());
		dto.setFeelings(event.getFeelings());
		
		List<Long> participantIds = new ArrayList<Long>();
		for(User participant : event.getParticipants()){
			participantIds.add(participant.getId());
		}
		dto.setParticipantIds(participantIds);
		
		List<Long> commentIds = new ArrayList<Long>();
		for(Comment comment : event.getComments()){
			commentIds.add(comment.getId());
		}
		dto.setCommentIds(commentIds);
		
		return dto;
	}
	
	@Override
	protected Event fromDTO(EventDTO dto) {

		User user = new User();
		user.setId(dto.getAuthorId());
		
		Event event;
		if(dto instanceof StayEventDTO){
			StayEventDTO stayDto = (StayEventDTO)dto;
			StayEvent stay = new StayEvent();
			stay.setStartTime(new Date(stayDto.getStartTime()));
			stay.setEndTime(new Date(stayDto.getEndTime()));
			stay.setLocation(stayDto.getLocation());
			event = stay;
		}else if(dto instanceof MoveEventDTO){
			MoveEventDTO moveDto = (MoveEventDTO)dto;
			MoveEvent move = new MoveEvent();
			move.setStartTime(new Date(moveDto.getStartTime()));
			move.setEndTime(new Date(moveDto.getEndTime()));
			move.setStartLocation(moveDto.getStartLocation());
			move.setEndLocation(moveDto.getEndLocation());
			move.setTrail(moveDto.getTrail());
			event = move;
		}else if(dto instanceof TellEventDTO){
			TellEventDTO tellDto = (TellEventDTO)dto;
			TellEvent tell = new TellEvent();
			tell.setTime(new Date(tellDto.getTime()));
			tell.setLocation(tellDto.getLocation());
			tell.setText(tellDto.getText());
			event = tell;
		}else{
			throw new AssertionError();
		}
		
		event.setId(dto.getId());
		event.setCreationTime(new Date(dto.getCreationTime()));
		event.setAuthor(user);
		event.setMedia(dto.getMedia());
		event.setType(dto.getType());
		event.setScore(dto.getScore());
		event.setMedia(dto.getMedia());
		event.setActions(dto.getActions());
		event.setFeelings(dto.getFeelings());
		
		List<User> participants = new ArrayList<User>();
		for(Long id : dto.getParticipantIds()){
			User participant = new User();
			participant.setId(id);
			participants.add(participant);
		}
		event.setParticipants(participants);
		
		List<Comment> comments = new ArrayList<Comment>();
		for(Long id : dto.getCommentIds()){
			Comment comment = new Comment();
			comment.setId(id);
			comments.add(comment);
		}
		event.setComments(comments);
		
		return event;
	}
	
}