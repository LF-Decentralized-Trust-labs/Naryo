package io.naryo.tests;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles(value = {"mongo", "kafka"})
@ContextConfiguration(initializers = KafkaBroadcasterPubSubTest.Initializer.class)
public class KafkaBroadcasterPubSubTest extends BaseKafkaBroadcasterTest {

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                            String.format(
                                    "ethereum.nodes[1].url=wss://%s:%s",
                                    ganacheContainer.getHost(),
                                    ganacheContainer.getFirstMappedPort()),
                            "ethereum.nodes[1].blockStrategy=PUBSUB")
                    .applyTo(context);
        }
    }
}
