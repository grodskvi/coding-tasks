package task.searchengine.server.domain;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public class SearchQuery {

    private final List<Token> tokens;

    public SearchQuery(List<Token> tokens) {
        this.tokens = unmodifiableList(tokens);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return "SearchQuery{" +
                "tokens=" + tokens +
                '}';
    }
}
