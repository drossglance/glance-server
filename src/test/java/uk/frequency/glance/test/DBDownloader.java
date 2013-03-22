package uk.frequency.glance.test;

import java.util.ArrayList;
import java.util.List;

import uk.frequency.glance.server.transfer.trace.PositionTraceDTO;
import uk.frequency.glance.server.transfer.trace.TraceDTO;

import com.google.gson.reflect.TypeToken;

public class DBDownloader {

	private static final String ROOT_URL = "http://glance-server.herokuapp.com/services/";

	public static void main(String[] args) {
		downloadTracesAndPrintForDebug(2);
//		downloadAndPrintJson("user");
	}
	
	static void downloadTracesAndPrintForDebug(long userId){
		List<TraceDTO> traces = downloadTraces(userId);
		for(TraceDTO trace : traces){
			PositionTraceDTO pos = (PositionTraceDTO)trace;
			String time = TestDTOFormatter.format(pos.getTime());
			String line = String.format("%s\t%s", time, pos.getPosition().toString());
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
	
}
