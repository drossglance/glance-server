package uk.frequency.glance.server.model.user;

import java.util.Arrays;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import uk.frequency.glance.server.model.GenericEntity;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.trace.Trace;

@Entity
@Table(name="tb_user") //for PostgreSQL compatibility, TODO:do this for all tables as a NamingStrategy
public class User extends GenericEntity{

	String username;
	
	String facebookId;
	
	@ElementCollection
	@CollectionTable(joinColumns=@JoinColumn(name="user_id")) //for PostgreSQL compatibility, TODO:do this for all fks as a NamingStrategy
	List<UserProfile> profileHistory;
	
	UserSettings settings;
	
	@OneToMany(mappedBy="user")
	List<Event> events;
	
	@OneToMany(mappedBy="user")
	List<Trace> traces;
	
	@OneToOne(mappedBy="user")
	EventGenerationInfo eventGenerationInfo;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public UserSettings getSettings() {
		return settings;
	}

	public void setSettings(UserSettings settings) {
		this.settings = settings;
	}
	

	public List<UserProfile> getProfileHistory() {
		return profileHistory;
	}

	public void setProfileHistory(List<UserProfile> profileHistory) {
		this.profileHistory = profileHistory;
	}
	
	public void setProfile(UserProfile... profile){
		this.profileHistory = Arrays.asList(profile);
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public List<Trace> getTraces() {
		return traces;
	}

	public void setTraces(List<Trace> traces) {
		this.traces = traces;
	}

	public EventGenerationInfo getEventGenerationInfo() {
		return eventGenerationInfo;
	}

	public void setEventGenerationInfo(EventGenerationInfo eventGenerationInfo) {
		this.eventGenerationInfo = eventGenerationInfo;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + profileHistory;
	}
	
}
