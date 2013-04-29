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
	Event currentPositionEvent;
	
	@LazyToOne(LazyToOneOption.FALSE)
	@OneToOne
	PositionTrace lastPositionTrace;

	Integer sleepCount = 0;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Event getCurrentPositionEvent() {
		return currentPositionEvent;
	}

	public void setCurrentPositionEvent(Event currentPositionEvent) {
		this.currentPositionEvent = currentPositionEvent;
	}

	public PositionTrace getLastPositionTrace() {
		return lastPositionTrace;
	}

	public void setLastPositionTrace(PositionTrace lastPositionTrace) {
		this.lastPositionTrace = lastPositionTrace;
	}

	public Integer getSleepCount() {
		return sleepCount;
	}

	public void setSleepCount(Integer sleepCount) {
		this.sleepCount = sleepCount;
	}

	@Override
	public String toString() {
		return user.getId() +  "";
	}
	
}
