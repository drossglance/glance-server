package uk.frequencymobile.flow.dev;

import javax.ws.rs.core.MediaType;

import uk.frequencymobile.flow.server.model.GenericEntity;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RESTClient {

	static Client client = Client.create();
//	static String host = "localhost:8080";
	static String host = "flow-server.herokuapp.com";
	
	public static void main(String[] args) {
        
        // PUT user
//        User user = new User();
//        user.setUserName("george");
//        user.setPassword("password");
//        user.setFullName("George Design Guy");
//        user.setImageUrl("george.png");
//        user.setBgImageUrl("bg.png");

		
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
        
		// PUT event
//		User author = new User();
//		author.setId(1);
//		Location location = new Location();
//		location.setLat(3.545484);
//		location.setLng(2.546464);
//		location.setAddress("5 Bonhill St.");
//		location.setDescription("Google Campus");
//		Event event = new Event();
//		event.setAuthor(author);
//		event.setImageUrl("bla.png");
//		event.setLocation(location);
//		event.setText("blAblbalBlablb a bal abl Lablab");
//		put(event, "event");

		
		// GET event
//		Event event = resource.path(""+1)
//			.accept(MediaType.APPLICATION_JSON)
//	        .get(Event.class);
//		System.out.println(event);

	}

	public static void put(GenericEntity entity, String path){
		WebResource resource = client.resource("http://" + host + "/services/" + path);
		ClientResponse response = resource
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, entity);
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		System.out.println("output from server:");
		System.out.println(response.getLocation());
		System.out.println(response.toString());
	}
	
}
