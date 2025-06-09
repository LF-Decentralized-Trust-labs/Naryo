package io.naryo.tests;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles(value = {"mongo", "kafka"})
@ContextConfiguration(initializers = KafkaBroadcasterMultinodeTest.Initializer.class)
public class KafkaBroadcasterMultinodeTest extends BaseKafkaBroadcasterTest {

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                            "ethereum.nodes[1].name=another",
                            String.format(
                                    "ethereum.nodes[1].url=http://%s:%s",
                                    ganacheContainer.getHost(),
                                    ganacheContainer.getFirstMappedPort()),
                            "ethereum.nodes[1].blockStrategy=POLL")
                    .applyTo(context);
        }
    }
}
