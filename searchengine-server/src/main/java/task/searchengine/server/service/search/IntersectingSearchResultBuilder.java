package task.searchengine.server.service.search;

import org.springframework.stereotype.Component;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class IntersectingSearchResultBuilder implements SearchResultBuilder {

    private boolean initialized = false;
    private List<DocumentKey> searchResult = new ArrayList<>();

    @Override
    public void acceptSearchResult(Token token, Set<DocumentKey> tokenDocuments) {
        if (tokenDocuments.isEmpty()) {
            searchResult.clear();
        }
        Consumer<Set<DocumentKey>> operation = !initialized
                ? this::initialize
                : this::join;

        operation.accept(tokenDocuments);
    }

    private void join(Set<DocumentKey> tokenDocuments) {
        searchResult = searchResult.stream()
                .filter(tokenDocuments::contains)
                .collect(Collectors.toList());
    }

    private void initialize(Set<DocumentKey> tokenDocuments) {
        initialized = true;
        searchResult.addAll(tokenDocuments);
    }

    @Override
    public List<DocumentKey> getResult() {
        return searchResult;
    }
}
