package uk.frequency.glance.server.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

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

/**
 * @author Victor
 * Makes JAX-RS use GSON instead of Jackson (the built-in library for JSON parsing). 
 * JAX-RS finds this class by the @Provider annotation through a classpath discovery mechanism.
 * http://eclipsesource.com/blogs/2012/11/02/integrating-gson-into-a-jax-rs-based-application/
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class JsonMessageBodyHandler implements MessageBodyWriter<Object>, MessageBodyReader<Object> {

	private static final String UTF_8 = "UTF-8";

	private Gson gson;

	private Gson getGson() {
		if (gson == null) {
			gson = buildGson();
		}
		return gson;
	}
	
	public static Gson buildGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(TraceDTO.class, JsonHierarchyTypeAdapter.getTraceInstance());
		builder.registerTypeAdapter(PositionTraceDTO.class, JsonHierarchyTypeAdapter.getTraceInstance());
		builder.registerTypeAdapter(ListenTraceDTO.class, JsonHierarchyTypeAdapter.getTraceInstance());
		builder.registerTypeAdapter(EventDTO.class, JsonHierarchyTypeAdapter.getEventInstance());
		builder.registerTypeAdapter(StayEventDTO.class, JsonHierarchyTypeAdapter.getEventInstance());
		builder.registerTypeAdapter(MoveEventDTO.class, JsonHierarchyTypeAdapter.getEventInstance());
		builder.registerTypeAdapter(TellEventDTO.class, JsonHierarchyTypeAdapter.getEventInstance());
		builder.registerTypeAdapter(ListenEventDTO.class, JsonHierarchyTypeAdapter.getEventInstance());
		return builder.create();
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException {
		InputStreamReader streamReader = new InputStreamReader(entityStream, UTF_8);
		try {
			Type jsonType;
			if (type.equals(genericType)) {
				jsonType = type;
			} else {
				jsonType = genericType;
			}
			return getGson().fromJson(streamReader, jsonType);
		} finally {
			streamReader.close();
		}
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8);
		try {
			Type jsonType;
			if (type.equals(genericType)) {
				jsonType = type;
			} else {
				jsonType = genericType;
			}
			getGson().toJson(object, jsonType, writer);
		} finally {
			writer.close();
		}
	}

	@Override
	public long getSize(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}
}