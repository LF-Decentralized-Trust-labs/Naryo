package io.naryo.domain.node.interaction.block.hedera;

import io.naryo.domain.node.interaction.block.InteractionMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

final class HederaMirrorNodeBlockInteractionConfigurationTest {

    @Test
    void testConstructor() {
        HederaMirrorNodeBlockInteractionConfiguration config =
                new HederaMirrorNodeBlockInteractionConfiguration(
                        new LimitPerRequest(100), new RetriesPerRequest(3));
        assertNotNull(config);
        assertEquals(InteractionMode.HEDERA_MIRROR_NODE, config.getMode());
        assertEquals(100, config.getLimitPerRequest().value());
        assertEquals(3, config.getRetriesPerRequest().value());
    }
}
