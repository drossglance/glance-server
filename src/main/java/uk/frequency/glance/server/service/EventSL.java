package uk.frequency.glance.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import uk.frequency.glance.server.business.EventBL;
import uk.frequency.glance.server.model.Comment;
import uk.frequency.glance.server.model.Event;
import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.User;
import uk.frequency.glance.server.transfer.EventDTO;


@Path("/event")
public class EventSL extends GenericSL<Event, EventDTO>{

	EventBL eventBl;

	public EventSL() {
		super(new EventBL());
		eventBl = (EventBL)business;
	}
	
	@GET
	@Path("/user-{id}")
	public List<EventDTO> findByAuthor(@PathParam("id") long userId) {
		List<Event> list = eventBl.findByAuthor(userId);
		return toDTO(list);
	}
	
	@PUT
	@Path("/user-{id}/auto-generated")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response generateEvent(Location location, 
			@PathParam("id") long userId){
		/*DEBUG*/System.out.println("Request received. user=" + userId + ", lat=" + location.getLat() + ", lng=" + location.getLng());
		eventBl.generateEvent(location, userId);
		return Response.status(Status.ACCEPTED).build();
	}
	
	@Override
	protected EventDTO toDTO(Event event){
		
		EventDTO dto = new EventDTO();
		dto.setId(event.getId());
		dto.setAuthorId(event.getAuthor().getId());
		dto.setAutoGenerated(event.isAutoGenerated());
		dto.setLocation(event.getLocation());
		dto.setMedia(event.getMedia());
		dto.setMediaType(event.getMediaType());
		dto.setText(event.getText());
		
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
		Event event = new Event();
		event.setAuthor(user);
		event.setAutoGenerated(dto.isAutoGenerated());
		event.setLocation(dto.getLocation());
		event.setMedia(dto.getMedia());
		event.setMediaType(dto.getMediaType());
		event.setText(dto.getText());
		
		return event;
	}
	
}