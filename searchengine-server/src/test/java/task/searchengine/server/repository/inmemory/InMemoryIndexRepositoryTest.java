package task.searchengine.server.repository.inmemory;

import org.junit.Before;
import org.junit.Test;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.IndexedDocument;
import task.searchengine.server.domain.Token;
import task.searchengine.utils.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static task.searchengine.server.repository.inmemory.InMemoryIndexRepository.restore;
import static task.searchengine.utils.CollectionUtils.hashSet;

public class InMemoryIndexRepositoryTest {

    private static final Token A_TOKEN = new Token("a");
    private static final Token B_TOKEN = new Token("b");
    private static final DocumentKey DOCUMENT = new DocumentKey("document");

    private InMemoryIndexRepository indexRepository;

    @Before
    public void setUp() {
        indexRepository = restore(prestoredIndex());
    }

    @Test
    public void findsDocumentKeyByToken() {
        indexRepository = restore(prestoredIndex());
        assertThat(indexRepository.findDocuments(A_TOKEN)).containsExactly(DOCUMENT);
    }

    @Test
    public void doesNotFindDocumentKeyByNotIndexedToken() {
        indexRepository = restore(prestoredIndex());
        assertThat(indexRepository.findDocuments(new Token("not indexed"))).isEmpty();
    }

    @Test
    public void addsToDocumentIndex() {
        indexRepository = restore(prestoredIndex());

        DocumentKey anotherDocument = new DocumentKey("another document");
        IndexedDocument indexedDocument = new IndexedDocument(anotherDocument, hashSet(B_TOKEN));

        indexRepository.save(indexedDocument);
        assertThat(indexRepository.findDocuments(B_TOKEN)).containsExactly(anotherDocument);
    }

    @Test
    public void mergesToDocumentsInIndex() {
        indexRepository = restore(prestoredIndex());

        DocumentKey anotherDocument = new DocumentKey("another document");
        IndexedDocument indexedDocument = new IndexedDocument(anotherDocument, hashSet(A_TOKEN));

        indexRepository.save(indexedDocument);

        assertThat(indexRepository.findDocuments(A_TOKEN))
                .hasSize(2)
                .containsOnly(anotherDocument, DOCUMENT);
    }

    private Map<Token, Set<DocumentKey>> prestoredIndex() {
        Map<Token, Set<DocumentKey>> index = new HashMap<>();

        index.put(A_TOKEN, hashSet(DOCUMENT));
        return index;
    }

}