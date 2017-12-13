package bitcoin.stats.service;

import bitcoin.stats.model.BlockInfo;
import bitcoin.stats.model.RawBlockchainBlock;
import bitcoin.stats.repository.BlockchainRepository;
import bitcoin.stats.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Collections.emptyList;

@Service
public class BlockchainService implements BlockListener {

    private static final Logger LOG = LoggerFactory.getLogger(BlockchainService.class);

    private final int initialCacheSize;

    private final int maxCacheSize;

    private final BlockchainRepository repository;

    private final CopyOnWriteArrayList<BlockInfo> recentBlocks;

    @Autowired
    public BlockchainService(@Value("${cache.blocks.initial}") int initialCacheSize,
                             @Value("${cache.blocks.max}") int maxCacheSize,
                             BlockchainRepository repository) {
        this(initialCacheSize, maxCacheSize, repository, emptyList());
    }

    BlockchainService(int initialCacheSize, int maxCacheSize, BlockchainRepository repository, List<BlockInfo> initialCache) {
        this.initialCacheSize = initialCacheSize;
        this.maxCacheSize = maxCacheSize;
        this.repository = repository;
        this.recentBlocks = new CopyOnWriteArrayList<>(initialCache);
    }

    @PostConstruct
    public void init() throws RepositoryException {
        synchronized (recentBlocks) {
            List<RawBlockchainBlock> blocks = repository.getLatestBlocks(initialCacheSize + 1);
            recentBlocks.addAll(createBlockInfos(blocks));

            RawBlockchainBlock baseBlock = blocks.get(blocks.size() - 1);
            while (recentBlocks.size() < initialCacheSize) {
                RawBlockchainBlock previousBlock = repository.getBlock(baseBlock.getPreviousBlockHash());
                recentBlocks.add(new BlockInfo(baseBlock, previousBlock));

                baseBlock = previousBlock;
            }
        }
    }

    public List<BlockInfo> getLatestBlocks(long lastHeight, int maxBlocks) {
        List<BlockInfo> infos = new ArrayList<>();
        for (BlockInfo info: recentBlocks) {
            if (infos.size() == maxBlocks || info.getHeight() <= lastHeight) {
                break;
            }
            infos.add(info);
        }
        return infos;
    }

    @Override
    public void acceptBlock(String blockHash) {
        LOG.info("Accepting {} block", blockHash);
        synchronized (recentBlocks) {
            if (recentBlocks.isEmpty()) {
                LOG.debug("Recent blocks were not initialized");
                return;
            }
            try {
                BlockInfo latestBlock = recentBlocks.get(0);
                RawBlockchainBlock block = repository.getBlock(blockHash);
                if (!block.isMainChain()) {
                    LOG.info("Block {} does not belong to main chain. Skipping", blockHash);
                    return;
                }
                if (block.getHeight() <= latestBlock.getHeight()) {
                    LOG.info("Block {} is older then latest block. Block height: {}, latest block height: {}. Skipping", blockHash, block.getHeight(), latestBlock.getHeight());
                    return;
                }
                if (block.getPreviousBlockHash().equals(latestBlock.getBlockHash())) {
                    recentBlocks.add(0, new BlockInfo(block, latestBlock.getBlock()));
                } else {
                    LOG.info("Some blocks were missed. Trying to traverse");
                    List<RawBlockchainBlock> blocks = new ArrayList<>();
                    blocks.add(block);
                    while (!block.getPreviousBlockHash().equals(latestBlock.getBlockHash()) && blocks.size() < maxCacheSize) {
                        block = repository.getBlock(block.getPreviousBlockHash());
                        blocks.add(block);
                    }
                    LOG.info("Found {} missing blocks", blocks.size());
                    blocks.add(latestBlock.getBlock());

                    recentBlocks.addAll(0, createBlockInfos(blocks));
                }

                while (recentBlocks.size() > maxCacheSize) {
                    recentBlocks.remove(recentBlocks.size() - 1);
                }

            } catch (RepositoryException e) {
                LOG.info("Error occurred while accepting {}. Skipping it for now", e);
            }

        }
    }

    private List<BlockInfo> createBlockInfos(List<RawBlockchainBlock> blocks) {
        List<BlockInfo> infos = new ArrayList<>();
        for (int i = 0; i < blocks.size() - 1; i++) {
            RawBlockchainBlock block = blocks.get(i);
            RawBlockchainBlock previousBlock = blocks.get(i + 1);
            infos.add(new BlockInfo(block, previousBlock));
        }
        return infos;
    }

    List<BlockInfo> getRecentBlocks() {
        return recentBlocks;
    }
}
