package io.naryo.chain.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.chain.factory.ContractEventDetailsFactory;
import io.naryo.chain.service.block.EventBlockManagementService;
import io.naryo.chain.service.domain.io.*;
import io.naryo.chain.settings.Node;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.event.filter.ContractEventSpecification;
import io.naryo.dto.event.filter.ParameterDefinition;
import io.naryo.dto.event.filter.ParameterType;
import io.naryo.service.EventStoreService;
import io.naryo.service.SubscriptionService;
import io.naryo.service.exception.NotFoundException;
import io.naryo.testutils.DummyAsyncTaskService;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HederaServiceTest {
    private static final String NODE_URL = "http://test.test";

    private static final String NODE_NAME = "test";

    private ContractEventDetailsFactory contractEventDetailsFactory;

    private ObjectMapper objectMapper;

    private HederaService hederaService;

    private Call remoteCall;

    private EventBlockManagementService mockBlockManagement;

    @BeforeEach
    public void init() {
        final EventStoreService eventStoreService = mock(EventStoreService.class);
        contractEventDetailsFactory = mock(ContractEventDetailsFactory.class);
        final OkHttpClient okHttpClient = mock(OkHttpClient.class);
        ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
        final SubscriptionService mockSubscriptionService = mock(SubscriptionService.class);
        final ModelMapper modelMapper = new ModelMapper();
        objectMapper = new ObjectMapper();
        Node node = new Node();
        node.setName(NODE_NAME);
        node.setUrl(NODE_URL);
        this.hederaService =
                new HederaService(
                        contractEventDetailsFactory,
                        eventStoreService,
                        objectMapper,
                        node,
                        executorService,
                        modelMapper,
                        okHttpClient,
                        mockBlockManagement,
                        new DummyAsyncTaskService(),
                        mockSubscriptionService);

        remoteCall = mock(Call.class);
        when(okHttpClient.newCall(any())).thenReturn(remoteCall);
    }

    @Test
    void getContractResults() throws IOException, NotFoundException {
        ContractResultsResponse contractResultsResponse = new ContractResultsResponse();
        ContractResultResponse contractResultResponse = new ContractResultResponse();
        BigInteger blockNumber = BigInteger.valueOf(5670700);
        contractResultResponse.setBlockNumber(blockNumber.toString());
        contractResultsResponse.setResults(Collections.singletonList(contractResultResponse));

        final Response httpResponse =
                new Response.Builder()
                        .request(new Request.Builder().url("https://url.com").build())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("")
                        .body(
                                ResponseBody.create(
                                        this.objectMapper.writeValueAsString(
                                                contractResultsResponse),
                                        MediaType.parse("application/json")))
                        .build();

        when(remoteCall.execute()).thenReturn(httpResponse);

        ContractResultsResponse response =
                this.hederaService.getContractResultsByBlock(blockNumber);
        Assert.notNull(response);
        Assert.isTrue(response.getResults().size() == 1);
        Assert.isTrue(response.getResults().getFirst().equals(contractResultResponse));
    }

    @Test
    void getContractResult() throws IOException, NotFoundException {
        ContractResultResponse contractResultResponse = new ContractResultResponse();
        contractResultResponse.setContractId("0.0.1");

        final Response httpResponse =
                new Response.Builder()
                        .request(new Request.Builder().url("https://url.com").build())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("")
                        .body(
                                ResponseBody.create(
                                        this.objectMapper.writeValueAsString(
                                                contractResultResponse),
                                        MediaType.parse("application/json")))
                        .build();

        when(remoteCall.execute()).thenReturn(httpResponse);

        ContractResultResponse response = this.hederaService.getContractResult("51423");
        Assert.notNull(response);
        Assert.isTrue(response.equals(contractResultResponse));
    }

    @Test
    void getContractResultButNotFoundIsExpected() throws IOException {
        ContractResultResponse contractResultResponse = new ContractResultResponse();
        contractResultResponse.setContractId("0.0.1");
        final String url = "https://url.com";

        final Response httpResponse =
                new Response.Builder()
                        .request(new Request.Builder().url(url).build())
                        .protocol(Protocol.HTTP_1_1)
                        .code(404)
                        .message("")
                        .body(
                                ResponseBody.create(
                                        this.objectMapper.writeValueAsString(
                                                contractResultResponse),
                                        MediaType.parse("application/json")))
                        .build();

        when(remoteCall.execute()).thenReturn(httpResponse);

        NotFoundException exception =
                assertThrows(
                        NotFoundException.class,
                        () -> {
                            this.hederaService.getContractResult("51423");
                        });

        Assert.notNull(exception);
        Assert.isTrue(exception.getMessage().contains("Empty response -"));
    }

    @Test
    void getContractResultButBadRequestIsExpected() throws IOException {
        ContractResultResponse contractResultResponse = new ContractResultResponse();
        contractResultResponse.setContractId("0.0.1");
        final String url = "https://url.com";

        final Response httpResponse =
                new Response.Builder()
                        .request(new Request.Builder().url(url).build())
                        .protocol(Protocol.HTTP_1_1)
                        .code(400)
                        .message("")
                        .body(
                                ResponseBody.create(
                                        this.objectMapper.writeValueAsString(
                                                contractResultResponse),
                                        MediaType.parse("application/json")))
                        .build();

        when(remoteCall.execute()).thenReturn(httpResponse);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> {
                            this.hederaService.getContractResult("51423");
                        });

        Assert.notNull(exception);
        Assert.isTrue(exception.getMessage().contains("Invalid request -"));
    }

    @Test
    void getBlock() throws IOException, NotFoundException {
        BlocksResponse blocksResponse = new BlocksResponse();
        BlockResponse blockResponse = new BlockResponse();
        blockResponse.setNumber(BigInteger.ONE);
        blocksResponse.setBlocks(Collections.singletonList(blockResponse));

        final Response httpResponse =
                new Response.Builder()
                        .request(new Request.Builder().url("https://url.com").build())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("")
                        .body(
                                ResponseBody.create(
                                        this.objectMapper.writeValueAsString(blocksResponse),
                                        MediaType.parse("application/json")))
                        .build();

        when(remoteCall.execute()).thenReturn(httpResponse);

        BlockResponse response = this.hederaService.getBlock(BigInteger.ONE);
        Assert.notNull(response);
        Assert.isTrue(response.equals(blockResponse));
    }

    @Test
    void getEventForFilter() {
        ContractEventFilter eventFilter = new ContractEventFilter();
        ContractEventSpecification eventSpecification = new ContractEventSpecification();
        eventSpecification.setEventName("test");
        ParameterDefinition indexedParameter =
                new ParameterDefinition(0, ParameterType.build("STRING"));
        eventSpecification.setIndexedParameterDefinitions(
                Collections.singletonList(indexedParameter));
        ParameterDefinition nonIndexedParameter =
                new ParameterDefinition(0, ParameterType.build("STRING"));
        eventSpecification.setNonIndexedParameterDefinitions(
                Collections.singletonList(nonIndexedParameter));
        eventFilter.setEventSpecification(eventSpecification);

        HederaLogResponse mockLogResponse = new HederaLogResponse();
        mockLogResponse.setData("0x");

        ContractResultResponse mockContractResult = new ContractResultResponse();
        mockContractResult.setHash("0x");
        mockContractResult.setFrom("0x");
        mockContractResult.setBlockHash("0x");
        mockContractResult.setBlockNumber("0");
        mockContractResult.setTimestamp("1664196703.0000000");

        ContractEventDetails eventDetails = new ContractEventDetails();

        when(contractEventDetailsFactory.createEventDetails(
                        eq(eventFilter),
                        any(),
                        eq(new BigInteger("1664196703")),
                        eq(mockContractResult.getFrom())))
                .thenReturn(eventDetails);

        ContractEventDetails event =
                hederaService.getEventForFilter(eventFilter, mockLogResponse, mockContractResult);

        Assert.notNull(event);
        Assert.isTrue(event.equals(eventDetails));
    }
}
