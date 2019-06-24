package task.searchengine.client;

import org.apache.commons.io.IOUtils;
import task.searchengine.client.domain.Document;
import task.searchengine.client.domain.DocumentKey;
import task.searchengine.client.domain.SearchQuery;
import task.searchengine.client.domain.event.*;
import task.searchengine.client.domain.exception.DocumentProcessingException;
import task.searchengine.client.domain.exception.DocumentRetrievalException;
import task.searchengine.client.domain.exception.SearchDocumentsException;
import task.searchengine.client.repository.DocumentRepository;
import task.searchengine.client.service.EventReporter;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SearchEngineClient {

    private final DocumentRepository documentRepository;
    private final EventReporter eventReporter;

    public SearchEngineClient(DocumentRepository documentRepository, EventReporter eventReporter) {
        this.documentRepository = documentRepository;
        this.eventReporter = eventReporter;
    }


    public void addDocument(String documentKey, String filePath) throws IOException {
        DocumentKey key = DocumentKey.documentKey(documentKey);
        List<String> lines = IOUtils.readLines(new FileReader(filePath));
        String content = String.join("\n", lines);
        Document document = new Document(key, content);

        try {
            DocumentKey addedDocumentKey = documentRepository.addDocument(document);

            AddedDocumentEvent addedDocumentEvent = new AddedDocumentEvent(addedDocumentKey);
            eventReporter.reportAddedDocument(addedDocumentEvent);
        } catch (DocumentProcessingException e) {
            AddingDocumentFailureEvent event = new AddingDocumentFailureEvent(key, e.getMessage());
            eventReporter.reportAddingDocumentFailure(event);
        }
    }

    public void getDocument(String documentKey) {
        DocumentKey key = DocumentKey.documentKey(documentKey);
        try {
            Document document = documentRepository.getDocument(key);
            DownloadedDocumentEvent event = new DownloadedDocumentEvent(document);
            eventReporter.reportDowloadedDocument(event);
        } catch (DocumentRetrievalException e) {
            RetrieveDocumentFailureEvent event = new RetrieveDocumentFailureEvent(key, e.getMessage());
            eventReporter.reportRetrievingDocumentFailure(event);
        }

    }

    public void searchDocuments(List<String> keywords) throws IOException {
        SearchQuery searchQuery = new SearchQuery(keywords);
        try {
            List<DocumentKey> documentKeys = documentRepository.searchDocuments(searchQuery);
            SearchResultsReadyEvent event = new SearchResultsReadyEvent(searchQuery, documentKeys);
            eventReporter.reportSearchResultsReady(event);
        } catch (SearchDocumentsException e) {
            SearchFailureEvent event = new SearchFailureEvent(searchQuery, e.getMessage());
            eventReporter.reportSearchFailure(event);
        }
    }
}
