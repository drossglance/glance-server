package uk.frequency.glance.testclient;

import java.util.List;

import uk.frequency.glance.server.transfer.trace.TraceDTO;

import com.google.gson.reflect.TypeToken;

public class DBDownloader {

	private static final String ROOT_URL = "http://glance-server.herokuapp.com/services/";

	public static void main(String[] args) {
//		downloadAndPrintTraces(98304);
//		downloadAndPrintJson("user");
	}
	
	static void downloadAndPrintTraces(long userId){
		List<TraceDTO> traces = downloadTraces(userId);
		for(TraceDTO trace : traces){
			System.out.println(TestDTOFormatter.format(trace));
		}
		System.out.println("size: " + traces.size());
	}
	
	static List<TraceDTO> downloadTraces(long userId){
		TestClient client = new TestClient(ROOT_URL);
		String path = String.format("trace/user-%d/", userId);
		return client.getList(path, new TypeToken<List<TraceDTO>>(){});
	}
	
	static void downloadAndPrintJson(String path){
		TestClient client = new TestClient(ROOT_URL);
		String json = client.getText(path);
		System.out.println(json);
	}
	
}
