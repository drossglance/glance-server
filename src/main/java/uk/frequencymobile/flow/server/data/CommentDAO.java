package uk.frequencymobile.flow.server.data;

import java.util.List;

import org.hibernate.Query;

import uk.frequencymobile.flow.server.model.Comment;
import uk.frequencymobile.flow.server.model.User;

@SuppressWarnings("unchecked")
public class CommentDAO extends GenericDAO<Comment>{
	
	public List<Comment> findByAuthor(User author){
		Query q = session.createQuery("from Comment where author = :user")
			.setEntity("user", author);
		return q.list();
	}
	
	public List<Comment> findByAuthor(long authorId){
		Query q = session.createQuery("from Comment where author.id = :userId")
			.setParameter("userId", authorId);
		return q.list();
	}
	
}
