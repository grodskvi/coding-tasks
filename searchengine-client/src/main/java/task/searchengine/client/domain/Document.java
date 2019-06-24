package task.searchengine.client.domain;

public class Document {
    private DocumentKey documentKey;
    private String text;

    public Document() {}

    public Document(DocumentKey documentKey, String text) {
        this.documentKey = documentKey;
        this.text = text;
    }

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
