package task.searchengine.client.domain.exception;

public class DocumentProcessingException extends Exception {
    public DocumentProcessingException(String message) {
        super(message);
    }

    public DocumentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
