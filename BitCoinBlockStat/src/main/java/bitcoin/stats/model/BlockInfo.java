package bitcoin.stats.model;

import java.util.Objects;

public class BlockInfo {
    private final RawBlockchainBlock block;
    private final RawBlockchainBlock previousBlock;

    public BlockInfo(RawBlockchainBlock block, RawBlockchainBlock previousBlock) {
        this.block = block;
        this.previousBlock = previousBlock;
    }

    public String getBlockHash() {
        return block.getBlockHash();
    }

    public long getHeight() {
        return block.getHeight();
    }

    public long getBlockTimestamp() {
        return block.getTimestamp();
    }

    /**
     * returns number of seconds between receiving current and previous blocks
     */
    public long getBlockGenerationTime() {
        return block.getTimestamp() - previousBlock.getTimestamp();
    }

    public RawBlockchainBlock getBlock() {
        return block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockInfo blockInfo = (BlockInfo) o;
        return Objects.equals(block, blockInfo.block) &&
                Objects.equals(previousBlock, blockInfo.previousBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(block, previousBlock);
    }

    @Override
    public String toString() {
        return "BlockInfo{" +
                "block=" + block +
                ", previousBlock=" + previousBlock +
                '}';
    }
}
