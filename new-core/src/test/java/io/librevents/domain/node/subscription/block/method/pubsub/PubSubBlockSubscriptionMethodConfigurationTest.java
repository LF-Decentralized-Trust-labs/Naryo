package io.librevents.domain.node.subscription.block.method.pubsub;

import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PubSubBlockSubscriptionMethodConfigurationTest {

    @Test
    void testConstructor() {
        PubSubBlockSubscriptionMethodConfiguration config =
                new PubSubBlockSubscriptionMethodConfiguration();
        assertEquals(BlockSubscriptionMethod.PUBSUB, config.getMethod());
    }
}
