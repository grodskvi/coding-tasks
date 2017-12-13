package bitcoin.stats.service;

import bitcoin.stats.model.BlockInfo;
import bitcoin.stats.model.RawBlockchainBlock;
import bitcoin.stats.repository.BlockchainRepository;
import bitcoin.stats.repository.RepositoryException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BlockchainServiceTest {

    public static final int DEFAULT_INITIAL_CACHE_SIZE = 2;
    public static final int DEFAULT_MAX_CACHE_SIZE = 4;

    private BlockchainRepository repository = mock(BlockchainRepository.class);

    private BlockchainService service;

    @Before
    public void setUp() {
        service = new BlockchainService(DEFAULT_INITIAL_CACHE_SIZE, DEFAULT_MAX_CACHE_SIZE, repository);
    }

    @Test
    public void fetchesLatestBlocksOnInit() throws RepositoryException {
        RawBlockchainBlock latestBlock = rawBlock("c", 300, 3, "b");
        RawBlockchainBlock previousBlock = rawBlock("b", 200, 2, "a");
        RawBlockchainBlock earliestBlock = rawBlock("a", 100, 1, "x");

        when(repository.getLatestBlocks(3)).thenReturn(Arrays.asList(latestBlock, previousBlock, earliestBlock));
        service.init();

        assertThat(service.getRecentBlocks())
                .extracting("block", "previousBlock")
                .containsExactly(
                        tuple(latestBlock, previousBlock),
                        tuple(previousBlock, earliestBlock));
    }

    @Test
    public void requestsBlocksIndividuallyIfBufferIsNotFilledBlocksOnInit() throws RepositoryException {
        RawBlockchainBlock latestBlock = rawBlock("c", 300, 3, "b");
        RawBlockchainBlock previousBlock = rawBlock("b", 200, 2, "a");
        RawBlockchainBlock earliestBlock = rawBlock("a", 100, 1, "x");

        when(repository.getLatestBlocks(3)).thenReturn(Arrays.asList(latestBlock, previousBlock));
        when(repository.getBlock("b")).thenReturn(previousBlock);
        when(repository.getBlock("a")).thenReturn(earliestBlock);

        service.init();

        assertThat(service.getRecentBlocks())
                .extracting("block", "previousBlock")
                .containsExactly(
                        tuple(latestBlock, previousBlock),
                        tuple(previousBlock, earliestBlock));
    }

    @Test
    public void retrievesBlocksAfterSpecifiedHeight() {
        RawBlockchainBlock latestBlock = rawBlock("c", 300, 3, "b");
        RawBlockchainBlock previousBlock = rawBlock("b", 200, 2, "a");
        RawBlockchainBlock earliestBlock = rawBlock("a", 100, 1, "x");

        BlockInfo latestBlockInfo = new BlockInfo(latestBlock, previousBlock);
        BlockInfo previousBlockInfo = new BlockInfo(previousBlock, earliestBlock);

        service = new BlockchainService(
                DEFAULT_INITIAL_CACHE_SIZE,
                DEFAULT_MAX_CACHE_SIZE,
                repository,
                Arrays.asList(latestBlockInfo, previousBlockInfo));

        assertThat(service.getLatestBlocks(2, 3)).containsExactly(latestBlockInfo);
    }

    @Test
    public void retrievesBlocksWithinLimit() {
        RawBlockchainBlock latestBlock = rawBlock("c", 300, 3, "b");
        RawBlockchainBlock previousBlock = rawBlock("b", 200, 2, "a");
        RawBlockchainBlock earliestBlock = rawBlock("a", 100, 1, "x");

        BlockInfo latestBlockInfo = new BlockInfo(latestBlock, previousBlock);
        BlockInfo previousBlockInfo = new BlockInfo(previousBlock, earliestBlock);

        service = new BlockchainService(
                DEFAULT_INITIAL_CACHE_SIZE,
                DEFAULT_MAX_CACHE_SIZE,
                repository,
                Arrays.asList(latestBlockInfo, previousBlockInfo));

        assertThat(service.getLatestBlocks(0, 1)).containsExactly(latestBlockInfo);
    }

    @Test
    public void skipsAcceptingBlockIfCacheIsNotInitialized() throws RepositoryException {
        service.acceptBlock("a");
        when(repository.getBlock("a")).thenReturn(rawBlock("a", 100, 1, "x"));

        assertThat(service.getRecentBlocks()).isEmpty();
    }

    @Test
    public void skipsAcceptingSiblingBlock() throws RepositoryException {
        RawBlockchainBlock latestBlock = rawBlock("b", 200, 2, "a");
        RawBlockchainBlock previousBlock = rawBlock("a", 100, 1, "x");

        BlockInfo latestBlockInfo = new BlockInfo(latestBlock, previousBlock);

        service = new BlockchainService(
                DEFAULT_INITIAL_CACHE_SIZE,
                DEFAULT_MAX_CACHE_SIZE,
                repository,
                Arrays.asList(latestBlockInfo));

        when(repository.getBlock("c")).thenReturn(siblingBlock("c", 300, 3, "xb"));

        service.acceptBlock("c");
        assertThat(service.getRecentBlocks()).containsExactly(latestBlockInfo);
    }

    @Test
    public void skipsAcceptingNotLatestBlock() throws RepositoryException {
        RawBlockchainBlock latestBlock = rawBlock("b", 200, 2, "a");
        RawBlockchainBlock previousBlock = rawBlock("a", 100, 1, "x");

        BlockInfo latestBlockInfo = new BlockInfo(latestBlock, previousBlock);

        service = new BlockchainService(
                DEFAULT_INITIAL_CACHE_SIZE,
                DEFAULT_MAX_CACHE_SIZE,
                repository,
                Arrays.asList(latestBlockInfo));

        when(repository.getBlock("a")).thenReturn(previousBlock);

        service.acceptBlock("a");
        assertThat(service.getRecentBlocks()).containsExactly(latestBlockInfo);
    }

    @Test
    public void addsAcceptedBlockToCache() throws RepositoryException {
        RawBlockchainBlock latestBlock = rawBlock("b", 200, 2, "a");
        RawBlockchainBlock previousBlock = rawBlock("a", 100, 1, "x");

        BlockInfo latestBlockInfo = new BlockInfo(latestBlock, previousBlock);

        service = new BlockchainService(
                DEFAULT_INITIAL_CACHE_SIZE,
                DEFAULT_MAX_CACHE_SIZE,
                repository,
                Arrays.asList(latestBlockInfo));

        RawBlockchainBlock newBlock = rawBlock("c", 300, 3, "b");
        BlockInfo newBlockInfo = new BlockInfo(newBlock, latestBlock);

        when(repository.getBlock("c")).thenReturn(newBlock);

        service.acceptBlock("c");
        assertThat(service.getRecentBlocks()).containsExactly(newBlockInfo, latestBlockInfo);
    }

    @Test
    public void addsMissingBlockToCacheWhileAccepting() throws RepositoryException {
        RawBlockchainBlock latestBlock = rawBlock("b", 200, 2, "a");
        RawBlockchainBlock previousBlock = rawBlock("a", 100, 1, "x");

        BlockInfo latestBlockInfo = new BlockInfo(latestBlock, previousBlock);

        service = new BlockchainService(
                DEFAULT_INITIAL_CACHE_SIZE,
                DEFAULT_MAX_CACHE_SIZE,
                repository,
                Arrays.asList(latestBlockInfo));

        RawBlockchainBlock newBlock = rawBlock("d", 400, 4, "c");
        RawBlockchainBlock missingBlock = rawBlock("c", 300, 3, "b");
        BlockInfo newBlockInfo = new BlockInfo(newBlock, missingBlock);
        BlockInfo missingBlockInfo = new BlockInfo(missingBlock, latestBlock);

        when(repository.getBlock("d")).thenReturn(newBlock);
        when(repository.getBlock("c")).thenReturn(missingBlock);

        service.acceptBlock("d");
        assertThat(service.getRecentBlocks()).containsExactly(newBlockInfo, missingBlockInfo, latestBlockInfo);
    }

    @Test
    public void removesOldestBlocksWhenCacheHasMaxSize() throws RepositoryException {
        RawBlockchainBlock latestBlock = rawBlock("b", 200, 2, "a");
        RawBlockchainBlock previousBlock = rawBlock("a", 100, 1, "x");

        BlockInfo latestBlockInfo = new BlockInfo(latestBlock, previousBlock);

        service = new BlockchainService(
                DEFAULT_INITIAL_CACHE_SIZE,
                DEFAULT_INITIAL_CACHE_SIZE,
                repository,
                Arrays.asList(latestBlockInfo));

        RawBlockchainBlock newBlock = rawBlock("d", 400, 4, "c");
        RawBlockchainBlock missingBlock = rawBlock("c", 300, 3, "b");
        BlockInfo newBlockInfo = new BlockInfo(newBlock, missingBlock);
        BlockInfo missingBlockInfo = new BlockInfo(missingBlock, latestBlock);

        when(repository.getBlock("d")).thenReturn(newBlock);
        when(repository.getBlock("c")).thenReturn(missingBlock);

        service.acceptBlock("d");
        assertThat(service.getRecentBlocks()).containsExactly(newBlockInfo, missingBlockInfo);
    }

    @Test
    public void doesNotChangeCacheOnError() throws RepositoryException {
        RawBlockchainBlock latestBlock = rawBlock("b", 200, 2, "a");
        RawBlockchainBlock previousBlock = rawBlock("a", 100, 1, "x");

        BlockInfo latestBlockInfo = new BlockInfo(latestBlock, previousBlock);

        service = new BlockchainService(
                DEFAULT_INITIAL_CACHE_SIZE,
                DEFAULT_MAX_CACHE_SIZE,
                repository,
                Arrays.asList(latestBlockInfo));

        RawBlockchainBlock newBlock = rawBlock("d", 400, 4, "c");

        when(repository.getBlock("d")).thenReturn(newBlock);
        when(repository.getBlock("c")).thenThrow(mock(RepositoryException.class));

        service.acceptBlock("d");
        assertThat(service.getRecentBlocks()).containsExactly(latestBlockInfo);
    }


    private RawBlockchainBlock rawBlock(String hash, long timestamp, long height, String previousHash) {
        return new RawBlockchainBlock(hash, timestamp, height, true, previousHash);
    }

    private RawBlockchainBlock siblingBlock(String hash, long timestamp, long height, String previousHash) {
        return new RawBlockchainBlock(hash, timestamp, height, false, previousHash);
    }
}