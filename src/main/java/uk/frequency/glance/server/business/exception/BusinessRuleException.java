package uk.frequency.glance.server.business.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public abstract class BusinessRuleException extends WebApplicationException{

	public BusinessRuleException(Response response) {
		super(response);
	}

}
