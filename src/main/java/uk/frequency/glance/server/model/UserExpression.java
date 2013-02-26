package uk.frequency.glance.server.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import uk.frequency.glance.server.model.user.User;

@MappedSuperclass
public abstract class UserExpression extends GenericEntity{

	@ManyToOne
	@JoinColumn(name="ref_user")
	User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return super.toString()
				+ " | " + user.id;
	}
	
}
