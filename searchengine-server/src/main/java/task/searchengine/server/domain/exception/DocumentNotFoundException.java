package task.searchengine.server.domain.exception;

public class DocumentNotFoundException extends Exception {
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
