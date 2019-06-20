package task.searchengine.server.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SearchEngineServerApplication {
    private ConfigurableApplicationContext applicationContext;

    public void start(String... args) {
        if(applicationContext != null) {
            throw new RuntimeException("Search Engine application is already running");
        }
        Class<?>[] sources = {
                SearchEngineServerApplication.class,
                SearchEngineServerApplicationConfiguration.class
        };
        applicationContext = SpringApplication.run(sources, args);
    }

    public void stop() {
        if(applicationContext == null) {
            throw new RuntimeException("Search Engine application is already stopped");
        }
        SpringApplication.exit(applicationContext);
        applicationContext = null;
    }

    public static void main(String[] args) {
        new SearchEngineServerApplication().start(args);
    }
}
