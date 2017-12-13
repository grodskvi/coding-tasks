package bitcoin.stats.service;

public interface BlockListener {
    void acceptBlock(String blockHash);
}
