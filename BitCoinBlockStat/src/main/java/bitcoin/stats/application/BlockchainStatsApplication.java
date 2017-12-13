package bitcoin.stats.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BlockchainStatsApplication {

    private ConfigurableApplicationContext applicationContext;

    public void start(String... args) {
        if(applicationContext != null) {
            throw new RuntimeException("TaxCalculator application is already running");
        }
        applicationContext = SpringApplication.run(new Object[] {BlockchainStatsApplication.class, BlockchainStatsApplicationConfiguration.class}, args);
    }

    public static void main(String[] args) {
        new BlockchainStatsApplication().start(args);
    }
}
