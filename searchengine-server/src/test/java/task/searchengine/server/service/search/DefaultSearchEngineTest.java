package task.searchengine.server.service.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.IndexedDocument;
import task.searchengine.server.repository.IndexRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSearchEngineTest {

    @Mock
    private IndexBuilder indexBuilder;
    @Mock
    private IndexRepository indexRepository;

    @InjectMocks
    private DefaultSearchEngine searchEngine;

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
}