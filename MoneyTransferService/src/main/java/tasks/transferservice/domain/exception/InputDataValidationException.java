package tasks.transferservice.domain.exception;

public class InputDataValidationException extends RuntimeException {

    private String requestId;
    private String requestType;

    public InputDataValidationException(String message, String requestId, String requestType) {
        super(message);
        this.requestId = requestId;
        this.requestType = requestType;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequestType() {
        return requestType;
    }
}
