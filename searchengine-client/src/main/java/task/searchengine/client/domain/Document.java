package task.searchengine.client.domain;

public class Document {
    private DocumentKey documentKey;
    private String text;

    public DocumentKey getDocumentKey() {
        return documentKey;
    }

    public void setDocumentKey(DocumentKey documentKey) {
        this.documentKey = documentKey;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
