package task.searchengine.client.domain.event;

import task.searchengine.client.domain.SearchQuery;

public class SearchFailureEvent {
    private SearchQuery searchQuery;
    private String errorMessage;

    public SearchFailureEvent(SearchQuery searchQuery, String errorMessage) {
        this.searchQuery = searchQuery;
        this.errorMessage = errorMessage;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
