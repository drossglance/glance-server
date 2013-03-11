package uk.frequency.glance.server.transfer.user;

import uk.frequency.glance.server.model.user.Friendship.Status;
import uk.frequency.glance.server.transfer.GenericDTO;


@SuppressWarnings("serial")
public class FriendshipDTO extends GenericDTO{

	long userId;
	
	long friendId;
	
	Status status;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getFriendId() {
		return friendId;
	}

	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
