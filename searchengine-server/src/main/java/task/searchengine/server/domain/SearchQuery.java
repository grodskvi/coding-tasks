package task.searchengine.server.domain;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public class SearchQuery {

    private final List<String> tokens;

    public SearchQuery(List<String> tokens) {
        this.tokens = unmodifiableList(tokens);
    }

    public List<String> getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return "SearchQuery{" +
                "tokens=" + tokens +
                '}';
    }
}
