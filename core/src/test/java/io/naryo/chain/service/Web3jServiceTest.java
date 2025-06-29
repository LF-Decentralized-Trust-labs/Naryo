/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.naryo.chain.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import io.naryo.chain.contract.ContractEventListener;
import io.naryo.chain.factory.ContractEventDetailsFactory;
import io.naryo.chain.service.block.EventBlockManagementService;
import io.naryo.chain.service.domain.Log;
import io.naryo.chain.service.domain.TransactionReceipt;
import io.naryo.chain.service.domain.wrapper.Web3jTransactionReceipt;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.testutils.DummyAsyncTaskService;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.reactivestreams.Subscriber;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Web3jServiceTest {

    private static final String BLOCK_HASH =
            "0xc0e07697167c58f2a173df45f5c9b2c46ca0941cdf0bf79616d53dc92f62aebd";

    private static final BigInteger BLOCK_NUMBER = BigInteger.valueOf(123);

    private static final String CONTRACT_ADDRESS = "0x7a55a28856d43bba3c6a7e36f2cee9a82923e99b";

    private static final String REVERT_REASON = "error";

    private Web3jService underTest;

    private Web3j mockWeb3j;

    private ContractEventDetailsFactory mockContractEventDetailsFactory;

    private ContractEventDetails mockContractEventDetails;

    @BeforeEach
    public void init() throws IOException {
        mockWeb3j = mock(Web3j.class);
        mockContractEventDetailsFactory = mock(ContractEventDetailsFactory.class);
        mockContractEventDetails = mock(ContractEventDetails.class);
        EventBlockManagementService mockBlockManagement = mock(EventBlockManagementService.class);

        // Wire up getBlockNumber
        final Request mockRequest = mock(Request.class);
        final EthBlockNumber blockNumber = new EthBlockNumber();
        blockNumber.setResult("0x0");
        when(mockRequest.send()).thenReturn(blockNumber);
        doReturn(mockRequest).when(mockWeb3j).ethBlockNumber();

        when(mockBlockManagement.getLatestBlockForEvent(any(ContractEventFilter.class)))
                .thenReturn(BLOCK_NUMBER);

        underTest =
                new Web3jService(
                        "test",
                        mockWeb3j,
                        mockContractEventDetailsFactory,
                        new DummyAsyncTaskService(),
                        mockBlockManagement);
    }

    @Test
    void testRegisterEventListener() throws IOException {

        final ContractEventDetails eventDetails = doRegisterEventListenerAndTrigger();

        assertNotNull(eventDetails);
    }

    @Test
    void testEventPassedToListenerIsCorrect() throws IOException {

        final ContractEventDetails eventDetails = doRegisterEventListenerAndTrigger();

        assertEquals(mockContractEventDetails, eventDetails);
    }

    @Test
    void testGetClientVersionHappyPath() throws IOException {
        final Request<?, Web3ClientVersion> mockRequest = mock(Request.class);
        final Web3ClientVersion mockClientVersion = mock(Web3ClientVersion.class);

        when(mockClientVersion.getWeb3ClientVersion()).thenReturn("Version 1.0");
        when(mockRequest.send()).thenReturn(mockClientVersion);
        doReturn(mockRequest).when(mockWeb3j).web3ClientVersion();

        assertEquals("Version 1.0", underTest.getClientVersion());
    }

    @Test
    void testGetClientVersionIOException() throws IOException {
        final Request<?, Web3ClientVersion> mockRequest = mock(Request.class);

        when(mockRequest.send()).thenThrow(new IOException());
        doReturn(mockRequest).when(mockWeb3j).web3ClientVersion();

        Assertions.assertThrows(BlockchainException.class, () -> underTest.getClientVersion());
    }

    @Test
    void testGetTransactionReceiptHappyPath() throws IOException {
        final Request<?, EthGetTransactionReceipt> mockRequest = mock(Request.class);
        final EthGetTransactionReceipt mockGetTxReceipt = mock(EthGetTransactionReceipt.class);
        final org.web3j.protocol.core.methods.response.TransactionReceipt mockTxReceipt =
                createMockTxReceipt();

        final Optional<org.web3j.protocol.core.methods.response.TransactionReceipt>
                txReceiptOptional = Optional.of(mockTxReceipt);
        when(mockGetTxReceipt.getTransactionReceipt()).thenReturn(txReceiptOptional);
        when(mockRequest.send()).thenReturn(mockGetTxReceipt);
        doReturn(mockRequest).when(mockWeb3j).ethGetTransactionReceipt(TX_HASH);

        checkTransactionReceipt(underTest.getTransactionReceipt(TX_HASH));
    }

    @Test
    void testGetTransactionReceiptIOException() throws IOException {
        final Request<?, EthGetTransactionReceipt> mockRequest = mock(Request.class);

        when(mockRequest.send()).thenThrow(new IOException());
        doReturn(mockRequest).when(mockWeb3j).ethGetTransactionReceipt(TX_HASH);

        Assertions.assertThrows(
                BlockchainException.class, () -> underTest.getTransactionReceipt(TX_HASH));
    }

    @Test
    void testGetCurrentBlockNumberHappyPath() throws IOException {
        final Request<?, EthBlockNumber> mockRequest = mock(Request.class);
        final EthBlockNumber mockBlockNumber = mock(EthBlockNumber.class);

        when(mockBlockNumber.getBlockNumber()).thenReturn(BigInteger.TEN);
        when(mockRequest.send()).thenReturn(mockBlockNumber);
        doReturn(mockRequest).when(mockWeb3j).ethBlockNumber();

        assertEquals(BigInteger.TEN, underTest.getCurrentBlockNumber());
    }

    @Test
    void testGetCurrentBlockNumberIOException() throws IOException {
        final Request<?, EthBlockNumber> mockRequest = mock(Request.class);

        when(mockRequest.send()).thenThrow(new IOException());
        doReturn(mockRequest).when(mockWeb3j).ethBlockNumber();

        Assertions.assertThrows(BlockchainException.class, () -> underTest.getCurrentBlockNumber());
    }

    @Test
    void testGetRevertReason() throws IOException {
        final Request<?, EthCall> mockRequest = mock(Request.class);
        final EthCall ethCall = mock(EthCall.class);

        when(ethCall.getRevertReason()).thenReturn(REVERT_REASON);
        when(mockRequest.send()).thenReturn(ethCall);
        doReturn(mockRequest)
                .when(mockWeb3j)
                .ethCall(any(Transaction.class), any(DefaultBlockParameter.class));

        assertEquals(
                REVERT_REASON,
                underTest.getRevertReason(FROM_ADDRESS, TO_ADDRESS, BLOCK_NUMBER, "0x1"));
    }

    private ContractEventDetails doRegisterEventListenerAndTrigger() throws IOException {
        final org.web3j.protocol.core.methods.response.Log mockLog =
                mock(org.web3j.protocol.core.methods.response.Log.class);

        final Flowable<org.web3j.protocol.core.methods.response.Log> flowable =
                new DummyFlowable<>(mockLog);
        when(mockWeb3j.ethLogFlowable(any(EthFilter.class))).thenReturn(flowable);

        final Request<?, EthBlock> mockRequest = mock(Request.class);
        final EthBlock mockBlock = mock(EthBlock.class);

        final Request<?, EthGetTransactionReceipt> mockRequestGetTransactionReceipt =
                mock(Request.class);
        final EthGetTransactionReceipt mockGetTransactionReceipt =
                mock(EthGetTransactionReceipt.class);
        final Optional<org.web3j.protocol.core.methods.response.TransactionReceipt>
                optionalTransactionReceipt =
                        Optional.of(
                                mock(
                                        org.web3j.protocol.core.methods.response.TransactionReceipt
                                                .class));

        when(mockRequest.send()).thenReturn(mockBlock);
        doReturn(mockRequest)
                .when(mockWeb3j)
                .ethGetBlockByNumber(any(DefaultBlockParameterNumber.class), eq(false));

        when(mockLog.getTransactionHash()).thenReturn(TX_HASH);
        when(mockRequestGetTransactionReceipt.send()).thenReturn(mockGetTransactionReceipt);
        when(mockGetTransactionReceipt.getTransactionReceipt())
                .thenReturn(optionalTransactionReceipt);
        doReturn(mockRequestGetTransactionReceipt)
                .when(mockWeb3j)
                .ethGetTransactionReceipt(TX_HASH);

        final ContractEventFilter filter = new ContractEventFilter();

        doReturn(mockContractEventDetails)
                .when(mockContractEventDetailsFactory)
                .createEventDetails(
                        eq(filter), eq(mockLog), eq(mockBlock), any(Web3jTransactionReceipt.class));

        final ContractEventListener mockEventListener = mock(ContractEventListener.class);
        underTest.registerEventListener(filter, mockEventListener);

        final ArgumentCaptor<ContractEventDetails> captor =
                ArgumentCaptor.forClass(ContractEventDetails.class);
        verify(mockEventListener).onEvent(captor.capture());

        return captor.getValue();
    }

    private static final String TX_HASH =
            "0xc283f53461e98400619f3fcdc081e6f95848c7ad32f79718fcb77fe865e5b58a";
    private static final BigInteger TX_INDEX = BigInteger.ONE;
    private static final BigInteger CUMULATIVE_GAS_USED = BigInteger.valueOf(123456);
    private static final BigInteger GAS_USED = BigInteger.valueOf(12345);
    private static final String FROM_ADDRESS = "0x5fd30686247835ee5e96567e29d88bD9A83dca52";
    private static final String TO_ADDRESS = "0x6fd30686247835ee5e96567e29d88bD9A83dca52";
    private static final String LOGS_BLOOM = "bloom";

    private org.web3j.protocol.core.methods.response.TransactionReceipt createMockTxReceipt() {
        final org.web3j.protocol.core.methods.response.TransactionReceipt txReceipt =
                mock(org.web3j.protocol.core.methods.response.TransactionReceipt.class);

        when(txReceipt.getTransactionHash()).thenReturn(TX_HASH);
        when(txReceipt.getTransactionIndex()).thenReturn(TX_INDEX);
        when(txReceipt.getBlockHash()).thenReturn(BLOCK_HASH);
        when(txReceipt.getBlockNumber()).thenReturn(BLOCK_NUMBER);
        when(txReceipt.getCumulativeGasUsed()).thenReturn(CUMULATIVE_GAS_USED);
        when(txReceipt.getGasUsed()).thenReturn(GAS_USED);
        when(txReceipt.getContractAddress()).thenReturn(CONTRACT_ADDRESS);
        when(txReceipt.getFrom()).thenReturn(FROM_ADDRESS);
        when(txReceipt.getTo()).thenReturn(TO_ADDRESS);
        when(txReceipt.getLogsBloom()).thenReturn(LOGS_BLOOM);
        final List<org.web3j.protocol.core.methods.response.Log> mockLogs =
                List.of(createMockTransactionLog(false));
        when(txReceipt.getLogs()).thenReturn(mockLogs);

        return txReceipt;
    }

    private void checkTransactionReceipt(TransactionReceipt txReceipt) {
        assertEquals(TX_HASH, txReceipt.getTransactionHash());
        assertEquals(TX_INDEX, txReceipt.getTransactionIndex());
        assertEquals(BLOCK_HASH, txReceipt.getBlockHash());
        assertEquals(BLOCK_NUMBER, txReceipt.getBlockNumber());
        assertEquals(CUMULATIVE_GAS_USED, txReceipt.getCumulativeGasUsed());
        assertEquals(GAS_USED, txReceipt.getGasUsed());
        assertEquals(CONTRACT_ADDRESS, txReceipt.getContractAddress());
        assertEquals(FROM_ADDRESS, txReceipt.getFrom());
        assertEquals(TO_ADDRESS, txReceipt.getTo());
        assertEquals(LOGS_BLOOM, txReceipt.getLogsBloom());

        checkTransactionLog(txReceipt.getLogs().getFirst());
    }

    private static final BigInteger LOG_INDEX = BigInteger.ZERO;
    private static final String DATA = "1234";
    private static final String TYPE = "log_type";
    private static final String TOPIC = "topic";

    private org.web3j.protocol.core.methods.response.Log createMockTransactionLog(
            boolean isRemoved) {
        final org.web3j.protocol.core.methods.response.Log log =
                mock(org.web3j.protocol.core.methods.response.Log.class);

        when(log.isRemoved()).thenReturn(isRemoved);
        when(log.getLogIndex()).thenReturn(LOG_INDEX);
        when(log.getTransactionIndex()).thenReturn(TX_INDEX);
        when(log.getTransactionHash()).thenReturn(TX_HASH);
        when(log.getBlockHash()).thenReturn(BLOCK_HASH);
        when(log.getBlockNumber()).thenReturn(BLOCK_NUMBER);
        when(log.getAddress()).thenReturn(CONTRACT_ADDRESS);
        when(log.getData()).thenReturn(DATA);
        when(log.getType()).thenReturn(TYPE);
        when(log.getTopics()).thenReturn(List.of(TOPIC));

        return log;
    }

    private void checkTransactionLog(Log log) {
        assertFalse(log.isRemoved());
        assertEquals(LOG_INDEX, log.getLogIndex());
        assertEquals(TX_INDEX, log.getTransactionIndex());
        assertEquals(TX_HASH, log.getTransactionHash());
        assertEquals(BLOCK_HASH, log.getBlockHash());
        assertEquals(BLOCK_NUMBER, log.getBlockNumber());
        assertEquals(CONTRACT_ADDRESS, log.getAddress());
        assertEquals(DATA, log.getData());
        assertEquals(TYPE, log.getType());
        assertEquals(TOPIC, log.getTopics().getFirst());
    }

    private static class DummyFlowable<T> extends Flowable<T> {

        private final T value;

        public DummyFlowable(T value) {
            this.value = value;
        }

        @Override
        protected void subscribeActual(Subscriber<? super T> s) {
            s.onNext(value);
        }
    }
}
