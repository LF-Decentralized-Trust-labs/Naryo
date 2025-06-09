package io.naryo.tests;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.BaseIntegrationTest;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.BlockEvent;
import io.naryo.dto.message.ContractEvent;
import io.naryo.dto.message.TransactionEvent;
import io.naryo.dto.transaction.TransactionStatus;
import io.naryo.model.TransactionIdentifierType;
import io.naryo.utils.EventFilterCreator;
import io.naryo.utils.EventVerification;
import io.naryo.utils.TransactionMonitorCreator;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static io.naryo.utils.StringManipulation.stringToBytes;
import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseBroadcasterTest extends BaseIntegrationTest {

    // "BytesValue" in hex
    protected static final String BYTES_VALUE_HEX =
            "0x427974657356616c756500000000000000000000000000000000000000000000";

    @Test
    void testBroadcastsUnconfirmedEventAfterInitialEmit() throws Exception {
        ContractEventFilter eventFilter =
                EventFilterCreator.buildDummyEventFilter(defaultContract.getContractAddress());
        eventFilterCreator.createFilter(eventFilter);

        TransactionReceipt txReceipt =
                defaultContract
                        .emitEvent(stringToBytes("BytesValue"), BigInteger.TEN, "StringValue")
                        .send();

        verifyEvent(txReceipt, eventFilter);
    }

    @Test
    void testBroadcastNotOrderedEvent() throws Exception {
        ContractEventFilter eventFilter =
                EventFilterCreator.buildDummyEventNotOrderedFilter(
                        defaultContract.getContractAddress());
        eventFilterCreator.createFilter(eventFilter);

        TransactionReceipt txReceipt =
                defaultContract
                        .emitEventNotOrdered(
                                stringToBytes("BytesValue"), BigInteger.TEN, "StringValue")
                        .send();

        verifyEvent(txReceipt, eventFilter);
    }

    @Test
    void testBroadcastsConfirmedEventAfterBlockThresholdReached() throws Exception {
        ContractEventFilter eventFilter =
                EventFilterCreator.buildDummyEventFilter(defaultContract.getContractAddress());
        eventFilterCreator.createFilter(eventFilter);

        TransactionReceipt txReceipt =
                defaultContract
                        .emitEvent(stringToBytes("BytesValue"), BigInteger.TEN, "StringValue")
                        .send();

        verifyEvent(txReceipt, eventFilter);

        mineBlocks(12);

        verifyEvent(txReceipt, eventFilter, ContractEventStatus.CONFIRMED);
    }

    @Test
    void testContractEventForUnregisteredEventFilterNotBroadcast() throws Exception {
        ContractEventFilter eventFilter =
                EventFilterCreator.buildDummyEventFilter(defaultContract.getContractAddress());
        eventFilterCreator.createFilter(eventFilter);

        Thread.sleep(1000);
        eventFilterCreator.removeFilter(eventFilter);

        defaultContract
                .emitEvent(stringToBytes("BytesValue"), BigInteger.TEN, "StringValue")
                .send();

        assertFalse(isEventEmitted(eventFilter));
    }

    @Test
    void testBroadcastBlock() throws Exception {
        BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
        BigInteger expectedBlockNumber = BigInteger.ONE.add(blockNumber);

        mineBlocks(1);

        assertTrue(getBlockEvent(expectedBlockNumber).isPresent());
    }

    @Test
    void testBroadcastsUnconfirmedTransactionAfterInitialMining() throws Exception {
        final String signedTxHex = createRawSignedTransaction(ZERO_ADDRESS);
        final String txHash = Hash.sha3(signedTxHex);
        assertEquals(txHash, sendRawTransaction(signedTxHex));

        transactionMonitorCreator.createTransactionMonitor(
                TransactionMonitorCreator.buildTransactionMonitor(
                        TransactionIdentifierType.HASH, txHash));

        assertTrue(getTransactionEvent(txHash, TransactionStatus.UNCONFIRMED).isPresent());
    }

    @Test
    void testBroadcastsConfirmedTransactionAfterBlockThresholdReached() throws Exception {
        final String signedTxHex = createRawSignedTransaction(ZERO_ADDRESS);
        final String txHash = Hash.sha3(signedTxHex);
        assertEquals(txHash, sendRawTransaction(signedTxHex));

        transactionMonitorCreator.createTransactionMonitor(
                TransactionMonitorCreator.buildTransactionMonitor(
                        TransactionIdentifierType.HASH, txHash));

        assertTrue(getTransactionEvent(txHash, TransactionStatus.UNCONFIRMED).isPresent());

        mineBlocks(12);

        assertTrue(getTransactionEvent(txHash, TransactionStatus.CONFIRMED).isPresent());
    }

    @Test
    void testBroadcastFailedTransactionFilteredByHash() throws Exception {
        final String signedTxHex = createRawSignedTransaction(defaultContract.getContractAddress());
        final String txHash = Hash.sha3(signedTxHex);
        assertEquals(txHash, sendRawTransaction(signedTxHex));

        transactionMonitorCreator.createTransactionMonitor(
                TransactionMonitorCreator.buildTransactionMonitor(
                        TransactionIdentifierType.HASH, txHash));

        assertTrue(getTransactionEvent(txHash, TransactionStatus.FAILED).isPresent());
    }

    @Test
    void testBroadcastFailedTransactionFilteredByTo() throws Exception {
        String address = defaultContract.getContractAddress();
        final String signedTxHex = createRawSignedTransaction(address);
        final String txHash = Hash.sha3(signedTxHex);
        assertEquals(txHash, sendRawTransaction(signedTxHex));

        transactionMonitorCreator.createTransactionMonitor(
                TransactionMonitorCreator.buildTransactionMonitor(
                        TransactionIdentifierType.TO_ADDRESS, address));

        assertTrue(getTransactionEvent(txHash, TransactionStatus.FAILED).isPresent());
    }

    @Test
    void testBroadcastFailedTransactionFilteredByFrom() throws Exception {
        final String signedTxHex = createRawSignedTransaction(defaultContract.getContractAddress());
        final String txHash = Hash.sha3(signedTxHex);
        assertEquals(txHash, sendRawTransaction(signedTxHex));

        transactionMonitorCreator.createTransactionMonitor(
                TransactionMonitorCreator.buildTransactionMonitor(
                        TransactionIdentifierType.FROM_ADDRESS, CREDENTIALS.getAddress()));

        assertTrue(getTransactionEvent(txHash, TransactionStatus.FAILED).isPresent());
    }

    protected abstract Optional<ContractEvent> getContractEvent(
            String transactionHash, ContractEventStatus status);

    protected abstract Optional<BlockEvent> getBlockEvent(BigInteger expectedBlockNumber);

    protected abstract Optional<TransactionEvent> getTransactionEvent(
            String transactionHash, TransactionStatus status);

    protected abstract Boolean isEventEmitted(ContractEventFilter eventFilter);

    private void verifyEvent(TransactionReceipt txReceipt, ContractEventFilter eventFilter) {
        verifyEvent(txReceipt, eventFilter, ContractEventStatus.UNCONFIRMED);
    }

    private void verifyEvent(
            TransactionReceipt txReceipt,
            ContractEventFilter eventFilter,
            ContractEventStatus status) {
        final Optional<ContractEvent> contractEvent =
                getContractEvent(txReceipt.getTransactionHash(), status);
        assertTrue(contractEvent.isPresent());
        final ContractEventDetails eventDetails = contractEvent.get().getDetails();

        EventVerification.verifyDummyEvent(
                eventFilter,
                eventDetails,
                status,
                BYTES_VALUE_HEX,
                Keys.toChecksumAddress(CREDENTIALS.getAddress()),
                BigInteger.TEN,
                "StringValue");
    }
}
