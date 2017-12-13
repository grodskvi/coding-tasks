package bitcoin.stats.application;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import info.blockchain.api.blockexplorer.BlockExplorer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@Configuration
@ComponentScan(basePackages="bitcoin.stats")
@PropertySource("classpath:application.properties")
public class BlockchainStatsApplicationConfiguration {

    private static final String BLOCKCHAIN_INFO_ENDPOINT = "wss://ws.blockchain.info/inv";

    @Bean
    public WebSocket webSocket() {
        try {
            return new WebSocketFactory().createSocket(BLOCKCHAIN_INFO_ENDPOINT);
        } catch (IOException e) {
            throw new RuntimeException("Error during initializing connection to " + BLOCKCHAIN_INFO_ENDPOINT, e);
        }
    }

    @Bean
    public BlockExplorer blockExplorer() {
        return new BlockExplorer();
    }

}
