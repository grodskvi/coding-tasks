package task.searchengine.client;

import task.searchengine.client.service.EventReporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleCommandDispatcher {

    private SearchEngineClient searchEngineClient;
    private EventReporter eventReporter;


    public ConsoleCommandDispatcher(SearchEngineClient searchEngineClient, EventReporter eventReporter) {
        this.searchEngineClient = searchEngineClient;
        this.eventReporter = eventReporter;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        eventReporter.reportNextOperationAvailability();
        while (true) {
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                break;
            }
            try {
                dispatchCommand(command);
            } catch (IOException e) {
                eventReporter.reportError(e.getMessage());
            }
            eventReporter.reportNextOperationAvailability();
        }
    }

    private void dispatchCommand(String command) throws IOException {
        String[] parsedCommand = command.split(" ");
        switch (parsedCommand[0]) {
            case "add":
                searchEngineClient.addDocument(parsedCommand[1], parsedCommand[2]);
                break;
            case "get":
                searchEngineClient.getDocument(parsedCommand[1]);
                break;
            case "search":
                List<String> keywords = new ArrayList();
                for (int i = 1; i < parsedCommand.length; i++) {
                    keywords.add(parsedCommand[i]);
                }
                searchEngineClient.searchDocuments(keywords);
                break;
            default:
                eventReporter.reportError("Unknown command");
        }
    }
}
