package uk.frequency.glance.server.business.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@SuppressWarnings("serial")
public abstract class InputFormatException extends BusinessRuleException{

	public InputFormatException(String msg){
		super(Response.status(Status.BAD_REQUEST).entity(msg).build());
	}
	
}
