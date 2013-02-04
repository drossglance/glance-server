package uk.frequencymobile.flow.dev;

import javax.ws.rs.core.MediaType;

import uk.frequencymobile.flow.server.model.Event;
import uk.frequencymobile.flow.server.model.Location;
import uk.frequencymobile.flow.server.model.User;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RESTClient {

	public static void main(String[] args) {
		Client client = Client.create();
        WebResource resource = client.resource("http://localhost:8080/FlowServer/rest/event");
        
//        // PUT user
//        ClientResponse response = resource
//        		.queryParam("username", "fionn")
//        		.queryParam("password", "123")
//        		.queryParam("fullname", "Fionn")
//        		.queryParam("image_url", "bla")
//        		.queryParam("bg_image_url", "ble")
//        		.accept(MediaType.WILDCARD)
//        		.put(ClientResponse.class);
////        if (response.getStatus() != 201) {
//        if (response.getStatus() != 200) {
//			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//		}
//		System.out.println("output from server:");
//		String output = response.getEntity(String.class);
//		System.out.println("id = " + output);
        
		
//        // GET user
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
        User author = new User();
		author.setId(1);
		Location location = new Location();
		location.setLat(3.545484);
		location.setLng(2.546464);
		location.setAddress("5 Bonhill St.");
		location.setDescription("Google Campus");
		Event event = new Event();
		event.setAuthor(author);
		event.setImageUrl("bla.png");
		event.setLocation(location);
		event.setText("blAblbalBlablb a bal abl Lablab");

		ClientResponse response = resource
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, event);
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		System.out.println("output from server:");
		System.out.println(response.getLocation());
		
		
		// GET event
//		Event event = resource.path(""+1)
//			.accept(MediaType.APPLICATION_JSON)
//	        .get(Event.class);
//		System.out.println(event);

	}

}
