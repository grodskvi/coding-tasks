package task.searchengine.client.domain.event;

import task.searchengine.client.domain.DocumentKey;

public class RetrieveDocumentFailureEvent {
    private DocumentKey documentKey;
    private String errorMessage;

    public RetrieveDocumentFailureEvent(DocumentKey documentKey, String errorMessage) {
        this.documentKey = documentKey;
        this.errorMessage = errorMessage;
    }

    public DocumentKey getDocumentKey() {
        return documentKey;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
