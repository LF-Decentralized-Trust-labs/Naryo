package io.librevents.domain.node.subscription.block.method.poll;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class PollBlockSubscriptionMethodConfigurationTest {

    @Test
    void testIntervalCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () -> new PollBlockSubscriptionMethodConfiguration(null));
    }

    @Test
    void testIntervalCreation() {
        Duration duration = Duration.ofSeconds(12);
        PollBlockSubscriptionMethodConfiguration config =
                new PollBlockSubscriptionMethodConfiguration(new Interval(duration));
        assertNotNull(config);
        assertEquals(duration, config.getInterval().value());
    }
}
