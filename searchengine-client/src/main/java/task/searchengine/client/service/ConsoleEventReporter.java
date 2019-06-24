package task.searchengine.client.service;

import task.searchengine.client.domain.DocumentKey;
import task.searchengine.client.domain.event.*;

import java.io.PrintWriter;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ConsoleEventReporter implements EventReporter {

    private final PrintWriter writer;

    public ConsoleEventReporter(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void reportAddedDocument(AddedDocumentEvent event) {
        DocumentKey documentKey = event.getDocumentKey();
        write("Added document with key: ", documentKey.getKey());
    }

    @Override
    public void reportAddingDocumentFailure(AddingDocumentFailureEvent event) {
        write("Got error while adding document:");
        write("Error message: " + event.getErrorMessage());
    }

    @Override
    public void reportDowloadedDocument(DownloadedDocumentEvent event) {
        write("Received document by key: ", event.getDocumentKey().getKey());
        write("Text:", "\n", event.getText());
    }

    @Override
    public void reportRetrievingDocumentFailure(RetrieveDocumentFailureEvent event) {
        write("Got error while retrieving document by key ", event.getDocumentKey().getKey());
        write("Error message: " + event.getErrorMessage());
    }

    @Override
    public void reportSearchResultsReady(SearchResultsReadyEvent event) {
        String stringifiedKeywords = String.join("," + event.getSearchQuery().getKeywords());
        write(
                "Found documents by keywords [",
                stringifiedKeywords,
                "]: ");

        List<String> keys = event.getDocumentKeys().stream()
                .map(DocumentKey::getKey)
                .collect(toList());

        String result = keys.isEmpty()
                ? "No documents found"
                : String.join("\n", keys);

        write(result);
    }

    @Override
    public void reportSearchFailure(SearchFailureEvent event) {
        String stringifiedKeywords = String.join("," + event.getSearchQuery().getKeywords());
        write(
                "Got error for search by keywords [",
                stringifiedKeywords, "]: ",
                event.getErrorMessage());
    }

    @Override
    public void reportClientStarted(ClientStartedEvent event) {
        write("Connected to " + event.getServerUrl());
        //Summary
        write("\nAvailable commands:");
        write("add <document key> <absolute file path>");
        write("\t\t\tadds file content to search engine\n");
        write("get <document key>");
        write("\t\t\tget content by document key\n");
        write("search <keyword1> <keyword2> ...");
        write("\t\t\tsearch documents by keyword\n");
        write("exit");
    }

    @Override
    public void reportError(String error) {
        write("ERROR: ", error);
    }

    @Override
    public void reportNextOperationAvailability() {
        writer.print("> ");
        writer.flush();
    }

    private void write(String... messages) {
        String message = String.join("", messages);
        writer.println(message);
        writer.flush();
    }
}
