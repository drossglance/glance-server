package uk.frequency.glance.server.business;

import java.util.List;

import uk.frequency.glance.server.data_access.CommentDAL;
import uk.frequency.glance.server.model.Comment;

public class CommentBL extends GenericBL<Comment>{

	CommentDAL commentDal;

	public CommentBL() {
		super(new CommentDAL());
		commentDal = (CommentDAL)dal;
	}
	
	public List<Comment> findByAuthor(long userId) {
		List<Comment> list = commentDal.findByAuthor(userId);
		return list;
	}
	
}
