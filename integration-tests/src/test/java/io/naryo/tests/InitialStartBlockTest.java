package io.naryo.tests;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

import io.naryo.BaseIntegrationTest;
import io.naryo.dto.message.BlockEvent;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Durations;

import static io.naryo.utils.KafkaConsumerFactory.createKafkaConsumer;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(value = {"mongo", "kafka"})
@ContextConfiguration(initializers = InitialStartBlockTest.Initializer.class)
public class InitialStartBlockTest extends BaseIntegrationTest {

    private static final String BLOCK_TOPIC = "block-events-" + UUID.randomUUID();
    private static KafkaConsumer<String, String> blockConsumer;
    private static BigInteger initialBlock = BigInteger.ZERO;

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            try {
                initialBlock = web3j.ethBlockNumber().send().getBlockNumber();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            TestPropertyValues.of(
                            "broadcaster.enableBlockNotifications=true",
                            "ethereum.nodes[0].maxBlockToSync=1",
                            "ethereum.nodes[0].initialStartBlock="
                                    + BigInteger.valueOf(10).add(initialBlock),
                            "kafka.topic.blockEvents=" + BLOCK_TOPIC)
                    .applyTo(context);
        }
    }

    @BeforeEach
    void beforeEach() {
        blockConsumer = createKafkaConsumer(kafkaContainer.getBootstrapServers(), BLOCK_TOPIC);
    }

    @Test
    void testStartBlockForBlockBroadcast() throws Exception {
        int blocksToMine = 11;

        mineBlocks(blocksToMine);

        assertTrue(getBlockEvent(BigInteger.ONE.add(initialBlock)).isPresent());
    }

    private Optional<BlockEvent> getBlockEvent(BigInteger expectedBlockNumber) {
        return kafkaConsumerFactory.getMessage(
                blockConsumer,
                BlockEvent.class,
                BLOCK_TOPIC,
                Durations.ONE_MINUTE,
                (BlockEvent b) -> b.getDetails().getNumber().equals(expectedBlockNumber));
    }
}
