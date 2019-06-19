package task.searchengine.server.repository;

import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;

import java.util.Optional;

public interface DocumentRepository {
    Optional<Document> findBy(DocumentKey key);
    boolean contains(DocumentKey key);
    void save(Document document);
}
