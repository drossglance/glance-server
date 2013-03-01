package uk.frequency.glance.server.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;

import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.trace.Trace;

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
	
	@OneToOne
	Event lastEvent;
	
	@OneToOne
	Event currentEvent;
	
	@OneToOne
	Trace lastUsedTrace;

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

	public Trace getLastUsedTrace() {
		return lastUsedTrace;
	}

	public void setLastUsedTrace(Trace lastUsedTrace) {
		this.lastUsedTrace = lastUsedTrace;
	}
	
}
