package task.searchengine.server.domain;

import java.util.List;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchQuery that = (SearchQuery) o;
        return Objects.equals(tokens, that.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokens);
    }

    @Override
    public String toString() {
        return "SearchQuery{" +
                "tokens=" + tokens +
                '}';
    }
}
