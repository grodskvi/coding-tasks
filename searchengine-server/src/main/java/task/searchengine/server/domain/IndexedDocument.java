package task.searchengine.server.domain;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class IndexedDocument {
    private final DocumentKey documentKey;
    private final Set<Token> tokens;

    public IndexedDocument(DocumentKey documentKey, Set<Token> tokens) {
        this.documentKey = documentKey;
        this.tokens = unmodifiableSet(tokens);
    }

    public DocumentKey getDocumentKey() {
        return documentKey;
    }

    public Set<Token> getTokens() {
        return tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexedDocument that = (IndexedDocument) o;
        return Objects.equals(documentKey, that.documentKey) &&
                Objects.equals(tokens, that.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentKey, tokens);
    }

    @Override
    public String toString() {
        return "IndexedDocument{" +
                "documentKey=" + documentKey +
                ", tokens=" + tokens +
                '}';
    }
}
