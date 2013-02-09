package uk.frequencymobile.flow.server.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import uk.frequencymobile.flow.server.data.CommentDAO;
import uk.frequencymobile.flow.server.data.UserDAO;
import uk.frequencymobile.flow.server.model.Comment;
import uk.frequencymobile.flow.server.model.Event;
import uk.frequencymobile.flow.server.model.User;
import uk.frequencymobile.flow.server.transfer.CommentDTO;


@Path("/comment")
public class CommentService extends GenericService<Comment, CommentDTO>{

	CommentDAO commentDao;
	UserDAO userDao;

	public CommentService() {
		super(new CommentDAO());
		commentDao = (CommentDAO)dao;
		userDao = new UserDAO();
	}
	
	@GET
	@Path("/user-{id}")
	public List<CommentDTO> findByAuthor(@PathParam("id") long userId) {
		List<Comment> list = commentDao.findByAuthor(userId);
		return toDTO(list);
	}

	@Override
	protected CommentDTO toDTO(Comment comment) {
		CommentDTO dto = new CommentDTO();
		dto.setId(comment.getId());
		dto.setAuthorId(comment.getAuthor().getId());
		dto.setSubjectId(comment.getSubject().getId());
		dto.setLocation(comment.getLocation());
		dto.setMedia(comment.getMedia());
		dto.setMediaType(comment.getMediaType());
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
		comment.setAuthor(user);
		comment.setSubject(event);
		comment.setLocation(dto.getLocation());
		comment.setMedia(dto.getMedia());
		comment.setMediaType(dto.getMediaType());
		comment.setText(dto.getText());
		
		return comment;
	}
	
}
