package uk.frequencymobile.flow.dev;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import uk.frequencymobile.flow.server.model.Event;
import uk.frequencymobile.flow.server.model.GenericEntity;
import uk.frequencymobile.flow.server.model.Location;
import uk.frequencymobile.flow.server.model.Media;
import uk.frequencymobile.flow.server.model.Media.Type;
import uk.frequencymobile.flow.server.model.User;
import uk.frequencymobile.flow.server.model.UserProfile;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RESTClient {

	static Client client = Client.create();
	static String host = "localhost:8080";
//	static String host = "flow-server.herokuapp.com";
	
	public static void createDummyData() {
		
		for(int i=0; i<10; i++){
			// PUT user
			UserProfile profile = new UserProfile();
			profile.setUserName("user" + i);
			profile.setFullName("User Name " + i);
			User user = new User();
			List<UserProfile> profiles = new ArrayList<UserProfile>();
			profiles.add(profile);
			user.setUserProfile(profiles);
			put(user, "user");
		}
		
		for(int i=0; i<10; i++){
			// PUT event
			User author = new User();
			author.setId(1);
			Location location = new Location();
			location.setLat(51.52257);
			location.setLng(-0.08553);
			location.setAddress("5 Bonhill St.");
			location.setDescription("Google Campus");
			Media media = new Media();
			media.setType(Type.IMAGE);
			media.setUrl("image" + i + ".png");
			List<Media> medias = new ArrayList<Media>();
			medias.add(media);
			Event event = new Event();
			event.setAuthor(author);
			event.setLocation(location);
			event.setText("bla bla bla bla bla..");
			event.setMedia(medias);
			put(event, "event");
		}
		
	}

	public static void put(GenericEntity entity, String path) {
		WebResource resource = client.resource("http://" + host + "/services/" + path);
		ClientResponse response = resource.accept(MediaType.TEXT_PLAIN).put(ClientResponse.class, entity);
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		System.out.println("output from server:");
		System.out.println(response.getLocation());
		System.out.println(response.toString());
	}

	public static void main(String[] args) {
        
		createDummyData();
		
        // GET user
//        User user = resource.path(""+output)
//            .accept(MediaType.APPLICATION_JSON)
//            .get(User.class);
//        System.out.println(user);
 
        
//        // GET all users
//        List<User> users = resource
//            .accept(MediaType.APPLICATION_JSON)
//            .get(new GenericType<List<User>>(){});
//        System.out.println("size = " + users.size());
//        
//        for(User u : users){
//        	System.out.println(u);
//        }
        
		// GET event
//		Event event = resource.path(""+1)
//			.accept(MediaType.APPLICATION_JSON)
//	        .get(Event.class);
//		System.out.println(event);

	}
	
}