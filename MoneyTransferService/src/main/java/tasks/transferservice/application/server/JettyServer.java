package tasks.transferservice.application.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public class JettyServer {
    private static final Logger LOG = LoggerFactory.getLogger(JettyServer.class);

    private String applicationName;
    private int port;
    private ResourceConfig resourceConfig;
    private Server server;

    public JettyServer(String applicationName, int port, ResourceConfig resourceConfig) {
        this.applicationName = applicationName;
        this.port = port;
        this.resourceConfig = resourceConfig;
    }

    public void start() throws Exception {
        if (server != null) {
            throw new RuntimeException("Server is already running");
        }
        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder jerseyServlet = new ServletHolder(servletContainer);

        server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(jerseyServlet, format("/%s/*", applicationName));
        server.start();
    }

    public void stop() {
        if (server == null) {
            throw new RuntimeException("Server was not started");
        }
        try {
            server.stop();
            server.destroy();
            server = null;
        } catch (Exception e) {
            LOG.error("Failed to stop server '{}'", applicationName, e);
        }
    }

    public void join() throws InterruptedException {
        server.join();
    }
}
