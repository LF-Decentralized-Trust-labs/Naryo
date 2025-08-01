package io.naryo.domain.node.interaction.block.ethereum;

import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.domain.node.interaction.block.InteractionMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

final class EthereumRpcBlockInteractionConfigurationTest {

    @Test
    void testConstructor() {
        EthereumRpcBlockInteractionConfiguration config =
                new EthereumRpcBlockInteractionConfiguration();
        assertNotNull(config);
        assertEquals(InteractionMode.ETHEREUM_RPC, config.getMode());
        assertEquals(InteractionStrategy.BLOCK_BASED, config.getStrategy());
    }
}
