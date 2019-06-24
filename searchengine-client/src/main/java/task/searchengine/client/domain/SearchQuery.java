package task.searchengine.client.domain;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public class SearchQuery {
    private final List<String> keywords;

    public SearchQuery(List<String> keywords) {
        this.keywords = unmodifiableList(keywords);
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
