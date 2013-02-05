package uk.frequencymobile.flow.server.model;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tb_user")
public class User extends GenericEntity{

	String userName;
	
	String password;
	
	String fullName;
	
	String imageUrl;
	
	String bgImageUrl;
	
	@OneToMany
	List<User> friends;
	
	@OneToMany
	List<Event> feed;
	
	@Embedded
	UserSettings settings;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getBgImageUrl() {
		return bgImageUrl;
	}

	public void setBgImageUrl(String bgImageUrl) {
		this.bgImageUrl = bgImageUrl;
	}

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

	public List<Event> getFeed() {
		return feed;
	}

	public void setFeed(List<Event> feed) {
		this.feed = feed;
	}

	public UserSettings getSettings() {
		return settings;
	}

	public void setSettings(UserSettings settings) {
		this.settings = settings;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + userName
				+ " | " + password
				+ " | " + fullName
				+ " | " + imageUrl
				+ " | " + bgImageUrl;
	}
	
}
