package task.searchengine.server.service.document;

import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.SearchQuery;

import java.util.List;

public interface DocumentService {

    void addDocument(Document document);

    Document getDocument(DocumentKey documentKey);

    List<DocumentKey> search(SearchQuery searchQuery);
}
