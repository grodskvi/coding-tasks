package bitcoin.stats.model.dto;

public class BlockDTO {
    private final String blockHash;
    private final long height;
    private final double generationSpeed;


    public BlockDTO(String blockHash, long height, double generationSpeed) {
        this.blockHash = blockHash;
        this.height = height;
        this.generationSpeed = generationSpeed;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public long getHeight() {
        return height;
    }

    public double getGenerationSpeed() {
        return generationSpeed;
    }

}
