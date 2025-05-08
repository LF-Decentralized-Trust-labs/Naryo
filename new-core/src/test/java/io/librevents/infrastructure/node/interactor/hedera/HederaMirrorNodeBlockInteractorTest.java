package io.librevents.infrastructure.node.interactor.hedera;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.dto.Transaction;
import io.librevents.infrastructure.node.interactor.hedera.exception.EmptyResponseException;
import io.librevents.infrastructure.node.interactor.hedera.exception.UnexpectedResponseException;
import io.librevents.infrastructure.node.interactor.hedera.http.MirrorNodeHttpClient;
import io.librevents.infrastructure.node.interactor.hedera.map.BlockConverter;
import io.librevents.infrastructure.node.interactor.hedera.map.TransactionConverter;
import io.librevents.infrastructure.node.interactor.hedera.response.*;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HederaMirrorNodeBlockInteractorTest {

    @Mock private MirrorNodeHttpClient client;

    @Mock private OkHttpClient httpClient;

    @Mock private ObjectMapper objectMapper;

    private ScheduledExecutorService dummyScheduler;
    private HederaMirrorNodeBlockInteractor interactor;

    @BeforeEach
    void setUp() throws Exception {
        when(objectMapper.copy()).thenReturn(objectMapper);
        lenient().doReturn(HttpUrl.parse("https://localhost/api/v1/test")).when(client).url(any());
        dummyScheduler = Executors.newSingleThreadScheduledExecutor();
        interactor =
                new HederaMirrorNodeBlockInteractor(
                        httpClient,
                        objectMapper,
                        "http://localhost",
                        Collections.emptyMap(),
                        Duration.ofMillis(1),
                        1,
                        10,
                        dummyScheduler);
        var clientField = HederaMirrorNodeBlockInteractor.class.getDeclaredField("client");
        clientField.setAccessible(true);
        clientField.set(interactor, client);
    }

    @Test
    void replayPastBlocks_returnsFlowable() throws IOException {
        HederaMirrorNodeBlockInteractor spyInteractor = spy(interactor);
        doReturn(BigInteger.ONE).when(spyInteractor).getCurrentBlockNumber();
        ContractResultListResponseModel list = mock();
        ContractResultResponseModel model = mock();
        doAnswer(
                        invocation -> {
                            TypeReference<?> typeRef = invocation.getArgument(1);
                            if (typeRef.getType()
                                    == new TypeReference<BlockResponseModel>() {}.getType()) {
                                return createFakeBlock(0);
                            }

                            if (typeRef.getType()
                                    == new TypeReference<
                                            ContractResultListResponseModel>() {}.getType()) {
                                doReturn(List.of(model)).when(list).getResults();
                                doReturn(Map.of()).when(list).getLinks();
                                when(model.hash()).thenReturn("0xabc");
                                when(model.nonce()).thenReturn(BigInteger.ZERO);
                                when(model.blockNumber()).thenReturn("0");
                                when(model.blockHash()).thenReturn("0xdef");
                                when(model.from()).thenReturn("0xghi");
                                when(model.to()).thenReturn("0xjkl");
                                return list;
                            }

                            return null;
                        })
                .when(client)
                .get(any(), any());

        var flowable = spyInteractor.replayPastBlocks(BigInteger.ZERO);
        assertNotNull(flowable);
        assertDoesNotThrow(
                () -> {
                    flowable.doOnError(
                                    error -> {
                                        throw new RuntimeException(error);
                                    })
                            .subscribe(
                                    block -> {
                                        assertNotNull(block);
                                        assertEquals(BigInteger.ZERO, block.number());
                                    });
                });
    }

    @Test
    void replayPastAndFutureBlocks_returnsMultipleBlocksFlowable() throws IOException {
        HederaMirrorNodeBlockInteractor spyInteractor = spy(interactor);
        AtomicInteger callCount = new AtomicInteger(0);
        ContractResultListResponseModel list = mock();
        ContractResultResponseModel model = mock();
        lenient()
                .doAnswer(
                        invocation -> {
                            int count = callCount.getAndIncrement();
                            TypeReference<?> typeRef = invocation.getArgument(1);
                            if (typeRef.getType()
                                    == new TypeReference<BlockResponseModel>() {}.getType()) {
                                if (count < 2) {
                                    return createFakeBlock(count);
                                }
                                throw new UnexpectedResponseException("Block not found");
                            }

                            if (typeRef.getType()
                                    == new TypeReference<
                                            ContractResultListResponseModel>() {}.getType()) {
                                doReturn(List.of(model)).when(list).getResults();
                                doReturn(Map.of()).when(list).getLinks();
                                when(model.hash()).thenReturn("0xabc");
                                when(model.nonce()).thenReturn(BigInteger.ZERO);
                                when(model.blockNumber()).thenReturn(String.valueOf(count));
                                when(model.blockHash()).thenReturn("0xdef");
                                when(model.from()).thenReturn("0xghi");
                                when(model.to()).thenReturn("0xjkl");
                                return list;
                            }

                            return null;
                        })
                .when(client)
                .get(any(), any());

        var flowable = spyInteractor.replayPastAndFutureBlocks(BigInteger.ZERO);
        assertNotNull(flowable);
        assertDoesNotThrow(
                () ->
                        flowable.doOnError(
                                        error -> {
                                            throw new RuntimeException(error);
                                        })
                                .subscribe(Assertions::assertNotNull));
    }

    @Test
    void replayFutureBlocks_returnsFlowable() throws IOException {
        HederaMirrorNodeBlockInteractor spyInteractor = spy(interactor);
        doReturn(BigInteger.ONE).when(spyInteractor).getCurrentBlockNumber();
        ContractResultListResponseModel list = mock();
        ContractResultResponseModel model = mock();
        lenient()
                .doAnswer(
                        invocation -> {
                            TypeReference<?> typeRef = invocation.getArgument(1);
                            if (typeRef.getType()
                                    == new TypeReference<BlockResponseModel>() {}.getType()) {
                                return createFakeBlock(1);
                            }

                            if (typeRef.getType()
                                    == new TypeReference<
                                            ContractResultListResponseModel>() {}.getType()) {
                                doReturn(List.of(model)).when(list).getResults();
                                doReturn(Map.of()).when(list).getLinks();
                                when(model.hash()).thenReturn("0xabc");
                                when(model.nonce()).thenReturn(BigInteger.ZERO);
                                when(model.blockNumber()).thenReturn("1");
                                when(model.blockHash()).thenReturn("0xdef");
                                when(model.from()).thenReturn("0xghi");
                                when(model.to()).thenReturn("0xjkl");
                                return list;
                            }

                            return null;
                        })
                .when(client)
                .get(any(), any());

        var flowable = spyInteractor.replyFutureBlocks();
        assertNotNull(flowable);
        assertDoesNotThrow(
                () -> {
                    flowable.doOnError(
                                    error -> {
                                        throw new RuntimeException(error);
                                    })
                            .subscribe(
                                    block -> {
                                        assertNotNull(block);
                                        if (BigInteger.ONE.equals(block.number())) {
                                            throw new RuntimeException();
                                        }
                                    });
                });
    }

    @Test
    void getCurrentBlock_returnsNullWhenNoResults() throws Exception {
        BlockPageResponseModel pageModel = mock(BlockPageResponseModel.class);
        List<BlockResponseModel> results = new ArrayList<>();
        when(pageModel.getResults()).thenReturn(results);
        when(client.get(any(HttpUrl.class), any())).thenReturn(pageModel);
        Block result = interactor.getCurrentBlock();
        assertNull(result);
    }

    @Test
    void getCurrentBlock_returnsMappedBlock() throws Exception {
        BlockPageResponseModel pageModel = mock(BlockPageResponseModel.class);
        List<BlockResponseModel> results =
                List.of(
                        new BlockResponseModel(
                                1,
                                BigInteger.ZERO,
                                "0xabc",
                                "0xdef",
                                "0xghi",
                                "0xjkl",
                                BigInteger.ZERO,
                                "0x123",
                                BigInteger.ZERO,
                                new TimestampResponseModel("10000.0001", "10000.0002")));
        when(pageModel.getResults()).thenReturn(results);
        when(client.get(any(HttpUrl.class), any())).thenReturn(pageModel);

        Block result = interactor.getCurrentBlock();
        assertNotNull(result);
        assertEquals(result.number(), results.getFirst().number());
    }

    @Test
    void getCurrentBlockNumber_returnsValue() throws Exception {
        BlockPageResponseModel pageModel = mock(BlockPageResponseModel.class);
        List<BlockResponseModel> results = new ArrayList<>();
        results.add(
                new BlockResponseModel(
                        1,
                        BigInteger.ZERO,
                        "0xabc",
                        "0xdef",
                        "0xghi",
                        "0xjkl",
                        BigInteger.TEN,
                        "0x123",
                        BigInteger.ZERO,
                        new TimestampResponseModel("10000.0001", "10000.0002")));
        when(pageModel.getResults()).thenReturn(results);
        when(client.get(any(HttpUrl.class), any())).thenReturn(pageModel);

        BigInteger result = interactor.getCurrentBlockNumber();
        assertEquals(BigInteger.TEN, result);
    }

    @Test
    void getCurrentBlockNumber_throwsOnEmpty() throws IOException {
        when(client.get(any(HttpUrl.class), any()))
                .thenReturn(
                        new BlockPageResponseModel(
                                Collections.emptyList(), Collections.emptyMap()));
        assertThrows(EmptyResponseException.class, interactor::getCurrentBlockNumber);
    }

    @Test
    void getBlock_byNumber_and_byHash() throws Exception {
        BlockResponseModel api = mock(BlockResponseModel.class);
        when(client.get(any(HttpUrl.class), any())).thenReturn(api);

        Block expected = mock(Block.class);
        try (MockedStatic<BlockConverter> bconv = Mockito.mockStatic(BlockConverter.class)) {
            bconv.when(() -> BlockConverter.map(eq(api), anyList())).thenReturn(expected);

            Block byNum = interactor.getBlock(BigInteger.TEN);
            Block byHash = interactor.getBlock("0xabc");
            assertSame(expected, byNum);
            assertSame(expected, byHash);
        }
    }

    @Test
    void getTransactionReceipt_success_and_retriesExhausted() throws Exception {
        String tx = "tx1";
        ContractResultResponseModel resp = mock(ContractResultResponseModel.class);

        when(client.get(any(HttpUrl.class), any())).thenReturn(resp);
        Transaction expectedTx = mock(Transaction.class);
        try (MockedStatic<TransactionConverter> tconv =
                Mockito.mockStatic(TransactionConverter.class)) {
            tconv.when(() -> TransactionConverter.map(resp)).thenReturn(expectedTx);
            Transaction result = interactor.getTransactionReceipt(tx);
            assertSame(expectedTx, result);
        }

        interactor =
                new HederaMirrorNodeBlockInteractor(
                        httpClient,
                        objectMapper,
                        "http://localhost",
                        Collections.emptyMap(),
                        Duration.ofMillis(1),
                        0,
                        1,
                        dummyScheduler);
        var fld = HederaMirrorNodeBlockInteractor.class.getDeclaredField("client");
        fld.setAccessible(true);
        fld.set(interactor, client);
        when(client.get(any(HttpUrl.class), any())).thenThrow(new IOException("err"));
        assertThrows(
                UnexpectedResponseException.class, () -> interactor.getTransactionReceipt("tx2"));
    }

    @Test
    void getLogs_byBlockNumber() throws IOException {
        doMockForGetLogsByBlockNumber();

        assertDoesNotThrow(
                () -> {
                    interactor.getLogs(BigInteger.ZERO, BigInteger.ONE);
                });
    }

    @Test
    void getLogs_byBlockNumberAndContract() throws IOException {
        doMockForGetLogsByBlockNumber();

        assertDoesNotThrow(
                () -> {
                    interactor.getLogs(BigInteger.ZERO, BigInteger.ONE, "0xabc");
                });
    }

    @Test
    void getLogs_byBlockNumberAndTopic() throws IOException {
        doMockForGetLogsByBlockNumber();

        assertDoesNotThrow(
                () -> {
                    interactor.getLogs(BigInteger.ZERO, BigInteger.ONE, List.of("0xdef"));
                });
    }

    @Test
    void getLogs_byHash() throws IOException {
        doMockForGetLogsByHash();

        assertDoesNotThrow(
                () -> {
                    interactor.getLogs("0xabc");
                });
    }

    @Test
    void getLogs_byHashAndContract() throws IOException {
        doMockForGetLogsByHash();

        assertDoesNotThrow(
                () -> {
                    interactor.getLogs("0xabc", "0xdef");
                });
    }

    private void doMockForGetLogsByBlockNumber() throws IOException {
        doReturn(createFakeBlock(0))
                .doReturn(createFakeBlock(1))
                .when(client)
                .get(
                        any(HttpUrl.class),
                        argThat(
                                ref ->
                                        ref.getType()
                                                .equals(
                                                        new TypeReference<
                                                                BlockResponseModel>() {}.getType())));
        doMockForGetLogs();
    }

    private void doMockForGetLogsByHash() throws IOException {
        doReturn(createFakeBlock(0))
                .when(client)
                .get(
                        any(HttpUrl.class),
                        argThat(
                                ref ->
                                        ref.getType()
                                                .equals(
                                                        new TypeReference<
                                                                BlockResponseModel>() {}.getType())));
        doMockForGetLogs();
    }

    private void doMockForGetLogs() throws IOException {
        LogPageResponseModel pageModel = mock();
        LogResponseModel model = mock();

        doReturn(List.of(model)).when(pageModel).getResults();
        doReturn(Map.of()).when(pageModel).getLinks();
        doReturn(1).when(model).index();
        doReturn(1).when(model).transactionIndex();
        doReturn("0xabc").when(model).transactionHash();
        doReturn("0xdef").when(model).blockHash();
        doReturn(1).when(model).blockNumber();
        doReturn("0xghi").when(model).address();
        doReturn("0xjkl").when(model).data();
        doReturn(List.of()).when(model).topics();
        when(client.get(
                        any(HttpUrl.class),
                        argThat(
                                ref ->
                                        ref.getType()
                                                .equals(
                                                        new TypeReference<
                                                                LogPageResponseModel>() {}.getType()))))
                .thenReturn(pageModel);
    }

    private BlockResponseModel createFakeBlock(Integer blockNumber) {
        return new BlockResponseModel(
                1,
                BigInteger.ZERO,
                "0xabc",
                "0xdef",
                "0xghi",
                "0xjkl",
                BigInteger.valueOf(blockNumber),
                "0x123",
                BigInteger.ZERO,
                new TimestampResponseModel("10000.0001", "10000.0002"));
    }
}
