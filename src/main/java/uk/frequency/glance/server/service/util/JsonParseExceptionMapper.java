package uk.frequency.glance.server.service.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.gson.JsonParseException;

/**
 * @author Victor
 * Handles JSON parsing exceptions and sends the client an appropriate HTTP response.
 * http://stackoverflow.com/questions/583973/jax-rs-jersey-how-to-customize-error-handling
 */
@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {

	@Override
	public Response toResponse(JsonParseException exception) {
		return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
	}
	
}