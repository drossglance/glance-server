package uk.frequency.glance.testclient;

import java.lang.reflect.Type;

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

	@Override
	public JsonElement serialize(T src, Type type, JsonSerializationContext context) {
		JsonElement elm = gson.toJsonTree(src);
		String className = src.getClass().getName();
		elm.getAsJsonObject().addProperty(CLASS_PROPERTY, className);
		return elm;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		try {
			JsonObject jsonObj = json.getAsJsonObject();
			JsonElement jsonElm = jsonObj.get(CLASS_PROPERTY);
			Class<T> subclass = (Class<T>)Class.forName(jsonElm.getAsString());
			T obj = gson.fromJson(json, subclass);
			return obj;
		} catch (ClassNotFoundException e) {
			throw new JsonParseException(e);
		} catch (ClassCastException e){
			throw new JsonParseException(e);
		}
	}
	
}
