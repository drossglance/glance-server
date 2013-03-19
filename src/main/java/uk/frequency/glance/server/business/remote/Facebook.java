package uk.frequency.glance.server.business.remote;


import uk.frequency.glance.server.model.user.User;
import uk.frequency.glance.server.model.user.UserProfile;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Page;

@SuppressWarnings("unused")
public class Facebook {

	private final String APP_ID = "464965430237574"; //TODO move to properties
	private final String APP_SECRET = "6f6f32b0da6da27c6e9e52e00e17fa94"; //TODO move to properties
	private final FacebookClient client;

	public Facebook(String ACCESS_TOKEN) {
		client = new DefaultFacebookClient(ACCESS_TOKEN);
	}
	
	public Facebook() {
		client = new DefaultFacebookClient();
	}

	public UserProfile requestUserData() {
		UserProfile profile = new UserProfile();
		
		com.restfb.types.User fbUser = client.fetchObject("me", com.restfb.types.User.class);
		profile.setAbout(fbUser.getAbout());
		profile.setBio(fbUser.getBio());
		profile.setBirthday(fbUser.getBirthday());
		profile.setEmail(fbUser.getEmail());
		profile.setFirstName(fbUser.getFirstName());
		profile.setGender(fbUser.getGender());
		profile.setHometown(fbUser.getHometownName());
		fbUser.getLanguages();
		profile.setLocale(fbUser.getLocale());
		profile.setMiddleName(fbUser.getMiddleName());
		profile.setFullName(fbUser.getName());
		profile.setTimezone(fbUser.getTimezone());
		profile.setUpdatedTime(fbUser.getUpdatedTime());
		profile.setFbUsername(fbUser.getUsername());
		profile.setWebsite(fbUser.getWebsite());
		profile.setFbLink(fbUser.getLink());
		
		Page page = client.fetchObject("me", Page.class);
		profile.setImageUrl(page.getPicture());
		profile.setBgImageUrl(page.getCover().getSource());
		
		return profile;
	}

}
