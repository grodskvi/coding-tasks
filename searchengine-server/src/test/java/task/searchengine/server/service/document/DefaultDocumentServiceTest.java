package task.searchengine.server.service.document;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.SearchQuery;
import task.searchengine.server.domain.exception.DocumentNotFoundException;
import task.searchengine.server.domain.exception.DuplicateDocumentException;
import task.searchengine.server.repository.DocumentRepository;
import task.searchengine.server.service.document.DefaultDocumentService;
import task.searchengine.server.service.search.SearchEngine;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDocumentServiceTest {

    private static final DocumentKey KEY = new DocumentKey("key");
    private static final DocumentKey ANOTHER_KEY = new DocumentKey("another key");

    private static final Document DOCUMENT = new Document(KEY, "text");

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private SearchEngine searchEngine;

    @InjectMocks
    private DefaultDocumentService documentService;

    @Test
    public void retrievesDocumentByKey() throws DocumentNotFoundException {
        when(documentRepository.findBy(KEY)).thenReturn(Optional.of(DOCUMENT));

        Document retrievedDocument = documentService.getDocument(KEY);
        assertThat(retrievedDocument).isEqualTo(DOCUMENT);
    }

    @Test
    public void savesDocuments() throws DuplicateDocumentException {
        documentService.addDocument(DOCUMENT);
        verify(documentRepository).save(DOCUMENT);
    }

    @Test
    public void indexesDocumentWhileAdding() throws DuplicateDocumentException {
        documentService.addDocument(DOCUMENT);
        verify(searchEngine).index(DOCUMENT);
    }

    @Test
    public void searchesDocumentsByQuery() {
        SearchQuery searchQuery = mock(SearchQuery.class);
        when(searchEngine.search(searchQuery)).thenReturn(asList(KEY, ANOTHER_KEY));

        assertThat(documentService.search(searchQuery)).containsExactly(KEY, ANOTHER_KEY);
    }
}