package io.naryo.tests;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.naryo.dto.block.BlockDetails;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.BlockEvent;
import io.naryo.dto.message.ContractEvent;
import io.naryo.dto.message.TransactionEvent;
import io.naryo.dto.transaction.TransactionDetails;
import io.naryo.dto.transaction.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@Slf4j
@ContextConfiguration(initializers = BaseHttpBroadcasterTest.Initializer.class)
public abstract class BaseHttpBroadcasterTest extends BaseBroadcasterTest {

    private static final String BLOCK_EVENTS_PATH = "/consumer/block-events";
    private static final String CONTRACT_EVENTS_PATH = "/consumer/contract-events";
    private static final String TRANSACTION_EVENTS_PATH = "/consumer/transaction-events";
    private static final List<ContractEvent> CONTRACT_EVENTS = new ArrayList<>();
    private static final List<TransactionEvent> TRANSACTION_EVENTS = new ArrayList<>();
    private static final List<BlockEvent> BLOCK_EVENTS = new ArrayList<>();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int WIREMOCK_PORT = 8081;
    private static WireMockServer wireMockServer;

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                            "broadcaster.enableBlockNotifications=true",
                            String.format(
                                    "broadcaster.http.blockEventsUrl=http://localhost:%s%s",
                                    WIREMOCK_PORT, BLOCK_EVENTS_PATH),
                            String.format(
                                    "broadcaster.http.contractEventsUrl=http://localhost:%s%s",
                                    WIREMOCK_PORT, CONTRACT_EVENTS_PATH),
                            String.format(
                                    "broadcaster.http.transactionEventsUrl=http://localhost:%s%s",
                                    WIREMOCK_PORT, TRANSACTION_EVENTS_PATH))
                    .applyTo(context);
        }
    }

    @BeforeAll
    static void setup() {
        wireMockServer = new WireMockServer(wireMockConfig().port(WIREMOCK_PORT));
        wireMockServer.start();

        wireMockServer.addStubMapping(
                post(urlPathEqualTo(BLOCK_EVENTS_PATH))
                        .willReturn(aResponse().withStatus(200))
                        .build());

        wireMockServer.addStubMapping(
                post(urlPathEqualTo(CONTRACT_EVENTS_PATH))
                        .willReturn(aResponse().withStatus(200))
                        .build());

        wireMockServer.addStubMapping(
                post(urlPathEqualTo(TRANSACTION_EVENTS_PATH))
                        .willReturn(aResponse().withStatus(200))
                        .build());

        wireMockServer.addMockServiceRequestListener(
                (request, response) -> {
                    if (request.getUrl().contains(CONTRACT_EVENTS_PATH)) {
                        final String body = request.getBodyAsString();

                        try {
                            ContractEventDetails details =
                                    OBJECT_MAPPER.readValue(body, ContractEventDetails.class);
                            CONTRACT_EVENTS.add(new ContractEvent(details));
                        } catch (IOException e) {
                            log.error("Failed to parse contract event: {}", body, e);
                        }
                    }
                });

        wireMockServer.addMockServiceRequestListener(
                (request, response) -> {
                    if (request.getUrl().contains(BLOCK_EVENTS_PATH)) {
                        final String body = request.getBodyAsString();

                        try {
                            BlockDetails details =
                                    OBJECT_MAPPER.readValue(body, BlockDetails.class);
                            BLOCK_EVENTS.add(new BlockEvent(details));
                        } catch (IOException e) {
                            log.error("Failed to parse block event: {}", body, e);
                        }
                    }
                });

        wireMockServer.addMockServiceRequestListener(
                (request, response) -> {
                    if (request.getUrl().contains(TRANSACTION_EVENTS_PATH)) {
                        final String body = request.getBodyAsString();

                        try {
                            TransactionDetails details =
                                    OBJECT_MAPPER.readValue(body, TransactionDetails.class);
                            TRANSACTION_EVENTS.add(new TransactionEvent(details));
                        } catch (IOException e) {
                            log.error("Failed to parse block event: {}", body, e);
                        }
                    }
                });
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @Override
    protected Optional<ContractEvent> getContractEvent(
            String transactionHash, ContractEventStatus status) {
        Predicate<ContractEvent> predicate =
                v ->
                        v.getDetails().getTransactionHash().equals(transactionHash)
                                && v.getDetails().getStatus().equals(status);

        Awaitility.await()
                .atMost(Durations.ONE_MINUTE)
                .until(() -> CONTRACT_EVENTS.stream().anyMatch(predicate));

        return CONTRACT_EVENTS.stream().filter(predicate).findFirst();
    }

    @Override
    protected Optional<BlockEvent> getBlockEvent(BigInteger expectedBlockNumber) {
        Predicate<BlockEvent> predicate =
                v -> v.getDetails().getNumber().equals(expectedBlockNumber);

        Awaitility.await()
                .atMost(Durations.ONE_MINUTE)
                .until(() -> BLOCK_EVENTS.stream().anyMatch(predicate));

        return BLOCK_EVENTS.stream().filter(predicate).findFirst();
    }

    @Override
    protected Optional<TransactionEvent> getTransactionEvent(
            String transactionHash, TransactionStatus status) {
        Predicate<TransactionEvent> predicate =
                v ->
                        v.getDetails().getTransactionHash().equals(transactionHash)
                                && v.getDetails().getStatus().equals(status);

        Awaitility.await()
                .atMost(Durations.ONE_MINUTE)
                .until(() -> TRANSACTION_EVENTS.stream().anyMatch(predicate));

        return TRANSACTION_EVENTS.stream().filter(predicate).findFirst();
    }

    @Override
    protected Boolean isEventEmitted(ContractEventFilter eventFilter) {
        Supplier<Boolean> checkEvents =
                () ->
                        CONTRACT_EVENTS.stream()
                                .noneMatch(
                                        v ->
                                                v.getDetails()
                                                        .getFilterId()
                                                        .equals(eventFilter.getId()));
        AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
        Awaitility.await()
                .atMost(Durations.TEN_SECONDS)
                .pollInterval(Durations.ONE_SECOND)
                .until(
                        () -> {
                            long elapsedTime = System.currentTimeMillis() - startTime.get();

                            boolean noEventFound = checkEvents.get();
                            return noEventFound
                                    && elapsedTime
                                            > org.testcontainers.shaded.org.awaitility.Durations
                                                    .FIVE_SECONDS
                                                    .toMillis();
                        });
        return !checkEvents.get();
    }
}
