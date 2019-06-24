package task.searchengine.client.service;

import task.searchengine.client.domain.event.*;

public interface EventReporter {
    void reportAddedDocument(AddedDocumentEvent event);
    void reportAddingDocumentFailure(AddingDocumentFailureEvent event);

    void reportDowloadedDocument(DownloadedDocumentEvent event);
    void reportRetrievingDocumentFailure(RetrieveDocumentFailureEvent event);

    void reportSearchResultsReady(SearchResultsReadyEvent event);
    void reportSearchFailure(SearchFailureEvent event);

    void reportClientStarted(ClientStartedEvent event);

    void reportError(String error);

    void reportNextOperationAvailability();
}
