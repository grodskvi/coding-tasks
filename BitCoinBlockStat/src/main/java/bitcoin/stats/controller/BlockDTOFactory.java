package bitcoin.stats.controller;

import bitcoin.stats.model.BlockInfo;
import bitcoin.stats.model.dto.BlockDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BlockDTOFactory {

    private int avgBlockGenerationSeconds;

    public BlockDTOFactory(@Value("${block.generation.avg.seconds}") int avgBlockGenerationSeconds) {
        this.avgBlockGenerationSeconds = avgBlockGenerationSeconds;
    }

    public BlockDTO createBlockDTO(BlockInfo blockInfo) {
        double generationSpeed = (double) blockInfo.getBlockGenerationTime() / avgBlockGenerationSeconds;
        return new BlockDTO(blockInfo.getBlockHash(), blockInfo.getHeight(), generationSpeed);
    }
}
