package uk.frequency.glance.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.ObjectNotFoundException;

import uk.frequency.glance.server.business.EventBL;
import uk.frequency.glance.server.data_access.util.HibernateUtil;
import uk.frequency.glance.server.model.Comment;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.ListenEvent;
import uk.frequency.glance.server.model.event.MoveEvent;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.event.TellEvent;
import uk.frequency.glance.server.model.user.User;
import uk.frequency.glance.server.transfer.event.EventDTO;
import uk.frequency.glance.server.transfer.event.ListenEventDTO;
import uk.frequency.glance.server.transfer.event.MoveEventDTO;
import uk.frequency.glance.server.transfer.event.StayEventDTO;
import uk.frequency.glance.server.transfer.event.TellEventDTO;


@Path("/event")
public class EventSL extends GenericSL<Event, EventDTO>{

	EventBL eventBl;

	public EventSL() {
		super(new EventBL());
		eventBl = (EventBL)business;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user-{id}")
	public Response findByAuthor(@PathParam("id") long userId) {
		try {
			List<Event> list = eventBl.findByUser(userId);
			List<EventDTO> dto = toDTO(list);
			eventBl.flush();
			return Response.ok(dto).build();
		} catch (ObjectNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user-{id}/{start}to{end}")
	public List<EventDTO> findByUserAndTimeRange(
			@PathParam("id") long userId,
			@PathParam("start") long start,
			@PathParam("end") long end) {
		List<Event> list = eventBl.findByTimeRange(userId, new Date(start), new Date(end)); 
		List<EventDTO> dto = toDTO(list);
		eventBl.flush();
		return dto;
	}
	
	@GET
	@Path("/user-{id}/created_after-{time}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<EventDTO> findCreatedAfter(
			@PathParam("id") long userId,
			@PathParam("time") long time) {
		try{
			List<Event> entity = eventBl.findCreatedAfter(userId, new Date(time));
			List<EventDTO> dto = toDTO(entity);
			eventBl.flush();
			return dto;
		}catch(ObjectNotFoundException e){
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	@Override
	protected EventDTO toDTO(Event event){
		
		EventDTO dto;
		if(event instanceof StayEvent){
			StayEvent stay = (StayEvent)event;
			StayEventDTO stayDto = new StayEventDTO();
			stayDto.setLocation(stay.getLocation());
			dto = stayDto;
		}else if(event instanceof MoveEvent){
			MoveEvent move = (MoveEvent)event;
			MoveEventDTO moveDto = new MoveEventDTO();
			moveDto.setStartLocation(move.getStartLocation());
			moveDto.setEndLocation(move.getEndLocation());
			moveDto.setTrail(HibernateUtil.initializeAndUnproxy(move.getTrail()));
			dto = moveDto;
		}else if(event instanceof TellEvent){
			TellEvent tell = (TellEvent)event;
			TellEventDTO tellDto = new TellEventDTO();
			tellDto.setLocation(tell.getLocation());
			tellDto.setText(tell.getText());
			dto = tellDto;
		}else if(event instanceof ListenEvent){
			ListenEvent listen = (ListenEvent)event;
			ListenEventDTO listenDto = new ListenEventDTO();
			listenDto.setSongMetadata(listen.getSongMetadata());
			dto = listenDto;
		}else{
			throw new AssertionError();
		}
		
		initToDTO(event, dto);
		dto.setUserId(event.getUser().getId());
		dto.setType(event.getType());
		dto.setStartTime(event.getStartTime().getTime());
		if(event.getEndTime() != null){
			dto.setEndTime(event.getEndTime().getTime());
		}
		dto.setScore(event.getScore());
		dto.setMedia(event.getMedia());
		
		if(event.getParticipants() != null){
			List<Long> participantIds = new ArrayList<Long>();
			for(User participant : event.getParticipants()){
				participantIds.add(participant.getId());
			}
			dto.setParticipantIds(participantIds);
		}
		
		if(event.getComments() != null){
			List<Long> commentIds = new ArrayList<Long>();
			for(Comment comment : event.getComments()){
				commentIds.add(comment.getId());
			}
			dto.setCommentIds(commentIds);
		}
		
		return dto;
	}
	
	@Override
	protected Event fromDTO(EventDTO dto) {

		User user = new User();
		user.setId(dto.getUserId());
		
		Event event;
		if(dto instanceof StayEventDTO){
			StayEventDTO stayDto = (StayEventDTO)dto;
			StayEvent stay = new StayEvent();
			stay.setLocation(stayDto.getLocation());
			event = stay;
		}else if(dto instanceof MoveEventDTO){
			MoveEventDTO moveDto = (MoveEventDTO)dto;
			MoveEvent move = new MoveEvent();
			move.setStartLocation(moveDto.getStartLocation());
			move.setEndLocation(moveDto.getEndLocation());
			move.setTrail(moveDto.getTrail());
			event = move;
		}else if(dto instanceof TellEventDTO){
			TellEventDTO tellDto = (TellEventDTO)dto;
			TellEvent tell = new TellEvent();
			tell.setLocation(tellDto.getLocation());
			tell.setText(tellDto.getText());
			event = tell;
		}else if(dto instanceof ListenEventDTO){
			ListenEventDTO listenDto = (ListenEventDTO)dto;
			ListenEvent listen = new ListenEvent();
			listen.setSongMetadata(listenDto.getSongMetadata());
			event = listen;
		}else{
			throw new AssertionError();
		}
		
		initFromDTO(dto, event);
		event.setUser(user);
		event.setType(dto.getType());
		event.setStartTime(new Date(dto.getStartTime()));
		if(dto.getEndTime() != 0){
			event.setEndTime(new Date(dto.getEndTime()));
		}
		event.setMedia(dto.getMedia());
		event.setScore(dto.getScore());
		
		if(dto.getParticipantIds() != null){
			List<User> participants = new ArrayList<User>();
			for(Long id : dto.getParticipantIds()){
				User participant = new User();
				participant.setId(id);
				participants.add(participant);
			}
			event.setParticipants(participants);
		}
		
		if(dto.getCommentIds() != null){
			List<Comment> comments = new ArrayList<Comment>();
			for(Long id : dto.getCommentIds()){
				Comment comment = new Comment();
				comment.setId(id);
				comments.add(comment);
			}
			event.setComments(comments);
		}
		
		return event;
	}
	
}