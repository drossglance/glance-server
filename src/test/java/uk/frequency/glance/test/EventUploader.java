package uk.frequency.glance.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.frequency.glance.server.business.remote.EventDataFinder;
import uk.frequency.glance.server.model.component.Media;
import uk.frequency.glance.server.model.component.Media.MediaType;
import uk.frequency.glance.server.model.component.Position;
import uk.frequency.glance.server.model.event.EventType;
import uk.frequency.glance.server.transfer.event.EventDTO;
import uk.frequency.glance.server.transfer.event.MoveEventDTO;
import uk.frequency.glance.server.transfer.event.StayEventDTO;
import uk.frequency.glance.server.transfer.user.UserDTO;

public class EventUploader {

	static final String DIR = "src/test/java/test_cases";
	static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	
//	static String ROOT_URL = "http://localhost:8080/services/";
	static String ROOT_URL = "http://glance-server.herokuapp.com/services/";
	public static final TestClient client = new TestClient(ROOT_URL);
	
	public static void main(String[] args) {
		try {
			upload("dummy_events_1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void upload(String fileName) throws IOException, ParseException, InterruptedException{
		UserDTO user = TestCaseLauncher.createTestUser("testuser1");
		user = (UserDTO) client.postAndPrint(user, "user");
		
		List<EventDTO> events = loadEvents(fileName, user.getId());
		for(EventDTO event : events){
			client.post(event, "event");
		}
		
		System.out.println("done.");
	}
	
	static List<EventDTO> loadEvents(String fileName, long user) throws IOException, ParseException{
		File file = new File(DIR, fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));

		List<EventDTO> events = new ArrayList<EventDTO>();
		String line = reader.readLine();
		while(line != null){
			EventDTO event = parseEvent(line, user);
			event.setUserId(user);
			events.add(event);
			line = reader.readLine();
		}

		reader.close();
		return events;
	}
	
	static EventDTO parseEvent(String line, long user) throws ParseException{
		String[] parts = line.split("\t");
		String type = parts[0];
		Date startTime = parseTime(parts[1]);
		Date endTime = parseTime(parts[2]);
		Position startPos = parsePosition(parts[3]);
		Position endPos = parts.length < 5 ? null : parsePosition(parts[4]);

		EventDataFinder finderStart = new EventDataFinder(startPos);
		EventDataFinder finderEnd = endPos==null? null : new EventDataFinder(endPos);
		
		EventDTO event;
		if("STAY".equals(type)){
			StayEventDTO stay = new StayEventDTO();
			stay.setLocation(finderStart.getLocation());
			stay.setEndTime(endTime.getTime());
			event = stay;
		}else if("MOVE".equals(type)){
			MoveEventDTO move = new MoveEventDTO();
			move.setStartLocation(finderStart.getLocation());
			move.setEndLocation(finderEnd.getLocation());
			move.setEndTime(endTime.getTime());
			List<Position> trail = new ArrayList<Position>();
			move.setTrail(trail);
			event = move;
		}else if("SLEEP".equals(type)){
			StayEventDTO sleep = new StayEventDTO();
			sleep.setType(EventType.SLEEP);
			sleep.setEndTime(endTime.getTime());
			sleep.setLocation(finderStart.getLocation());
			event = sleep;
		}else if("WAKE".equals(type)){
			StayEventDTO wake = new StayEventDTO();
			wake.setType(EventType.WAKE);
			wake.setLocation(finderStart.getLocation());
			event = wake;
		}else{
			throw new AssertionError();
		}
		
		event.setStartTime(startTime.getTime());
		
		List<Media> media = new ArrayList<Media>();
		Media img = new Media();
		img.setType(MediaType.IMAGE);
		img.setUrl(finderStart.getImageUrl());
		media.add(img);
		event.setMedia(media);
		
		return event;
	}
	
	static Position parsePosition(String str){
		if(str.length() == 0)
			return null;
		String[] latlngParts = str.split(",");
		double lat = Double.valueOf(latlngParts[0]);
		double lng = Double.valueOf(latlngParts[1]);
		Position pos = new Position();
		pos.setLat(lat);
		pos.setLng(lng);
		return pos;
	}
	
	static Date parseTime(String str){
		try {
			return new SimpleDateFormat(DATE_FORMAT).parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

}
