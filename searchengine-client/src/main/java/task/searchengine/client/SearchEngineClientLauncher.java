package task.searchengine.client;

import task.searchengine.client.repository.DocumentRepository;
import task.searchengine.client.repository.HttpDocumentRepository;
import task.searchengine.client.service.ConsoleEventReporter;
import task.searchengine.client.service.EventReporter;

import task.searchengine.client.domain.event.ClientStartedEvent;

import java.io.IOException;
import java.io.PrintWriter;

public class SearchEngineClientLauncher {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;
        int port = args.length > 1 ? Integer.valueOf(args[1]) : DEFAULT_PORT;

        PrintWriter writer = new PrintWriter(System.out);

        String serverUrl = "http://" + host + ":" + port;
        DocumentRepository documentRepository = new HttpDocumentRepository(serverUrl);
        EventReporter eventReporter = new ConsoleEventReporter(writer);
        SearchEngineClient client = new SearchEngineClient(documentRepository, eventReporter);
        ConsoleCommandDispatcher commandDispatcher = new ConsoleCommandDispatcher(client, eventReporter);

        eventReporter.reportClientStarted(new ClientStartedEvent(serverUrl));
        commandDispatcher.run();

        writer.flush();
        writer.close();
    }
}
