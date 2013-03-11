package uk.frequency.glance.server.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.trace.PositionTrace;

@Entity
public class EventGenerationInfo {

	@Id
    @GeneratedValue(generator = "foreign")
    @GenericGenerator(
        name = "foreign",
        strategy = "foreign",
        parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "user")})
    @Column(name = "user_id")
    private long userId;
	
	@OneToOne
	@PrimaryKeyJoinColumn(name="user_id")
	User user;
	
	@LazyToOne(LazyToOneOption.FALSE)
	@OneToOne
	Event lastEvent;
	
	@LazyToOne(LazyToOneOption.FALSE)
	@OneToOne
	Event currentEvent;
	
	@LazyToOne(LazyToOneOption.FALSE)
	@OneToOne
	PositionTrace lastPositionTrace;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Event getLastEvent() {
		return lastEvent;
	}

	public void setLastEvent(Event lastEvent) {
		this.lastEvent = lastEvent;
	}

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(Event currentEvent) {
		this.currentEvent = currentEvent;
	}

	public PositionTrace getLastPositionTrace() {
		return lastPositionTrace;
	}

	public void setLastPositionTrace(PositionTrace lastPositionTrace) {
		this.lastPositionTrace = lastPositionTrace;
	}

	@Override
	public String toString() {
		return user.getId() +  "";
	}
	
}
