package taxcalculator.dto;

public class ErrorResponse implements Response {
	
	private String message;
	private String exception;

	public ErrorResponse(String message, String exception) {
		this.message = message;
		this.exception = exception;
	}

	public String getException() {
		return exception;
	}
	
	public String getMessage() {
		return message;
	}
}
