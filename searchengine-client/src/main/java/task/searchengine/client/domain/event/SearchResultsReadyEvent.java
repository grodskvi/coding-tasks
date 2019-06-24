package task.searchengine.client.domain.event;

import task.searchengine.client.domain.DocumentKey;
import task.searchengine.client.domain.SearchQuery;

import java.util.List;

public class SearchResultsReadyEvent {
    private SearchQuery searchQuery;
    private List<DocumentKey> documentKeys;

    public SearchResultsReadyEvent(SearchQuery searchQuery, List<DocumentKey> documentKeys) {
        this.searchQuery = searchQuery;
        this.documentKeys = documentKeys;
    }

    public List<DocumentKey> getDocumentKeys() {
        return documentKeys;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }
}
