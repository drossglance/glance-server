package uk.frequency.glance.server.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import uk.frequency.glance.server.business.CommentBL;
import uk.frequency.glance.server.model.Comment;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.user.User;
import uk.frequency.glance.server.transfer.CommentDTO;


@Path("/comment")
public class CommentSL extends GenericSL<Comment, CommentDTO>{

	CommentBL commentBl;

	public CommentSL() {
		super(new CommentBL());
		commentBl = (CommentBL)business;
	}
	
	@GET
	@Path("/user-{id}")
	public List<CommentDTO> findByAuthor(@PathParam("id") long userId) {
		List<Comment> list = commentBl.findByAuthor(userId);
		List<CommentDTO> dto = toDTO(list);
		commentBl.flush();
		return dto;
	}

	@Override
	protected CommentDTO toDTO(Comment comment) {
		CommentDTO dto = new CommentDTO();
		dto.initFromEntity(comment);
		dto.userId = comment.getUser().getId();
		dto.subjectId = comment.getSubject().getId();
		dto.location = comment.getLocation();
		dto.media = comment.getMedia();
		dto.text = comment.getText();
		return dto;
	}
	
	@Override
	protected Comment fromDTO(CommentDTO dto) {

		User user = new User();
		user.setId(dto.userId);
		Event event = new Event();
		event.setId(dto.subjectId);
		Comment comment = new Comment();
		dto.initEntity(comment);
		comment.setUser(user);
		comment.setSubject(event);
		comment.setLocation(dto.location);
		comment.setMedia(dto.media);
		comment.setText(dto.text);
		return comment;
	}
	
}
