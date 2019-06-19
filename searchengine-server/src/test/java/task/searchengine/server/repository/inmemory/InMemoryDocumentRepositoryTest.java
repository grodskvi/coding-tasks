package task.searchengine.server.repository.inmemory;

import org.junit.Test;
import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static task.searchengine.server.domain.Document.aDocumentWithKey;
import static task.searchengine.server.repository.inmemory.InMemoryDocumentRepository.restore;

public class InMemoryDocumentRepositoryTest {

    private InMemoryDocumentRepository documentRepository;

    @Test
    public void isEmptyOnStartup() {
        documentRepository = new InMemoryDocumentRepository();
        assertThat(documentRepository.getStoredDocumentsSize()).isZero();
    }

    @Test
    public void countsStoredDocuments() {
        documentRepository = restore(prestoredDocuments());
        assertThat(documentRepository.getStoredDocumentsSize()).isEqualTo(2);
    }

    @Test
    public void findsDocumentByKey() {
        documentRepository = restore(prestoredDocuments());
        DocumentKey key = new DocumentKey("key");
        Document expectedDocument = new Document(key, "text");
        assertThat(documentRepository.findBy(key)).hasValue(expectedDocument);
    }

    @Test
    public void doesNotFindDocumentByMissingKey() {
        documentRepository = restore(prestoredDocuments());
        DocumentKey key = new DocumentKey("wrong key");
        assertThat(documentRepository.findBy(key)).isEmpty();
    }

    @Test
    public void addsDocument() {
        documentRepository = new InMemoryDocumentRepository();
        Document document = aDocumentWithKey("key", "text");
        documentRepository.save(document);
        assertThat(documentRepository.getStoredDocumentsSize()).isEqualTo(1);
    }

    @Test
    public void findsStoredDocument() {
        documentRepository = new InMemoryDocumentRepository();
        Document document = aDocumentWithKey("key", "text");
        documentRepository.save(document);


        Optional<Document> storedDocument = documentRepository.findBy(document.getDocumentKey());
        assertThat(storedDocument).hasValue(document);
    }

    @Test
    public void overridesStoredDocument() {
        documentRepository = restore(prestoredDocuments());
        Document document = aDocumentWithKey("key", "another text");
        documentRepository.save(document);


        Optional<Document> storedDocument = documentRepository.findBy(document.getDocumentKey());
        assertThat(storedDocument).hasValue(document);
    }


    @Test
    public void indicatesWhetherDocumentKeyExists() {
        documentRepository = InMemoryDocumentRepository.restore(prestoredDocuments());
        DocumentKey key = new DocumentKey("key");
        assertThat(documentRepository.contains(key)).isTrue();
    }

    @Test
    public void indicatesMissingDocumentKey() {
        documentRepository = InMemoryDocumentRepository.restore(prestoredDocuments());
        DocumentKey key = new DocumentKey("missing key");
        assertThat(documentRepository.contains(key)).isFalse();
    }


    private Map<DocumentKey, Document> prestoredDocuments() {
        Map<DocumentKey, Document> documents = new HashMap<>();
        Document document = aDocumentWithKey("key", "text");
        Document anotherDocument = aDocumentWithKey("another", "another text");
        documents.put(document.getDocumentKey(), document);
        documents.put(anotherDocument.getDocumentKey(), anotherDocument);

        return documents;
    }
}