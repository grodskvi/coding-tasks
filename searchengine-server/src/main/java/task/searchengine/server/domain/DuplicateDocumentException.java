package task.searchengine.server.domain;

public class DuplicateDocumentException extends RuntimeException {

    public DuplicateDocumentException(String message) {
        super(message);
    }
}
