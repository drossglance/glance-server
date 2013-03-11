package uk.frequency.glance.testclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.frequency.glance.server.model.component.Position;
import uk.frequency.glance.server.model.user.UserProfile;
import uk.frequency.glance.server.transfer.event.EventDTO;
import uk.frequency.glance.server.transfer.trace.PositionTraceDTO;
import uk.frequency.glance.server.transfer.user.UserDTO;

import com.google.gson.reflect.TypeToken;

public class TestCaseLauncher {

	static final String DIR = "src/test/java/test_cases";
	static final String DATE_FORMAT = "HH:mm:ss";
	static final int TIME_BETWEEN_REQUESTS = 1 * 1000;

	public static void main(String[] args) {
		try {
			runTestCase("case_1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void runTestCase(String fileName) throws IOException, ParseException, InterruptedException{
		UserDTO user = createTestUser();
		user = (UserDTO) TestClient.postAndPrint(user, "user");
		
		List<PositionTraceDTO> traces = loadTraces(fileName, user.getId());
		for(PositionTraceDTO trace : traces){
			Date requestTime = new Date();
			TestClient.postAndPrint(trace, "trace");
			Thread.sleep(TIME_BETWEEN_REQUESTS);
			List<EventDTO> events = (List<EventDTO>) TestClient.getListAndPrint("event/user-" + user.getId() + "/created_after-" + requestTime.getTime(), new TypeToken<List<EventDTO>>(){});
			verifyEvents(events);
		}
	}
	
	static boolean verifyEvents(List<EventDTO> events){
		//TODO
		return true;
	}
	
	static UserDTO createTestUser(){
		UserProfile profile = new UserProfile();
		profile.setFullName("Test");
		UserDTO user = new UserDTO();
		user.setUsername("testuser");
		user.setProfile(profile);
		return user;
	}
	
	static List<PositionTraceDTO> loadTraces(String fileName, long user) throws IOException, ParseException{
		File file = new File(DIR, fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));

		List<PositionTraceDTO> traces = new ArrayList<PositionTraceDTO>();
		String line = reader.readLine();
		while(line != null){
			PositionTraceDTO trace = parseTrace(line, user);
			traces.add(trace);
			line = reader.readLine();
		}

		reader.close();
		return traces;
	}
	
	static PositionTraceDTO parseTrace(String line, long user) throws ParseException{
		String[] parts = line.split("\t");
		String[] latlngParts = parts[0].split(",");
		long time = new SimpleDateFormat(DATE_FORMAT).parse(parts[1]).getTime();
		double lat = Double.valueOf(latlngParts[0]);
		double lng = Double.valueOf(latlngParts[1]);
		Position pos = new Position();
		pos.setLat(lat);
		pos.setLng(lng);
		PositionTraceDTO trace = new PositionTraceDTO();
		trace.setPosition(pos);
		trace.setTime(time);
		trace.setUserId(user);
		return trace;
	}

}