package task.searchengine.server.domain;

import java.util.Objects;

public class Document {

    private final DocumentKey documentKey;
    private final String text;

    public Document(DocumentKey documentKey, String text) {
        this.documentKey = documentKey;
        this.text = text;
    }

    public DocumentKey getDocumentKey() {
        return documentKey;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(documentKey, document.documentKey) &&
                Objects.equals(text, document.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentKey, text);
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentKey=" + documentKey +
                ", text='" + text + '\'' +
                '}';
    }

    public static Document aDocumentWithKey(String key, String text) {
        DocumentKey documentKey = new DocumentKey(key);
        return new Document(documentKey, text);
    }
}
