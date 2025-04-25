package io.librevents.application.node.interactor.block.mapper;

import java.util.Map;
import java.util.UUID;

import io.librevents.application.common.Mapper;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.event.block.BlockEvent;

public final class BlockToBlockEventMapper implements Mapper<Block, BlockEvent> {

    @Override
    public BlockEvent map(Block source) {
        throw new UnsupportedOperationException("Mapping without nodeId is not supported");
    }

    @Override
    public BlockEvent map(Block source, Map<String, Object> additionalProperties) {
        return map(source, (UUID) additionalProperties.get("nodeId"));
    }

    private static BlockEvent map(Block block, UUID nodeId) {
        return new BlockEvent(
                nodeId,
                new NonNegativeBlockNumber(block.number()),
                block.hash(),
                block.logsBloom(),
                block.size(),
                block.gasUsed(),
                block.gasLimit(),
                block.timestamp(),
                block.transactions());
    }
}
