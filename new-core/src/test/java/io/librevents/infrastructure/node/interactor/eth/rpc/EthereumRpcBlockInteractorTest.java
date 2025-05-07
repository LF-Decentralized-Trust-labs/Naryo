package io.librevents.infrastructure.node.interactor.eth.rpc;

import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.dto.Log;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.websocket.events.NewHead;
import org.web3j.protocol.websocket.events.NewHeadsNotification;
import org.web3j.protocol.websocket.events.NotificationParams;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EthereumRpcBlockInteractorTest {

    @Mock private Web3j web3j;

    @Test
    void testConstructor_withNullValues() {
        assertThrows(NullPointerException.class, () -> new EthereumRpcBlockInteractor(null));
    }

    @Test
    void testConstructor_withValidValues() {
        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        assertNotNull(interactor);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetCurrentBlockNumber() throws Exception {
        BigInteger value = BigInteger.valueOf(12345678);
        Request<?, EthBlockNumber> request = Mockito.mock(Request.class);
        EthBlockNumber response = Mockito.mock(EthBlockNumber.class);
        when(web3j.ethBlockNumber()).thenAnswer(invocation -> request);
        when(response.getBlockNumber()).thenReturn(value);
        when(request.send()).thenReturn(response);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        assertEquals(value, interactor.getCurrentBlockNumber());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetCurrentBlock() throws Exception {
        BigInteger value = BigInteger.valueOf(12345678);
        Request<?, EthBlockNumber> request = Mockito.mock(Request.class);
        EthBlockNumber response = Mockito.mock(EthBlockNumber.class);
        when(web3j.ethBlockNumber()).thenAnswer(invocation -> request);
        when(response.getBlockNumber()).thenReturn(value);
        when(request.send()).thenReturn(response);

        Request<?, EthBlock> request2 = Mockito.mock(Request.class);
        EthBlock response2 = createFakeBlock(value, "0x1234567890abcdef");
        when(web3j.ethGetBlockByNumber(any(), anyBoolean())).thenAnswer(invocation -> request2);
        when(request2.send()).thenReturn(response2);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        var actual = interactor.getCurrentBlock();
        assertNotNull(actual);
        assertEquals(value, actual.number());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetBlock() throws Exception {
        BigInteger value = BigInteger.valueOf(12345678);

        Request<?, EthBlock> request = Mockito.mock(Request.class);
        EthBlock response = createFakeBlock(value, "0x1234567890abcdef");
        when(web3j.ethGetBlockByNumber(any(), anyBoolean())).thenAnswer(invocation -> request);
        when(request.send()).thenReturn(response);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        var actual = interactor.getBlock(value);
        assertNotNull(actual);
        assertEquals(response.getBlock().getNumber(), actual.number());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetBlockByHash() throws Exception {
        String hash = "0x1234567890abcdef";

        Request<?, EthBlock> request = Mockito.mock(Request.class);
        EthBlock response = createFakeBlock(BigInteger.ZERO, hash);
        when(web3j.ethGetBlockByHash(any(), anyBoolean())).thenAnswer(invocation -> request);
        when(request.send()).thenReturn(response);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        var actual = interactor.getBlock(hash);
        assertNotNull(actual);
        assertEquals(response.getBlock().getHash(), actual.hash());
    }

    @Test
    void testReplayPastBlocks() {
        EthBlock block = createFakeBlock(BigInteger.ZERO, "0x0");
        when(web3j.replayPastBlocksFlowable(any(), anyBoolean())).thenReturn(Flowable.just(block));
        Flowable<EthBlock> flowable = Flowable.just(block);
        when(web3j.replayPastBlocksFlowable(any(), anyBoolean())).thenReturn(flowable);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        assertNotNull(interactor.replayPastBlocks(BigInteger.ZERO));
    }

    @Test
    void testReplayPastAndFutureBlocks() {
        EthBlock block = createFakeBlock(BigInteger.ZERO, "0x0");
        when(web3j.replayPastAndFutureBlocksFlowable(any(), anyBoolean())).thenReturn(Flowable.just(block));
        Flowable<EthBlock> flowable = Flowable.just(block);
        when(web3j.replayPastAndFutureBlocksFlowable(any(), anyBoolean())).thenReturn(flowable);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        assertNotNull(interactor.replayPastAndFutureBlocks(BigInteger.ZERO));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testReplyFutureBlocks() throws IOException {
        EthBlock block = createFakeBlock(BigInteger.ZERO, "0x0");
        NewHeadsNotification notification = Mockito.mock(NewHeadsNotification.class);
        NewHead newHead = Mockito.mock(NewHead.class);
        NotificationParams<NewHead> params = Mockito.mock(NotificationParams.class);
        when(web3j.newHeadsNotifications()).thenReturn(Flowable.just(notification));
        when(notification.getParams()).thenReturn(params);
        when(params.getResult()).thenReturn(newHead);
        when(newHead.getHash()).thenReturn("0x0");

        Request<?, EthBlock> request = Mockito.mock(Request.class);
        when(web3j.ethGetBlockByHash(any(), anyBoolean())).thenAnswer(invocation -> request);
        when(request.send()).thenReturn(block);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        Flowable<Block> flowable = interactor.replyFutureBlocks();
        assertNotNull(flowable);
        flowable.subscribe(actualBlock -> {
            assertNotNull(actualBlock);
            assertEquals("0x0", actualBlock.hash());
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetLogs_withOnlyNumbers() throws IOException {
        BigInteger startBlock = BigInteger.valueOf(1);
        BigInteger endBlock = BigInteger.valueOf(10);
        Request<?, EthLog> request = Mockito.mock(Request.class);
        EthLog response = createFakeLog();

        when(web3j.ethGetLogs(any())).thenAnswer(invocation -> request);
        when(request.send()).thenReturn(response);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        AtomicReference<List<Log>> logs = new AtomicReference<>();
        assertDoesNotThrow(() -> logs.set(interactor.getLogs(startBlock, endBlock)));
        assertNotNull(logs.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetLogs_withNumbersAndTopics() throws IOException {
        BigInteger startBlock = BigInteger.valueOf(1);
        BigInteger endBlock = BigInteger.valueOf(10);
        List<String> topics = Arrays.asList("topic1", "topic2", "topic3");
        Request<?, EthLog> request = Mockito.mock(Request.class);
        EthLog response = createFakeLog();

        when(web3j.ethGetLogs(any())).thenAnswer(invocation -> request);
        when(request.send()).thenReturn(response);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        AtomicReference<List<Log>> logs = new AtomicReference<>();
        assertDoesNotThrow(() -> logs.set(interactor.getLogs(startBlock, endBlock, topics)));
        assertNotNull(logs.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetLogs_withContractAddress() throws IOException {
        String contractAddress = "0x1234567890abcdef";
        BigInteger startBlock = BigInteger.valueOf(1);
        BigInteger endBlock = BigInteger.valueOf(10);
        Request<?, EthLog> request = Mockito.mock(Request.class);
        EthLog response = createFakeLog();

        when(web3j.ethGetLogs(any())).thenAnswer(invocation -> request);
        when(request.send()).thenReturn(response);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        AtomicReference<List<Log>> logs = new AtomicReference<>();
        assertDoesNotThrow(() -> logs.set(interactor.getLogs(startBlock, endBlock, contractAddress)));
        assertNotNull(logs.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetLogs_withContractAddressAndTopics() throws IOException {
        String contractAddress = "0x1234567890abcdef";
        List<String> topics = Arrays.asList("topic1", "topic2", "topic3");
        BigInteger startBlock = BigInteger.valueOf(1);
        BigInteger endBlock = BigInteger.valueOf(10);
        Request<?, EthLog> request = Mockito.mock(Request.class);
        EthLog response = createFakeLog();

        when(web3j.ethGetLogs(any())).thenAnswer(invocation -> request);
        when(request.send()).thenReturn(response);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        AtomicReference<List<Log>> logs = new AtomicReference<>();
        assertDoesNotThrow(() -> logs.set(interactor.getLogs(startBlock, endBlock, contractAddress, topics)));
        assertNotNull(logs.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetLogs_withBlockHash() throws IOException {
        String blockHash = "0x1234567890abcdef";
        Request<?, EthLog> request = Mockito.mock(Request.class);
        EthLog response = createFakeLog();

        when(web3j.ethGetLogs(any())).thenAnswer(invocation -> request);
        when(request.send()).thenReturn(response);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        AtomicReference<List<Log>> logs = new AtomicReference<>();
        assertDoesNotThrow(() -> logs.set(interactor.getLogs(blockHash)));
        assertNotNull(logs.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetLogs_withBlockHashAndContractAddress() throws IOException {
        String blockHash = "0x1234567890abcdef";
        String contractAddress = "0x1234567890abcdef";
        Request<?, EthLog> request = Mockito.mock(Request.class);
        EthLog response = createFakeLog();

        when(web3j.ethGetLogs(any())).thenAnswer(invocation -> request);
        when(request.send()).thenReturn(response);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        AtomicReference<List<Log>> logs = new AtomicReference<>();
        assertDoesNotThrow(() -> logs.set(interactor.getLogs(blockHash, contractAddress)));
        assertNotNull(logs.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetTransactionReceipt() throws IOException {
        String transactionHash = "0x1234567890abcdef";
        EthGetTransactionReceipt transactionReceipt = new EthGetTransactionReceipt();
        transactionReceipt.setResult(new TransactionReceipt(
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            List.of(),
            "0x0",
            "0x0",
            "0x0",
            "0x0"
        ));

        Request<?, EthGetTransactionReceipt> request = Mockito.mock(Request.class);
        when(web3j.ethGetTransactionReceipt(transactionHash)).thenAnswer(invocation -> request);
        when(request.send()).thenReturn(transactionReceipt);

        EthereumRpcBlockInteractor interactor = new EthereumRpcBlockInteractor(web3j);
        var actual = interactor.getTransactionReceipt(transactionHash);
        assertNotNull(actual);
    }

    private EthBlock createFakeBlock(BigInteger number, String hash) {
        EthBlock response2 = new EthBlock();
        EthBlock.Block result = new EthBlock.Block(
            number.toString(),
            hash,
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            "0x0",
            List.of(new EthBlock.TransactionObject(
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                1,
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                List.of()
            )),
            List.of(),
            List.of(),
            "0x0",
            "0x0",
            List.of(),
            "0x0",
            "0x0"
        );
        response2.setResult(result);
        return response2;
    }

    private EthLog createFakeLog() {
        EthLog response = new EthLog();
        response.setResult(List.of(
            new EthLog.LogObject(
                false,
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                "0x0",
                List.of()
            )
        ));
        return response;
    }
}
