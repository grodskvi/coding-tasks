package bitcoin.stats.controller;

import bitcoin.stats.model.BlockInfo;
import bitcoin.stats.model.dto.BlockDTO;
import org.assertj.core.data.Percentage;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BlockDTOFactoryTest {

    private final int avgGenerationSeconds = 100;
    private BlockDTOFactory factory = new BlockDTOFactory(avgGenerationSeconds);

    @Test
    public void createsBlockDTO() {
        BlockInfo blockInfo = mock(BlockInfo.class);
        when(blockInfo.getBlockHash()).thenReturn("a");
        when(blockInfo.getHeight()).thenReturn(2000L);
        when(blockInfo.getBlockGenerationTime()).thenReturn(150L);

        BlockDTO blockDTO = factory.createBlockDTO(blockInfo);
        assertThat(blockDTO.getBlockHash()).isEqualTo("a");
        assertThat(blockDTO.getHeight()).isEqualTo(2000L);
        assertThat(blockDTO.getGenerationSpeed()).isCloseTo(1.5, withPercentage(0.01));
    }

}