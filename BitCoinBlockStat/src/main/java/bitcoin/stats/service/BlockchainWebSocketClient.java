package bitcoin.stats.service;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class BlockchainWebSocketClient {

    private static final Logger LOG = LoggerFactory.getLogger(BlockchainWebSocketClient.class);

    private static final String BLOCK_INFO_SUBSCRIBE = "{\"op\":\"blocks_sub\"}";
    private static final String BLOCK_INFO_UNSUBSCRIBE = "{\"op\":\"blocks_unsub\"}";

    @Autowired
    private WebSocket webSocket;
    @Autowired
    private WebSocketAdapter webSocketAdapter;


    @PostConstruct
    public void init() {
        try {
            webSocket.addListener(webSocketAdapter);
            LOG.info("Connecting to {}", webSocket.getURI());
            webSocket.connect();
            LOG.info("Subscribing for new blocks");
            webSocket.sendText(BLOCK_INFO_SUBSCRIBE);
        } catch (WebSocketException e) {
            LOG.error("Error during establishing connection to {}", webSocket.getURI(), e);
            throw new RuntimeException("Error during establishing connection to " + webSocket.getURI(), e);
        }
    }

    @PreDestroy
    public void destroy() {
        LOG.info("Unsubscribing for new blocks");
        webSocket.sendText(BLOCK_INFO_UNSUBSCRIBE);
        LOG.info("Disconnecting from {}", webSocket.getURI());
        webSocket.disconnect();
    }
}
