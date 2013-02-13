package uk.frequency.glance.test;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.ArrayList;
import java.util.List;

import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.Media;
import uk.frequency.glance.server.model.UserProfile;
import uk.frequency.glance.server.model.Media.MediaType;
import uk.frequency.glance.server.transfer.EventDTO;
import uk.frequency.glance.server.transfer.GenericDTO;
import uk.frequency.glance.server.transfer.UserDTO;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RESTClient {

	static Client client = Client.create();
	static String host = "localhost:8080";
//	static String host = "flow-server.herokuapp.com";

	public static void main(String[] args) {
		putDummyUsers(1);
	}

	static void putDummyUsers(int n) {
		for(int i=0; i<n; i++){
			// PUT user
			UserProfile profile = new UserProfile();
			profile.setUserName("user" + i);
			profile.setFullName("User Name " + i);
			UserDTO user = new UserDTO();
			user.setProfile(profile);
			put(user, "user");
		}
	}
	
	static void putDummyEvents(int n) {
		for(int i=0; i<n; i++){
			// PUT event
			Location location = new Location();
			location.setLat(51.52257);
			location.setLng(-0.08553);
			location.setAddress("5 Bonhill St.");
			location.setName("Google Campus");
			Media media = new Media();
			media.setType(MediaType.IMAGE);
			media.setUrl("http://www.nottingham.ac.uk/UGstudy/images-multimedia/Open-day-image-dtp-Cropped-714x474.jpg");
			List<Media> medias = new ArrayList<Media>();
			medias.add(media);
			EventDTO event = new EventDTO();
			event.setAuthorId(1);
			event.setLocation(location);
			event.setText("bla bla bla bla bla..");
			event.setMedia(medias);
			put(event, "event");
		}
	}

	static void put(GenericDTO dto, String path) {
		WebResource resource = client.resource("http://" + host + "/services/" + path);
		ClientResponse response = resource.accept(TEXT_PLAIN).put(ClientResponse.class, dto);
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		System.out.println("output from server:");
		System.out.println(response.getLocation());
		System.out.println(response.toString());
	}

	
}