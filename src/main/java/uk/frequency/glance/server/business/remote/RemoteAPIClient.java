package uk.frequency.glance.server.business.remote;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RemoteAPIClient {

	String rootUrl;
	Client client;
	Gson parser;
	
	public RemoteAPIClient(String rootUrl) {
		this.rootUrl = rootUrl;
		this.parser = new Gson();
		this.client = Client.create();
		client.setFollowRedirects(true);
	}

	public String getText(String path) {
		WebResource resource = client.resource(rootUrl + path);
		ClientResponse response = resource
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		if(response.getStatus() == 200){
			return response.getEntity(String.class);
		}else{
			throw new RuntimeException(response.getStatus() + " " + response.getEntity(String.class));
		}
	}

	public <T> T getJson(String path, Class<T> clazz) {
		WebResource resource = client.resource(rootUrl + path);
		ClientResponse response = resource
				.accept(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		if(response.getStatus() == 200){
			String json =  response.getEntity(String.class);
			return parser.fromJson(json, clazz);
		}else{
			throw new RuntimeException(response.getStatus() + " " + response.getEntity(String.class));
		}
	}
	
	public Image getImage(String path) {
		WebResource resource = client.resource(rootUrl + path);
		ClientResponse response = resource
				.accept(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		if(response.getStatus() == 200){
			InputStream in =  response.getEntity(InputStream.class);
			try {
				return ImageIO.read(in);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}else{
			throw new RuntimeException(response.getStatus() + " " + response.getEntity(String.class));
		}
	}

}
