package task.searchengine.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleCommandDispatcher {
    private SearchEngineClient searchEngineClient;
    private PrintWriter writer;


    public ConsoleCommandDispatcher(SearchEngineClient searchEngineClient, PrintWriter writer) {
        this.searchEngineClient = searchEngineClient;
        this.writer = writer;
    }

    public void run() {
        writer.println("Connected to " + searchEngineClient.getServerUrl());
        printCommandSummary();

        Scanner scanner = new Scanner(System.in);
        printPrompt();
        while (true) {
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                break;
            }
            try {
                dispatchCommand(command);
            } catch (IOException e) {
                writer.println("ERROR: " + e.getMessage());
                writer.flush();
            }
            printPrompt();
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
                System.out.println("Unknown command");
        }
    }

    private void printPrompt() {
        writer.print("> ");
        writer.flush();
    }

    private void printCommandSummary() {
        writer.println("\nAvailable commands:");
        writer.println("add <document key> <absolute file path>");
        writer.println("\t\t\tadds file content to search engine\n");
        writer.println("get <document key>");
        writer.println("\t\t\tget content by document key\n");
        writer.println("search <keyword1> <keyword2> ...");
        writer.println("\t\t\tsearch documents by keyword\n");
        writer.println("exit");
        writer.flush();
    }
}
