package bitcoin.stats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class BlockchainWebsocketAdapter extends WebSocketAdapter {

    @Autowired
    private List<BlockListener> blockListeners;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Logger LOG = LoggerFactory.getLogger(BlockchainWebsocketAdapter.class);

    public BlockchainWebsocketAdapter(BlockListener... listeners) {
        blockListeners = Arrays.asList(listeners);
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        LOG.info("Connected to {}", websocket.getURI());
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        LOG.error("Error during connection to {}", websocket.getURI(), exception);
    }

    @Override
    public void onTextMessage(WebSocket websocket, String message) throws Exception {
        LOG.info("Received message from {}: {}", websocket.getURI(), message);
        String blockHash = getBlockHash(message);
        if (blockHash != null) {
            blockListeners.forEach(blockListener -> blockListener.acceptBlock(blockHash));
        } else {
            LOG.warn("Unable to resolve hash field in received message");
        }
    }

    private String getBlockHash(String message) throws IOException {
        Map parsedMessage = mapper.readValue(message, Map.class);
        Object xField = parsedMessage.get("x");
        if (xField == null || !(xField instanceof Map)) {
            return null;
        }
        return (String)((Map) xField).get("hash");
    }
}
