package task.searchengine.server.service.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import task.searchengine.server.domain.*;
import task.searchengine.server.repository.IndexRepository;
import task.searchengine.utils.CollectionUtils;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static task.searchengine.server.domain.Document.aDocumentWithKey;
import static task.searchengine.utils.CollectionUtils.hashSet;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSearchEngineTest {

    @Mock
    private IndexBuilder indexBuilder;
    @Mock
    private IndexRepository indexRepository;
    @Mock
    private SearchResultBuilder resultBuilder;
    @Mock
    private SearchResultBuilderProvider resultBuilderProvider;

    @InjectMocks
    private DefaultSearchEngine searchEngine;

    @Before
    public void setUp() {
        when(resultBuilderProvider.getSearchResultBuilder()).thenReturn(resultBuilder);
    }

    @Test
    public void indexesDocument() {
        Document document = mock(Document.class);
        IndexedDocument indexedDocument = mock(IndexedDocument.class);

        when(indexBuilder.buildIndexFor(document)).thenReturn(indexedDocument);

        assertThat(searchEngine.index(document)).isEqualTo(indexedDocument);
    }

    @Test
    public void storesIndexedDocument() {
        Document document = mock(Document.class);
        IndexedDocument indexedDocument = mock(IndexedDocument.class);

        when(indexBuilder.buildIndexFor(document)).thenReturn(indexedDocument);

        searchEngine.index(document);
        verify(indexRepository).save(indexedDocument);
    }

    @Test
    public void searchesDocumentsWithIndex() {
        DocumentKey key1 = new DocumentKey("1");
        DocumentKey key2 = new DocumentKey("2");
        DocumentKey key3 = new DocumentKey("3");

        Token tokenA = new Token("a");
        Token tokenB = new Token("b");

        when(indexRepository.findDocuments(tokenA)).thenReturn(hashSet(key1, key2));
        when(indexRepository.findDocuments(tokenB)).thenReturn(hashSet(key3, key2));

        searchEngine.search(new SearchQuery(asList("a", "b")));

        verify(resultBuilder).acceptSearchResult(tokenA, hashSet(key1, key2));
        verify(resultBuilder).acceptSearchResult(tokenB, hashSet(key3, key2));
        verify(resultBuilder).getResult();
    }

    @Test
    public void lowercasesSearchTokens() {
        DocumentKey key1 = new DocumentKey("1");
        DocumentKey key2 = new DocumentKey("2");

        Token tokenA = new Token("a");

        when(indexRepository.findDocuments(tokenA)).thenReturn(hashSet(key1, key2));

        searchEngine.search(new SearchQuery(asList("A")));

        verify(resultBuilder).acceptSearchResult(tokenA, hashSet(key1, key2));
        verify(resultBuilder).getResult();
    }
}