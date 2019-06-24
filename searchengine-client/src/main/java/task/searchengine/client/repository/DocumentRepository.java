package task.searchengine.client.repository;

import task.searchengine.client.domain.Document;
import task.searchengine.client.domain.DocumentKey;
import task.searchengine.client.domain.SearchQuery;
import task.searchengine.client.domain.exception.DocumentProcessingException;
import task.searchengine.client.domain.exception.DocumentRetrievalException;
import task.searchengine.client.domain.exception.SearchDocumentsException;

import java.util.List;

public interface DocumentRepository {
    Document getDocument(DocumentKey documentKey) throws DocumentRetrievalException;
    DocumentKey addDocument(Document document) throws DocumentProcessingException;
    List<DocumentKey> searchDocuments(SearchQuery query) throws SearchDocumentsException;
}
