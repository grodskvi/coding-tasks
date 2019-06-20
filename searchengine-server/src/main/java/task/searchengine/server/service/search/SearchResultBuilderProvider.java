package task.searchengine.server.service.search;

import org.springframework.stereotype.Component;

@Component
public class SearchResultBuilderProvider {

    public SearchResultBuilder getSearchResultBuilder() {
        return new IntersectingSearchResultBuilder();
    }
}
