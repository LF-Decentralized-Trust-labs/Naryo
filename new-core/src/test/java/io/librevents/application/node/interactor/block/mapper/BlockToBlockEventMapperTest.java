package io.librevents.application.node.interactor.block.mapper;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.domain.event.block.BlockEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockToBlockEventMapperTest {

    @Test
    void testMap() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> {
                    Block block =
                            new Block(
                                    BigInteger.ONE,
                                    "0x123",
                                    BigInteger.ONE,
                                    "1000",
                                    BigInteger.ZERO,
                                    BigInteger.TEN,
                                    BigInteger.TEN,
                                    BigInteger.ZERO,
                                    List.of());
                    BlockToBlockEventMapper mapper = new BlockToBlockEventMapper();
                    mapper.map(block);
                });
    }

    @Test
    void testMapWithNodeId() {
        // Arrange
        Block block =
                new Block(
                        BigInteger.ONE,
                        "0x123",
                        BigInteger.ONE,
                        "1000",
                        BigInteger.ZERO,
                        BigInteger.TEN,
                        BigInteger.TEN,
                        BigInteger.ZERO,
                        List.of());
        BlockToBlockEventMapper mapper = new BlockToBlockEventMapper();
        UUID nodeId = UUID.randomUUID();

        // Act
        BlockEvent event = mapper.map(block, Map.of("nodeId", nodeId));

        // Assert
        assertNotNull(event);
        assertEquals(nodeId, event.getNodeId());
    }
}
