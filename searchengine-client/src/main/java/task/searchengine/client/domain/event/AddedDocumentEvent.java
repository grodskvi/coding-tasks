package task.searchengine.client.domain.event;

import task.searchengine.client.domain.DocumentKey;

public class AddedDocumentEvent {
    private DocumentKey documentKey;

    public AddedDocumentEvent(DocumentKey documentKey) {
        this.documentKey = documentKey;
    }

    public DocumentKey getDocumentKey() {
        return documentKey;
    }
}
