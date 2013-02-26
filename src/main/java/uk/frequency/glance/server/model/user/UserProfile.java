package uk.frequency.glance.server.model.user;

import javax.persistence.Embeddable;

@Embeddable
public class UserProfile {

	String userName;
	String password;
	
	String firstName;
	String middleName;
	String fullName;
	
	String bio;
	String birthday;
	
	String hometown;
	String location;
	String locale;
	
	String languages;
	
	String fb_link;
	
	String imageUrl;
	String bgImageUrl;
	
		
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


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getBio() {
		return bio;
	}


	public void setBio(String bio) {
		this.bio = bio;
	}


	public String getBirthday() {
		return birthday;
	}


	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}


	public String getHometown() {
		return hometown;
	}


	public void setHometown(String hometown) {
		this.hometown = hometown;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getLocale() {
		return locale;
	}


	public void setLocale(String locale) {
		this.locale = locale;
	}


	public String getLanguages() {
		return languages;
	}


	public void setLanguages(String languages) {
		this.languages = languages;
	}


	public String getFb_link() {
		return fb_link;
	}


	public void setFb_link(String fb_link) {
		this.fb_link = fb_link;
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


	@Override
	public String toString() {
		return super.toString()
				+ " | " + userName;
	}
	
}
