package uk.frequency.glance.testclient;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import uk.frequency.glance.server.service.util.JsonMessageBodyHandler;
import uk.frequency.glance.server.transfer.GenericDTO;
import uk.frequency.glance.server.transfer.UserDTO;
import uk.frequency.glance.server.transfer.event.EventDTO;
import uk.frequency.glance.server.transfer.trace.TraceDTO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TestClient {

	static String host = "localhost:8080";
//	static String host = "glance-server.herokuapp.com";
	
	static Client client = Client.create();
	static Gson gson = JsonMessageBodyHandler.buildGson();

	public static void main(String[] args) {

	}

	public static GenericDTO post(GenericDTO dto, String path) {
		String json = gson.toJson(dto);
		WebResource resource = client.resource("http://" + host + "/services/" + path);
		ClientResponse response = resource
				.type(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(ClientResponse.class, json);
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : " + response.getStatus() + "\n" + response.getEntity(String.class).toString());
		}else{
			String json2 = response.getEntity(JSONObject.class).toString();
			if(dto instanceof EventDTO)
				return gson.fromJson(json2, EventDTO.class);
			else if(dto instanceof TraceDTO)
				return gson.fromJson(json2, TraceDTO.class);
			else if(dto instanceof UserDTO)
				return gson.fromJson(json2, UserDTO.class);
			else
				throw new AssertionError();
		}
	}
	
	public static <T> T get(String path, Class<T> type){
		WebResource resource = client.resource("http://" + host + "/services/" + path);
		ClientResponse response = resource
				.accept(APPLICATION_JSON)
				.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : " + response.getStatus() + "\n" + response.getEntity(String.class).toString());
		}else{
			String json = response.getEntity(String.class);
			T obj = gson.fromJson(json, type);
			return obj;
		}
	}
	
	public static <T> List<T> getList(String path, TypeToken<List<T>> type){
		WebResource resource = client.resource("http://" + host + "/services/" + path);
		ClientResponse response = resource
				.accept(APPLICATION_JSON)
				.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : " + response.getStatus() + "\n" + response.getEntity(String.class).toString());
		}else{
			String json = response.getEntity(String.class);
			List<T> obj = gson.fromJson(json, type.getType());
			return obj;
		}
	}
	
	public static GenericDTO postAndPrint(GenericDTO dto, String path) {
		try{
			GenericDTO resp = post(dto, path);
			System.out.println(TestDTOFormatter.format(resp));
			return resp;
		}catch(RuntimeException e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public static <T extends GenericDTO> T getAndPrint(String path, Class<T> type){
		try{
			T resp = get(path, type);
			System.out.println(TestDTOFormatter.format(resp));
			return resp;
		}catch(RuntimeException e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public static <T extends GenericDTO> List<T> getListAndPrint(String path, TypeToken<List<T>> type){
		try{
			List<T> resp = getList(path, type);
			for (GenericDTO dto : resp) {
				System.out.println(TestDTOFormatter.format(dto));
			}
			return resp;
		}catch(RuntimeException e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	
}