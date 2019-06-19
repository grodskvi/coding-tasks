package task.searchengine.server.repository.inmemory;

import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.repository.DocumentRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryDocumentRepository implements DocumentRepository {

    private final Map<DocumentKey, Document> documents;

    public InMemoryDocumentRepository() {
        this(new HashMap<>());
    }

    private InMemoryDocumentRepository(Map<DocumentKey, Document> prestoredDocuments) {
        documents = new HashMap<>(prestoredDocuments);
    }

    @Override
    public Optional<Document> findBy(DocumentKey key) {
        return Optional.ofNullable(documents.get(key));
    }

    @Override
    public boolean contains(DocumentKey key) {
        return documents.containsKey(key);
    }

    @Override
    public void save(Document document) {
        DocumentKey documentKey = document.getDocumentKey();
        documents.put(documentKey, document);
    }

    int getStoredDocumentsSize() {
        return documents.size();
    }

    static InMemoryDocumentRepository restore(Map<DocumentKey, Document> restoredDocuments) {
        return new InMemoryDocumentRepository(restoredDocuments);
    }
}
