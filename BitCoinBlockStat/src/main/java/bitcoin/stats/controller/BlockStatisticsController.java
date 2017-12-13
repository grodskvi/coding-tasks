package bitcoin.stats.controller;

import bitcoin.stats.model.dto.BlockDTO;
import bitcoin.stats.service.BlockchainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BlockStatisticsController {

    private static final Logger LOG = LoggerFactory.getLogger(BlockStatisticsController.class);

    @Autowired
    private BlockchainService statService;

    @Autowired
    private BlockDTOFactory blockDTOFactory;

    @Value("${request.blocks.default.number}")
    private int defaultBlocksNumber;

    @RequestMapping(path="/statistics/lastblocks", method=RequestMethod.GET)
    @ResponseBody
    public List<BlockDTO> lastBlocks(@RequestParam(value = "lastHeight", required = false) Long lastHeight,
                                     @RequestParam(value = "limit", required = false) Integer limit) {
        LOG.debug("Received request for lastBlocks with params {}, {}", lastHeight, limit);
        if (lastHeight == null) {
            lastHeight = 0L;
        }
        if (limit == null) {
            limit = defaultBlocksNumber;
        }
        if (lastHeight < 0) {
            throw new IllegalArgumentException("Invalid parameter 'lastHeight' value " + lastHeight);
        }
        if (limit < 0) {
            throw new IllegalArgumentException("Invalid parameter 'limit' value " + limit);
        }
        return statService.getLatestBlocks(lastHeight, limit).stream()
                .map(blockDTOFactory::createBlockDTO)
                .collect(Collectors.toList());
    }
}
