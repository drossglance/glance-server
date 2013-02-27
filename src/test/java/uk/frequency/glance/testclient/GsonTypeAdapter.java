package uk.frequency.glance.testclient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import uk.frequency.glance.server.transfer.event.EventDTO;
import uk.frequency.glance.server.transfer.event.ListenEventDTO;
import uk.frequency.glance.server.transfer.event.MoveEventDTO;
import uk.frequency.glance.server.transfer.event.StayEventDTO;
import uk.frequency.glance.server.transfer.event.TellEventDTO;
import uk.frequency.glance.server.transfer.trace.ListenTraceDTO;
import uk.frequency.glance.server.transfer.trace.PositionTraceDTO;
import uk.frequency.glance.server.transfer.trace.TraceDTO;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonTypeAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

	final static String CLASS_PROPERTY = "@class";
	private Gson gson = new Gson();
	private Map<String, Class<? extends T>> map;
	private Map<Class<? extends T>, String> unmap;

	public GsonTypeAdapter(Map<String, Class<? extends T>> map, Map<Class<? extends T>, String> backMap) {
		super();
		this.map = map;
		this.unmap = backMap;
	}

	@Override
	public JsonElement serialize(T src, Type type, JsonSerializationContext context) {
		JsonElement elm = gson.toJsonTree(src);
		String classValue = unmap.get(src.getClass());
		elm.getAsJsonObject().addProperty(CLASS_PROPERTY, classValue);
		return elm;
	}

	@Override
	public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObj = json.getAsJsonObject();
		JsonElement jsonElm = jsonObj.get(CLASS_PROPERTY);
		Class<? extends T> subclass = map.get(jsonElm.getAsString());
		T obj = gson.fromJson(json, subclass);
		return obj;
	}

	public static GsonTypeAdapter<EventDTO> getEventInstance() {
		Map<String, Class<? extends EventDTO>> map = new HashMap<String, Class<? extends EventDTO>>();
		map.put("TELL_EVENT", TellEventDTO.class);
		map.put("STAY_EVENT", StayEventDTO.class);
		map.put("MOVE_EVENT", MoveEventDTO.class);
		map.put("LISTEN_EVENT", ListenEventDTO.class);
		Map<Class<? extends EventDTO>, String> unmap = new HashMap<Class<? extends EventDTO>, String>();
		unmap.put(TellEventDTO.class, "TELL_EVENT");
		unmap.put(StayEventDTO.class, "STAY_EVENT");
		unmap.put(MoveEventDTO.class, "MOVE_EVENT");
		unmap.put(ListenEventDTO.class, "LISTEN_EVENT");
		return new GsonTypeAdapter<EventDTO>(map, unmap);
	}

	public static GsonTypeAdapter<TraceDTO> getTraceInstance() {
		Map<String, Class<? extends TraceDTO>> map = new HashMap<String, Class<? extends TraceDTO>>();
		map.put("POSITION_TRACE", PositionTraceDTO.class);
		map.put("LISTEN_TRACE", ListenTraceDTO.class);
		Map<Class<? extends TraceDTO>, String> unmap = new HashMap<Class<? extends TraceDTO>, String>();
		unmap.put(PositionTraceDTO.class, "POSITION_TRACE");
		unmap.put(ListenTraceDTO.class, "LISTEN_TRACE");
		return new GsonTypeAdapter<TraceDTO>(map, unmap);
	}

}
