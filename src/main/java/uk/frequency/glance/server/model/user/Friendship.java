package uk.frequency.glance.server.model.user;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import uk.frequency.glance.server.model.GenericEntity;


@Entity
//@IdClass(FriendshipId.class)
@Table(uniqueConstraints={
	@UniqueConstraint(columnNames={"user_id", "friend_id"})
})
public class Friendship extends GenericEntity{

//	@Id
//	@GeneratedValue(generator = "foreign")
//    @GenericGenerator(
//        name = "foreign",
//        strategy = "foreign",
//        parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "user")})
//    @Column(name = "user_id")
//	long userId;
//	
//	@Id
//    @GeneratedValue(generator = "foreign")
//    @GenericGenerator(
//        name = "foreign",
//        strategy = "foreign",
//        parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "friend")})
//    @Column(name = "friend_id")
//	long friendId;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	User user;
	
	@ManyToOne
	@JoinColumn(name="friend_id")
	User friend;
	
	FriendshipStatus status;
	
//	@Formula("(select f from Friendship f where f.user = friend and f.friend = user)")
//	@OneToOne
//	Friendship reciprocal;
	
//	@SuppressWarnings("serial")
//	public class FriendshipId implements Serializable{
//		
//		long userId, friendId;
//		
//		@Override
//		public boolean equals(Object obj) {
//			if(obj instanceof FriendshipId){
//				FriendshipId id = ((FriendshipId) obj);
//				return id.userId == userId && id.friendId == friendId;
//			}else{
//				return false;
//			}
//		}
//		
//		@Override
//		public int hashCode() {
//			return (int)(userId*31 + friendId); //http://stackoverflow.com/questions/892618/create-a-hashcode-of-two-numbers
//		}
//	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}

	public FriendshipStatus getStatus() {
		return status;
	}

	public void setStatus(FriendshipStatus status) {
		this.status = status;
	}

}
