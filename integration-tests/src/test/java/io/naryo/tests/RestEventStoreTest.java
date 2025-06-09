package io.naryo.tests;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.naryo.BaseIntegrationTest;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.ContractEvent;
import io.naryo.integration.eventstore.EventStore;
import io.naryo.integration.mixin.SimplePageImpl;
import io.naryo.model.LatestBlock;
import io.naryo.utils.EventFilterCreator;
import io.naryo.utils.JSON;
import io.naryo.utils.KafkaConsumerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;
import org.testcontainers.shaded.org.awaitility.Durations;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.naryo.utils.StringManipulation.stringToBytes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ActiveProfiles(value = {"mongo", "kafka"})
@ContextConfiguration(initializers = RestEventStoreTest.Initializer.class)
class RestEventStoreTest extends BaseIntegrationTest {

    private static final String EVENTS_PATH = "/events";
    private static final String LATEST_BLOCK_PATH = "/latest-block";
    private static final String CONTRACT_TOPIC = "contract-events-" + UUID.randomUUID();
    private static final int WIREMOCK_PORT = 8081;
    private static WireMockServer wireMockServer;
    protected static KafkaConsumer<String, String> contractConsumer;
    @Autowired private EventStore eventStore;

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                            "kafka.topic.contractEvents=" + CONTRACT_TOPIC,
                            "eventStore.type=REST",
                            "eventStore.eventPath=" + EVENTS_PATH,
                            "eventStore.latestBlockPath=" + LATEST_BLOCK_PATH,
                            "eventStore.url=http://localhost:" + WIREMOCK_PORT)
                    .applyTo(context);
        }
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        contractConsumer =
                KafkaConsumerFactory.createKafkaConsumer(
                        kafkaContainer.getBootstrapServers(), CONTRACT_TOPIC);

        wireMockServer = new WireMockServer(wireMockConfig().port(WIREMOCK_PORT));
        wireMockServer.start();

        EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
        final LatestBlock latestBlock = new LatestBlock();
        latestBlock.setNumber(blockNumber.getBlockNumber());
        wireMockServer.stubFor(
                get(urlPathEqualTo(LATEST_BLOCK_PATH))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(
                                                "Content-Type",
                                                MediaType.APPLICATION_JSON.getMediaType())
                                        .withBody(JSON.stringify(latestBlock))));
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    void testBroadcastEventAddedToEventStore() throws Exception {
        ContractEventFilter eventFilter =
                EventFilterCreator.buildDummyEventFilter(defaultContract.getContractAddress());
        eventFilterCreator.createFilter(eventFilter);

        TransactionReceipt txReceipt =
                defaultContract
                        .emitEvent(stringToBytes("BytesValue"), BigInteger.TEN, "StringValue")
                        .send();

        final Optional<ContractEvent> contractEvent =
                kafkaConsumerFactory.getTransactionalMessage(
                        contractConsumer,
                        ContractEvent.class,
                        CONTRACT_TOPIC,
                        Durations.ONE_MINUTE,
                        txReceipt.getTransactionHash());
        assertTrue(contractEvent.isPresent());
        final ContractEventDetails eventDetails = contractEvent.get().getDetails();

        final Page<ContractEventDetails> eventPage =
                new SimplePageImpl<>(List.of(eventDetails), 1, 1, 1);
        wireMockServer.stubFor(
                get(urlPathEqualTo(EVENTS_PATH))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(
                                                "Content-Type",
                                                MediaType.APPLICATION_JSON.getMediaType())
                                        .withBody(JSON.stringify(eventPage))));

        List<ContractEventDetails> savedEvents =
                eventStore
                        .getContractEventsForSignature(
                                eventDetails.getEventSpecificationSignature(),
                                Keys.toChecksumAddress(defaultContract.getContractAddress()),
                                PageRequest.of(0, 100000)
                                        .withSort(Sort.Direction.ASC, "blockNumber"))
                        .getContent();

        assertEquals(1, savedEvents.size());
        assertEquals(eventDetails, savedEvents.getFirst());
    }
}
