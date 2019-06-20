package task.searchengine.server.service.document;

import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.SearchQuery;
import task.searchengine.server.domain.exception.DocumentNotFoundException;
import task.searchengine.server.domain.exception.DocumentProcessingException;

import java.util.List;

public interface DocumentService {

    DocumentKey addDocument(Document document) throws DocumentProcessingException;

    Document getDocument(DocumentKey documentKey) throws DocumentNotFoundException;

    List<DocumentKey> search(SearchQuery searchQuery);
}
