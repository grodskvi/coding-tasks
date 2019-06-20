package task.searchengine.server.controller.dto;

public class ErrorResponse {
    private String errorMessage;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
