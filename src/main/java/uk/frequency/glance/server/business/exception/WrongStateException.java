package uk.frequency.glance.server.business.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@SuppressWarnings("serial")
public class WrongStateException extends BusinessRuleException {

	public WrongStateException(String msg){
		super(Response.status(Status.CONFLICT).entity(msg).build());
	}
	
}
