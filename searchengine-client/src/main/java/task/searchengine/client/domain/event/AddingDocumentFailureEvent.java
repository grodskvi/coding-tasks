package task.searchengine.client.domain.event;


import task.searchengine.client.domain.DocumentKey;

public class AddingDocumentFailureEvent {
    private DocumentKey documentKey;
    private String errorMessage;

    public AddingDocumentFailureEvent(DocumentKey documentKey, String errorMessage) {
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
