package bitcoin.stats.model;

import java.util.Objects;

public class RawBlockchainBlock {
    private final String blockHash;
    private final long timestamp;
    private final long height;
    private final boolean mainChain;
    private final String previousBlockHash;

    public RawBlockchainBlock(String blockHash, long timestamp, long height, boolean mainChain, String previousBlockHash) {
        this.blockHash = blockHash;
        this.timestamp = timestamp;
        this.height = height;
        this.mainChain = mainChain;
        this.previousBlockHash = previousBlockHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public long getHeight() {
        return height;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isMainChain() {
        return mainChain;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawBlockchainBlock block = (RawBlockchainBlock) o;
        return timestamp == block.timestamp &&
                height == block.height &&
                mainChain == block.mainChain &&
                Objects.equals(blockHash, block.blockHash) &&
                Objects.equals(previousBlockHash, block.previousBlockHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockHash, timestamp, height, mainChain, previousBlockHash);
    }

    @Override
    public String toString() {
        return "RawBlockchainBlock{" +
                "blockHash='" + blockHash + '\'' +
                ", timestamp=" + timestamp +
                ", height=" + height +
                ", mainChain=" + mainChain +
                ", previousBlockHash='" + previousBlockHash + '\'' +
                '}';
    }
}
