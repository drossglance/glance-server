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
		return toDTO(list);
	}

	@Override
	protected CommentDTO toDTO(Comment comment) {
		CommentDTO dto = new CommentDTO();
		dto.setId(comment.getId());
		dto.setAuthorId(comment.getUser().getId());
		dto.setSubjectId(comment.getSubject().getId());
		dto.setLocation(comment.getLocation());
		dto.setMedia(comment.getMedia());
		dto.setText(comment.getText());
		return dto;
	}
	
	@Override
	protected Comment fromDTO(CommentDTO dto) {

		User user = new User();
		user.setId(dto.getAuthorId());
		Event event = new Event();
		event.setId(dto.getSubjectId());
		Comment comment = new Comment();
		comment.setUser(user);
		comment.setSubject(event);
		comment.setLocation(dto.getLocation());
		comment.setMedia(dto.getMedia());
		comment.setText(dto.getText());
		
		return comment;
	}
	
}
