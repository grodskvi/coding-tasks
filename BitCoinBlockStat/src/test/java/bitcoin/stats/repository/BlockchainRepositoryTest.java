package bitcoin.stats.repository;

import bitcoin.stats.model.RawBlockchainBlock;
import info.blockchain.api.APIException;
import info.blockchain.api.blockexplorer.BlockExplorer;
import info.blockchain.api.blockexplorer.entity.Block;
import info.blockchain.api.blockexplorer.entity.LatestBlock;
import info.blockchain.api.blockexplorer.entity.SimpleBlock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BlockchainRepositoryTest {

    @Mock
    private BlockExplorer blockExplorer;

    @InjectMocks
    private BlockchainRepository blockchainRepository;

    @Test
    public void retrievesLatestBlocks() throws APIException, IOException, RepositoryException {
        when(blockExplorer.getBlocks()).thenReturn(Arrays.asList(
                mainchainSimpleBlock("b", 3, 300),
                mainchainSimpleBlock("a", 1, 100)
        ));
        assertThat(blockchainRepository.getLatestBlocks(2))
                .extracting("blockHash", "timestamp", "height", "mainChain", "previousBlockHash")
                .containsExactly(tuple("b", 300L, 3L, true, "a"));
    }

    @Test
    public void retrievesLatestBlocksWithinLimit() throws APIException, IOException, RepositoryException {
        when(blockExplorer.getBlocks()).thenReturn(Arrays.asList(
                mainchainSimpleBlock("d", 4, 400),
                mainchainSimpleBlock("c", 3, 300),
                mainchainSimpleBlock("b", 2, 200),
                mainchainSimpleBlock("a", 1, 100)
        ));
        assertThat(blockchainRepository.getLatestBlocks(2))
                .extracting("blockHash", "timestamp", "height", "mainChain", "previousBlockHash")
                .containsExactly(
                        tuple("d", 400L, 4L, true, "c"),
                        tuple("c", 300L, 3L, true, "b"));
    }

    @Test
    public void ignoresSiblingChainBlocksWhileRetrievingLatestBlocksWithinLimit() throws APIException, IOException, RepositoryException {
        when(blockExplorer.getBlocks()).thenReturn(Arrays.asList(
                mainchainSimpleBlock("d", 4, 400),
                siblingchainSimpleBlock("c", 3, 300),
                mainchainSimpleBlock("b", 2, 200),
                mainchainSimpleBlock("a", 1, 100)
        ));
        assertThat(blockchainRepository.getLatestBlocks(2))
                .extracting("blockHash", "timestamp", "height", "mainChain", "previousBlockHash")
                .containsExactly(
                        tuple("d", 400L, 4L, true, "b"),
                        tuple("b", 200L, 2L, true, "a"));
    }

    @Test
    public void retrievesLatestBlockIfNoBlocksWereProvided() throws APIException, IOException, RepositoryException {
        when(blockExplorer.getBlocks()).thenReturn(emptyList());
        when(blockExplorer.getLatestBlock()).thenReturn(latestBlock("b", 2, 200));
        Block block = block("b", 2, 200, "a");
        when(blockExplorer.getBlock("b")).thenReturn(block);

        assertThat(blockchainRepository.getLatestBlocks(2))
                .extracting("blockHash", "timestamp", "height", "mainChain", "previousBlockHash")
                .containsExactly(tuple("b", 200L, 2L, true, "a"));
    }

    @Test
    public void retrievesLatestBlock() throws APIException, IOException, RepositoryException {
        when(blockExplorer.getLatestBlock()).thenReturn(latestBlock("b", 2, 200));
        Block block = block("b", 2, 200, "a");
        when(blockExplorer.getBlock("b")).thenReturn(block);

        assertThat(blockchainRepository.getLatestBlock())
                .isEqualTo(new RawBlockchainBlock("b", 200, 2, true, "a"));
    }

    private SimpleBlock mainchainSimpleBlock(String hash, long height, long timestamp) {
        return new SimpleBlock(height, hash, timestamp, true);
    }

    private SimpleBlock siblingchainSimpleBlock(String hash, long height, long timestamp) {
        return new SimpleBlock(height, hash, timestamp, false);
    }

    private LatestBlock latestBlock(String hash, long height, long timestamp) {
        return new LatestBlock(height, hash, timestamp, true, 0, emptyList());
    }

    private Block block(String hash, long height, long timestamp, String previousHash) {
        Block block = mock(Block.class);
        when(block.getHeight()).thenReturn(height);
        when(block.getHash()).thenReturn(hash);
        when(block.getTime()).thenReturn(timestamp);
        when(block.isMainChain()).thenReturn(true);
        when(block.getPreviousBlockHash()).thenReturn(previousHash);
        return block;
    }
}