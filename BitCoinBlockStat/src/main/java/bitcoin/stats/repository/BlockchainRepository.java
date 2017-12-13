package bitcoin.stats.repository;

import bitcoin.stats.model.RawBlockchainBlock;
import info.blockchain.api.APIException;
import info.blockchain.api.blockexplorer.BlockExplorer;
import info.blockchain.api.blockexplorer.entity.Block;
import info.blockchain.api.blockexplorer.entity.LatestBlock;
import info.blockchain.api.blockexplorer.entity.SimpleBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class BlockchainRepository {

    private static final Logger LOG = LoggerFactory.getLogger(BlockchainRepository.class);

    @Autowired
    private BlockExplorer blockExplorer;

    /**
     * retrieves latest blocks using webservice. Webservice returns blocks returned within current day
     * Blocks are sorted, so that latest block is at head, and the earliest block at tail.
     * Number of elements in list is not much then maxLimit parameter
     * If no blocks are returned by webservice, this is an indicator that no blocks were formed today. In such case, latest block is returned
     */
    public List<RawBlockchainBlock> getLatestBlocks(int maxLimit) throws RepositoryException {
        Iterator<SimpleBlock> blocks = null;
        try {
            blocks = blockExplorer.getBlocks().iterator();
        } catch (APIException | IOException e) {
            LOG.info("Exception occurred while requesting blocks latest block", e);
            throw new RepositoryException(e);
        }

        List<RawBlockchainBlock> rawBlockchainBlocks = new ArrayList<>();
        SimpleBlock latestBlock = null;
        while (blocks.hasNext()) {
            SimpleBlock currentBlock = blocks.next();
            if (!currentBlock.isMainChain()) {
                LOG.info("Block {} is not on main chain. Ignoring", currentBlock.getHash());
                continue;
            }
            if (latestBlock != null) {
                RawBlockchainBlock rawBlockchainBlock = createRawBlock(latestBlock, currentBlock);
                rawBlockchainBlocks.add(rawBlockchainBlock);
            }
            latestBlock = currentBlock;

            if (rawBlockchainBlocks.size() >= maxLimit) {
                break;
            }
        }
        if (rawBlockchainBlocks.isEmpty()) {
            rawBlockchainBlocks.add(getLatestBlock());
        }
        return rawBlockchainBlocks;
    }

    /**
     * Gets the latest block on the main chain
     */
    public RawBlockchainBlock getLatestBlock() throws RepositoryException {
        try {
            LatestBlock latestBlock = blockExplorer.getLatestBlock();
            Block block = blockExplorer.getBlock(latestBlock.getHash());
            return createRawBlock(block);
        } catch (APIException | IOException e) {
            LOG.info("Exception occurred while retrieving latest block", e);
            throw new RepositoryException(e);
        }
    }

    //TODO: retry logic is required
    public RawBlockchainBlock getBlock(String blockHash) throws RepositoryException {
        LOG.info("Requesting block {}", blockHash);
        try {
            Block block = blockExplorer.getBlock(blockHash);
            return createRawBlock(block);
        } catch (APIException | IOException e) {
            LOG.info("Exception occurred while retrieving block {}", blockHash, e);
            throw new RepositoryException(e);
        }
    }

    private RawBlockchainBlock createRawBlock(Block block) {
        return new RawBlockchainBlock(block.getHash(), block.getTime(), block.getHeight(), block.isMainChain(), block.getPreviousBlockHash());
    }

    private RawBlockchainBlock createRawBlock(SimpleBlock block, SimpleBlock previousBlock) {
        return new RawBlockchainBlock(block.getHash(), block.getTime(), block.getHeight(), block.isMainChain(), previousBlock.getHash());
    }
}
