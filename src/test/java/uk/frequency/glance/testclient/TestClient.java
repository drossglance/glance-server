package uk.frequency.glance.testclient;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.frequency.glance.server.model.component.Location;
import uk.frequency.glance.server.model.component.Media;
import uk.frequency.glance.server.model.component.Media.MediaType;
import uk.frequency.glance.server.model.component.Position;
import uk.frequency.glance.server.model.event.EventScore;
import uk.frequency.glance.server.model.user.UserProfile;
import uk.frequency.glance.server.transfer.GenericDTO;
import uk.frequency.glance.server.transfer.UserDTO;
import uk.frequency.glance.server.transfer.event.EventDTO;
import uk.frequency.glance.server.transfer.event.ListenEventDTO;
import uk.frequency.glance.server.transfer.event.MoveEventDTO;
import uk.frequency.glance.server.transfer.event.StayEventDTO;
import uk.frequency.glance.server.transfer.event.TellEventDTO;
import uk.frequency.glance.server.transfer.trace.ListenTraceDTO;
import uk.frequency.glance.server.transfer.trace.PositionTraceDTO;
import uk.frequency.glance.server.transfer.trace.TraceDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TestClient {

	static Client client = Client.create();
	static String host = "localhost:8080";
//	static String host = "glance-server.herokuapp.com";
	static Gson gson = buildGson();

	public static void main(String[] args) {
//		putDummyTraces(1);
//		putDummyUsers(1);
//		putDummyEvents(1);
//		get(TraceDTO.class, "trace", 9);
//		get(TraceDTO.class, "event", 9);
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
	
	static void putDummyTraces(int n) {
		for(int i=0; i<n; i++){
			// PUT trace
			PositionTraceDTO trace = new PositionTraceDTO();
			Position pos = new Position();
			pos.setLat(51.52257);
			pos.setLng(-0.08553);
			trace.setPosition(pos);
			trace.setSpeed(10);
			trace.setTime(new Date().getTime());
			trace.setUserId(1);
			put(trace, "trace");
		}
	}
	
	static void putDummyEvents(int n) {
		for(int i=0; i<n; i++){
			// PUT event
			Position position = new Position();
			position.setLat(51.52257);
			position.setLng(-0.08553);
			Location location = new Location();
			location.setPosition(position);
			location.setAddress("5 Bonhill St.");
			location.setName("Google Campus");
			Media media = new Media();
			media.setType(MediaType.IMAGE);
			media.setUrl("http://www.nottingham.ac.uk/UGstudy/images-multimedia/Open-day-image-dtp-Cropped-714x474.jpg");
			List<Media> medias = new ArrayList<Media>();
			medias.add(media);
			EventScore score = new EventScore();
			TellEventDTO event = new TellEventDTO();
			event.setAuthorId(1);
			event.setLocation(location);
			event.setText("bla bla bla bla bla..");
			event.setMedia(medias);
			event.setScore(score);
			put(event, "event");
		}
	}

	static void put(GenericDTO dto, String path) {
		String json = gson.toJson(dto);
		WebResource resource = client.resource("http://" + host + "/services/" + path);
		ClientResponse response = resource
				.type(APPLICATION_JSON)
				.accept(TEXT_PLAIN)
				.put(ClientResponse.class, json);
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		System.out.println("output from server:");
		System.out.println(response.getLocation());
		System.out.println(response.toString());
	}
	
	static <T> T get(Class<T> clazz, String path, long id){
		WebResource resource = client.resource("http://" + host + "/services/" + path + "/" + id);
		String json = resource
				.accept(APPLICATION_JSON)
				.get(String.class);
		T obj = gson.fromJson(json, clazz);
		return obj;
	}

	static Gson buildGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(TraceDTO.class, GsonTypeAdapter.getTraceInstance());
		builder.registerTypeAdapter(PositionTraceDTO.class, GsonTypeAdapter.getTraceInstance());
		builder.registerTypeAdapter(ListenTraceDTO.class, GsonTypeAdapter.getTraceInstance());
		builder.registerTypeAdapter(EventDTO.class, GsonTypeAdapter.getEventInstance());
		builder.registerTypeAdapter(StayEventDTO.class, GsonTypeAdapter.getEventInstance());
		builder.registerTypeAdapter(MoveEventDTO.class, GsonTypeAdapter.getEventInstance());
		builder.registerTypeAdapter(TellEventDTO.class, GsonTypeAdapter.getEventInstance());
		builder.registerTypeAdapter(ListenEventDTO.class, GsonTypeAdapter.getEventInstance());
		return builder.create();
	}
	
}