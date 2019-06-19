package task.searchengine.server.repository;

import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.IndexedDocument;
import task.searchengine.server.domain.Token;

import java.util.Set;

public interface IndexRepository {
    Set<DocumentKey> findDocuments(Token token);
    void save(IndexedDocument indexedDocument);
}
