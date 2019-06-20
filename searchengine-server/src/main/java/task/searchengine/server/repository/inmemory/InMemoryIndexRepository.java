package task.searchengine.server.repository.inmemory;

import org.springframework.stereotype.Repository;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.IndexedDocument;
import task.searchengine.server.domain.Token;
import task.searchengine.server.repository.IndexRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

@Repository
public class InMemoryIndexRepository implements IndexRepository {

    private final Map<Token, Set<DocumentKey>> index;

    public InMemoryIndexRepository() {
        this(new HashMap<>());
    }

    private InMemoryIndexRepository(Map<Token, Set<DocumentKey>> prestoredIndex) {
        index = new HashMap<>(prestoredIndex);
    }

    @Override
    public Set<DocumentKey> findDocuments(Token token) {
        Set<DocumentKey> documents = index.getOrDefault(token, emptySet());
        return unmodifiableSet(documents);
    }

    @Override
    public void save(IndexedDocument indexedDocument) {
        DocumentKey documentKey = indexedDocument.getDocumentKey();
        Set<Token> tokens = indexedDocument.getTokens();

        tokens.forEach(token -> indexToken(token, documentKey));
    }

    private void indexToken(Token token, DocumentKey documentKey) {
        index.putIfAbsent(token, new HashSet<>());
        Set<DocumentKey> tokenDocuments = index.get(token);
        tokenDocuments.add(documentKey);
    }

    static InMemoryIndexRepository restore(Map<Token, Set<DocumentKey>> index) {
        return new InMemoryIndexRepository(index);
    }
}
