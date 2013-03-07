package uk.frequency.glance.server.business.exception;

@SuppressWarnings("serial")
public class MissingFieldException extends InputFormatException {

	public MissingFieldException(String fieldName) {
		super("Missing field: " + fieldName);
	}
	
}
