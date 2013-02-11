package uk.frequency.glance.server.data_access;

import java.util.List;

import org.hibernate.Query;

import uk.frequency.glance.server.model.Comment;
import uk.frequency.glance.server.model.User;

@SuppressWarnings("unchecked")
public class CommentDAL extends GenericDAL<Comment>{
	
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
