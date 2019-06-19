package task.searchengine.server.domain.exception;

public class DuplicateDocumentException extends DocumentProcessingException {

    public DuplicateDocumentException(String message) {
        super(message);
    }
}
