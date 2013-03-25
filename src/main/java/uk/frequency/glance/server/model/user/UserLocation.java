package uk.frequency.glance.server.model.user;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import uk.frequency.glance.server.model.GenericEntity;
import uk.frequency.glance.server.model.Location;

@Entity
@Table(uniqueConstraints={
	@UniqueConstraint(columnNames={"user_id", "location_id"})
})
public class UserLocation extends GenericEntity{

	public static enum LocationType{
		HOME, WORK, SCHOOL, REGULAR
	}
	
	@ManyToOne
	@JoinColumn(name="user_id")
	User user;
	
	@ManyToOne
	@JoinColumn(name="location_id")
	Location location;
	
	LocationType type;

}
