package task.searchengine.server.service.search;

import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.IndexedDocument;
import task.searchengine.server.domain.SearchQuery;

import java.util.List;

public interface SearchEngine {
    IndexedDocument index(Document document);
    List<DocumentKey> search(SearchQuery searchQuery);
}
