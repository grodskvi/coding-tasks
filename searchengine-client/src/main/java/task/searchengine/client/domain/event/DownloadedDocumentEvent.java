package task.searchengine.client.domain.event;

import task.searchengine.client.domain.Document;
import task.searchengine.client.domain.DocumentKey;

public class DownloadedDocumentEvent {

    private Document downloadeDocument;

    public DownloadedDocumentEvent(Document downloadeDocument) {
        this.downloadeDocument = downloadeDocument;
    }

    public DocumentKey getDocumentKey() {
        return downloadeDocument.getDocumentKey();
    }

    public String getText() {
        return downloadeDocument.getText();
    }
}
