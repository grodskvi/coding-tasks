package bitcoin.stats.service;


import com.neovisionaries.ws.client.WebSocket;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BlockchainWebsocketAdapterTest {

    private BlockListener listener = mock(BlockListener.class);
    private BlockchainWebsocketAdapter adapter = new BlockchainWebsocketAdapter(listener);

    @Test
    public void notifiesListenersWithReceivedBlockHash() throws Exception {
        String webSocketResponse = IOUtils.toString(getClass().getResourceAsStream("/websocket_new_block_response.txt"), "UTF-8");
        adapter.onTextMessage(mock(WebSocket.class), webSocketResponse);
        verify(listener).acceptBlock("000000000000000000cdbea7193bb9b94187b8a10e68c0e966d8f1fd44d41e5b");
    }


}