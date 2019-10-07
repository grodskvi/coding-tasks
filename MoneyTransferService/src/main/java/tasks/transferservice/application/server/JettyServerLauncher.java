package tasks.transferservice.application.server;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Feature;

public class JettyServerLauncher {

    private static final Logger LOG = LoggerFactory.getLogger(JettyServerLauncher.class);

    private String applicationName;
    private int port;
    private ResourceConfig resourceConfig = new ResourceConfig();

    private JettyServerLauncher(int port) {
        this.port = port;
    }

    public static JettyServerLauncher aServerRunningOnPort(int port) {

        return new JettyServerLauncher(port);
    }

    public JettyServerLauncher withApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public JettyServerLauncher withResourcePackage(String resourcePackage) {
        resourceConfig.packages(resourcePackage);
        return this;
    }

    public JettyServerLauncher withFeature(Class<? extends Feature> feature) {
        resourceConfig.register(feature);
        return this;
    }

    public JettyServerLauncher withConfiguration(DynamicConfiguration configuration) {
        resourceConfig.register(configuration);
        return this;
    }

    public JettyServer start() throws Exception {
        JettyServer server = new JettyServer(applicationName, port, resourceConfig);
        server.start();
        LOG.info("Started server {} on port {}", applicationName, port);
        return server;
    }

}
