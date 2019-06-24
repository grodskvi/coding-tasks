package task.searchengine.client.domain.exception;

public class DocumentRetrievalException extends Exception {
    public DocumentRetrievalException(String message) {
        super(message);
    }

    public DocumentRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
