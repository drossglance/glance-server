package uk.frequency.glance.server.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import uk.frequency.glance.server.model.event.Event;

@Entity
@Table(name="tb_user") //for PostgreSQL compatibility, TODO:do this for all tables as a NamingStrategy
public class User extends GenericEntity{

	@ElementCollection
	@CollectionTable(joinColumns=@JoinColumn(name="user_id")) //for PostgreSQL compatibility, TODO:do this for all fks as a NamingStrategy
	List<UserProfile> profileHistory;
	
	@Embedded
	UserSettings settings;
	
	@OneToMany
	List<User> friends;
	
	@OneToMany(mappedBy="author")
	List<Event> events;
	
	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
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

	@Override
	public String toString() {
		return super.toString()
				+ " | " + profileHistory;
	}
	
}
