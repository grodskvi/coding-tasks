package task.searchengine.server.repository;

import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;

public interface DocumentRepository {
    Document findBy(DocumentKey key);
    boolean contains(DocumentKey key);
    void save(Document document);
}
