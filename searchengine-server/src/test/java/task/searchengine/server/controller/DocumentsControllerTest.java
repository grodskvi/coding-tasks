package task.searchengine.server.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import task.searchengine.server.application.SearchEngineServerApplicationConfiguration;
import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.SearchQuery;
import task.searchengine.server.domain.exception.DocumentNotFoundException;
import task.searchengine.server.domain.exception.DocumentProcessingException;
import task.searchengine.server.service.document.DocumentService;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static task.searchengine.server.domain.Document.aDocumentWithKey;


@RunWith(SpringRunner.class)
@WebMvcTest(DocumentsController.class)
@ContextConfiguration(classes = {SearchEngineServerApplicationConfiguration.class})
public class DocumentsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Test
    public void retrievesDocumentByKey() throws Exception {
        Document document = aDocumentWithKey("key", "document text");
        when(documentService.getDocument(document.getDocumentKey())).thenReturn(document);
        mockMvc
                .perform(get("/document/key"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentKey.key").value(is("key")))
                .andExpect(jsonPath("$.text").value(is("document text")));
    }

    @Test
    public void reportsNotFoundDocuments() throws Exception {
        DocumentKey documentKey = new DocumentKey("key");
        when(documentService.getDocument(documentKey)).thenThrow(new DocumentNotFoundException("error message"));
        mockMvc
                .perform(get("/document/key"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value(is("error message")));
    }

    @Test
    public void addsDocument() throws Exception {
        String documentText = "Some <> text";
        when(documentService.addDocument(aDocumentWithKey("key", documentText)))
                .thenReturn(new DocumentKey("key"));

        mockMvc
                .perform(
                        post("/document/key").
                                content(documentText)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value(is("key")));
        verify(documentService).addDocument(aDocumentWithKey("key", documentText));
    }

    @Test
    public void reportsFailureToProcessDocument() throws Exception {
        String documentText = "Some <> text";
        String errorMessage = "error in document processing";
        when(documentService.addDocument(aDocumentWithKey("key", documentText)))
                .thenThrow(new DocumentProcessingException(errorMessage));
        mockMvc
                .perform(
                        post("/document/key").
                                content(documentText)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(is(errorMessage)));
        verify(documentService).addDocument(aDocumentWithKey("key", documentText));
    }

    @Test
    public void searchesDocumentsAccordingToParameters() throws Exception {
        List<String> tokens = asList("a", "B", "1c");
        SearchQuery searchQuery = new SearchQuery(tokens);
        List<DocumentKey> matchedDocuments = asList(new DocumentKey("key1"), new DocumentKey("key2"));
        when(documentService.search(searchQuery)).thenReturn(matchedDocuments);
        mockMvc
                .perform(get("/document/search?token=a&token=B&token=1c"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].key").value(hasItems("key1", "key2")));
    }

    @Test
    public void searchesDocumentsOnEmptyParametersList() throws Exception {
        SearchQuery searchQuery = new SearchQuery(emptyList());
        when(documentService.search(any())).thenReturn(null);
        when(documentService.search(searchQuery)).thenReturn(emptyList());
        mockMvc
                .perform(get("/document/search?token="))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].key").isEmpty());
    }
}