package task.searchengine.server.service.search;

public class SearchResultBuilderProvider {

    public SearchResultBuilder getSearchResultBuilder() {
        return new IntersectingSearchResultBuilder();
    }
}
