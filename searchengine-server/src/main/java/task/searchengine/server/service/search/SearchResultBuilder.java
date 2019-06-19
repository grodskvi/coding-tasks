package task.searchengine.server.service.search;

import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.Token;

import java.util.List;
import java.util.Set;

public interface SearchResultBuilder {
    void acceptSearchResult(Token token, Set<DocumentKey> tokenDocuments);
    List<DocumentKey> getResult();
}
