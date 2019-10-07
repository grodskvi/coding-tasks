package tasks.transferservice.application;


import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.transferservice.application.server.JettyServer;
import tasks.transferservice.application.server.JettyServerLauncher;

public class TransferServiceApplication {

    private static final Logger LOG = LoggerFactory.getLogger(TransferServiceApplication.class);

    public static final String APPLICATION_NAME = "transfer-service";
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        JettyServer server = null;
        try {
            server = startServer(DEFAULT_PORT);
            server.join();
        } catch (Exception ex) {
            LOG.error("Error occurred while starting Jetty", ex);
            System.exit(1);
        } finally {
            if (server != null) {
                server.stop();
            }
        }
    }

    public static JettyServer startServer(int port) throws Exception {
        return JettyServerLauncher.aServerRunningOnPort(port)
                .withResourcePackage("tasks.transferservice.rest.resources")
                .withApplicationName(APPLICATION_NAME)
                .withFeature(JacksonFeature.class)
                .withConfiguration(new TransferServiceApplicationConfiguration())
                .start();
    }
}
