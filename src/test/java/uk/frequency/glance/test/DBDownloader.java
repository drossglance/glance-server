package uk.frequency.glance.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.frequency.glance.server.business.logic.TimeUtil;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.service.EventSL;
import uk.frequency.glance.server.transfer.event.EventDTO;
import uk.frequency.glance.server.transfer.trace.PositionTraceDTO;
import uk.frequency.glance.server.transfer.trace.TraceDTO;

import com.google.gson.reflect.TypeToken;

public class DBDownloader {

	private static final String ROOT_URL = "http://glance-server.herokuapp.com/services/";

	public static void main(String[] args) {
		Date begin = TimeUtil.parse("2013/04/10 00:00:00");
		Date end = TimeUtil.parse("2013/05/10 00:00:00");
		downloadTracesAndPrintForDebug(1, begin, end);
//		downloadAndPrintJson("user");
	}
	
	static void downloadTracesAndPrintForDebug(long userId, Date begin, Date end){
		List<PositionTraceDTO> traces = downloadPositionTraces(userId, begin, end);
		for(TraceDTO trace : traces){
			PositionTraceDTO pos = (PositionTraceDTO)trace;
			String time = TestDTOFormatter.format(pos.time);
			String line = String.format("%s\t%s", time, pos.position.toString());
			System.out.println(line);
		}
		System.out.println("size: " + traces.size());
	}
	
	public static List<PositionTraceDTO> downloadPositionTraces(long userId){
		return filterPositionTraces(downloadTraces(userId));
	}
	
	public static List<TraceDTO> downloadTraces(long userId){
		TestClient client = new TestClient(ROOT_URL);
		String path = String.format("trace/user-%d/", userId);
		return client.getList(path, new TypeToken<List<TraceDTO>>(){});
	}
	
	public static List<PositionTraceDTO> downloadPositionTraces(long userId, Date begin, Date end){
		return filterPositionTraces(downloadTraces(userId, begin, end));
	}
	
	public static List<TraceDTO> downloadTraces(long userId, Date begin, Date end){
		TestClient client = new TestClient(ROOT_URL);
		String path = String.format("trace/user-%d/%sto%s", userId, begin.getTime(), end.getTime());
		return client.getList(path, new TypeToken<List<TraceDTO>>(){});
	}
	
	public static List<EventDTO> downloadEvents(long userId){
		TestClient client = new TestClient(ROOT_URL);
		String path = String.format("event/user-%d/", userId);
		return client.getList(path, new TypeToken<List<EventDTO>>(){});
	}
	
	static void downloadAndPrintJson(String path){
		TestClient client = new TestClient(ROOT_URL);
		String json = client.getText(path);
		System.out.println(json);
	}
	
	public static List<PositionTraceDTO> filterPositionTraces(List<TraceDTO> traces){
		List<PositionTraceDTO> pos = new ArrayList<PositionTraceDTO>();
		for (TraceDTO trace : traces) {
			if(trace instanceof PositionTraceDTO){
				pos.add((PositionTraceDTO)trace);
			}
		}
		return pos;
	}
	
	public static List<Event> fromDTO(List<EventDTO> dtos){
		List<Event> events = new ArrayList<Event>();
		for(EventDTO dto : dtos){
			events.add(EventSL.staticFromDTO(dto));
		}
		return events;
	}
	
}
