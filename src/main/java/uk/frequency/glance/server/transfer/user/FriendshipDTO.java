package uk.frequency.glance.server.transfer.user;

import java.util.ArrayList;
import java.util.List;

import uk.frequency.glance.server.model.user.Friendship;
import uk.frequency.glance.server.model.user.FriendshipStatus;
import uk.frequency.glance.server.transfer.GenericDTO;


@SuppressWarnings("serial")
public class FriendshipDTO extends GenericDTO{

	public long userId;
	
	public long friendId;
	
	public FriendshipStatus status;
	
	public static FriendshipDTO from(Friendship friendship){
		FriendshipDTO dto = new FriendshipDTO();
		dto.initFromEntity(friendship);
		
		dto.userId = friendship.getUser().getId();
		dto.friendId = friendship.getFriend().getId();
		dto.status = friendship.getStatus();
		
		return dto;
	}
	
	public static List<FriendshipDTO> from(List<Friendship> list){
		List<FriendshipDTO> dto = new ArrayList<FriendshipDTO>();
		for(Friendship entity : list){
			dto.add(from(entity));
		}
		return dto;
	}
}
