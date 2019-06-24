package task.searchengine.client.domain.exception;


public class SearchDocumentsException extends Exception {
    public SearchDocumentsException(String message) {
        super(message);
    }

    public SearchDocumentsException(String message, Throwable cause) {
        super(message, cause);
    }
}
