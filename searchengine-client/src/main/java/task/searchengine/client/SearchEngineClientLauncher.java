package task.searchengine.client;

import java.io.IOException;
import java.io.PrintWriter;

public class SearchEngineClientLauncher {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;
        int port = args.length > 1 ? Integer.valueOf(args[1]) : DEFAULT_PORT;

        PrintWriter writer = new PrintWriter(System.out);

        SearchEngineClient client = new SearchEngineClient(host, port, writer);

        ConsoleCommandDispatcher commandDispatcher = new ConsoleCommandDispatcher(client, writer);
        commandDispatcher.run();

        writer.flush();
        writer.close();
    }
}
