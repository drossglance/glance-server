package uk.frequency.glance.server.data_access;

import java.util.List;

import org.hibernate.Query;

import uk.frequency.glance.server.model.Comment;
import uk.frequency.glance.server.model.user.User;

@SuppressWarnings("unchecked")
public class CommentDAL extends GenericDAL<Comment>{
	
	public List<Comment> findByAuthor(User author){
		Query q = getSession().createQuery("from Comment where user = :user")
			.setEntity("user", author);
		return q.list();
	}
	
	public List<Comment> findByAuthor(long authorId){
		Query q = getSession().createQuery("from Comment where user.id = :userId")
			.setParameter("userId", authorId);
		return q.list();
	}
	
}
