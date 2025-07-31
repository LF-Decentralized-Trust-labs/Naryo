package io.naryo.application.node.trigger.disposable.block;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.naryo.application.node.trigger.Trigger;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.contract.ContractEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractEventConfirmationDisposableTriggerTest {

    @Mock private Dispatcher dispatcher;
    @Mock private BlockInteractor interactor;

    private ContractEventConfirmationDisposableTrigger trigger;
    private ContractEvent contractEvent;
    private BigInteger requiredConfirmations;

    @BeforeEach
    void setUp() {
        // initialize a sample ContractEvent with UNCONFIRMED status
        contractEvent =
                new ContractEvent(
                        java.util.UUID.randomUUID(),
                        new EventName("testEvent"),
                        Set.of(),
                        "0xabc", // from
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "0xdef", // contract address
                        "0xowner", // owner
                        "0xspender", // spender
                        ContractEventStatus.UNCONFIRMED,
                        BigInteger.ZERO // original block
                        );
        requiredConfirmations = BigInteger.valueOf(5);
        trigger =
                new ContractEventConfirmationDisposableTrigger(
                        contractEvent,
                        new NonNegativeBlockNumber(requiredConfirmations),
                        new NonNegativeBlockNumber(BigInteger.ONE),
                        new NonNegativeBlockNumber(BigInteger.valueOf(10)),
                        dispatcher,
                        interactor);
    }

    @Test
    void trigger_onOrAfterRequiredBlock_dispatchesConfirmedEvent() throws IOException {
        // block number equal to required confirmations
        BlockEvent blockEvent =
                new BlockEvent(
                        java.util.UUID.randomUUID(),
                        new NonNegativeBlockNumber(requiredConfirmations),
                        contractEvent.getBlockHash(), // hash
                        "0xparentHash", // parent
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        java.util.Collections.emptyList());

        doReturn(
                        new TransactionReceipt(
                                contractEvent.getTransactionHash(),
                                BigInteger.ONE,
                                blockEvent.getHash(),
                                BigInteger.TEN,
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "0xabcdef1234567890",
                                "0x0",
                                "0xabcdef1234567890",
                                "0xabcdef1234567890",
                                List.of(),
                                "0x0",
                                "0x0",
                                "0x0"))
                .when(interactor)
                .getTransactionReceipt(contractEvent.getTransactionHash());

        trigger.trigger(blockEvent);

        ArgumentCaptor<ContractEvent> captor = ArgumentCaptor.forClass(ContractEvent.class);
        verify(dispatcher).dispatch(captor.capture());
        ContractEvent dispatched = captor.getValue();
        assertNotNull(dispatched);
        assertEquals(ContractEventStatus.CONFIRMED, dispatched.getStatus());
        assertEquals(contractEvent.getBlockHash(), dispatched.getBlockHash());
    }

    @Test
    void trigger_missingTransaction_invalidateEvent() throws IOException {
        BlockEvent blockEvent =
                new BlockEvent(
                        java.util.UUID.randomUUID(),
                        new NonNegativeBlockNumber(requiredConfirmations),
                        contractEvent.getBlockHash(), // hash
                        "0xparentHash", // parent
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        java.util.Collections.emptyList());

        doReturn(null).when(interactor).getTransactionReceipt(contractEvent.getTransactionHash());

        trigger.trigger(blockEvent);

        BlockEvent secondBlockEvent =
                new BlockEvent(
                        java.util.UUID.randomUUID(),
                        new NonNegativeBlockNumber(requiredConfirmations.add(BigInteger.TWO)),
                        contractEvent.getBlockHash(),
                        "0xparentHash", // parent
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        java.util.Collections.emptyList());
        trigger.trigger(secondBlockEvent);

        ArgumentCaptor<ContractEvent> captor = ArgumentCaptor.forClass(ContractEvent.class);
        verify(dispatcher).dispatch(captor.capture());
        ContractEvent dispatched = captor.getValue();
        assertNotNull(dispatched);
        assertEquals(ContractEventStatus.INVALIDATED, dispatched.getStatus());
        assertEquals(contractEvent.getBlockHash(), dispatched.getBlockHash());
    }

    @Test
    void trigger_missingTransactionReceipt_doCallback() throws IOException {
        BlockEvent blockEvent =
                new BlockEvent(
                        java.util.UUID.randomUUID(),
                        new NonNegativeBlockNumber(requiredConfirmations),
                        contractEvent.getBlockHash(), // hash
                        "0xparentHash", // parent
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        java.util.Collections.emptyList());

        doReturn(null).when(interactor).getTransactionReceipt(contractEvent.getTransactionHash());

        trigger.trigger(blockEvent);

        verify(dispatcher, never()).dispatch(any());
    }

    @Test
    void trigger_beforeRequiredBlock_doesNotDispatch() {
        // block number less than required confirmations
        BlockEvent blockEvent =
                new BlockEvent(
                        java.util.UUID.randomUUID(),
                        new NonNegativeBlockNumber(requiredConfirmations.subtract(BigInteger.ONE)),
                        "0xblockHash",
                        "0xparentHash",
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        java.util.Collections.emptyList());

        trigger.trigger(blockEvent);

        verify(dispatcher, never()).dispatch(any());
    }

    @Test
    void onDispose_registrationDoesNotThrow() {
        // verify onDispose accepts a consumer
        assertDoesNotThrow(
                () ->
                        trigger.onDispose(
                                evt -> {
                                    // no-op consumer
                                }));
    }

    @Test
    void supports_onlySupportsBlockEvent() {
        BlockEvent blockEvent =
                new BlockEvent(
                        java.util.UUID.randomUUID(),
                        new NonNegativeBlockNumber(requiredConfirmations),
                        "h",
                        "p",
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        java.util.Collections.emptyList());
        assertTrue(trigger.supports(blockEvent));

        // does not support other event types
        Trigger<?> generic = trigger;
        ContractEvent otherEvent = contractEvent;
        assertFalse(trigger.supports(otherEvent));
    }
}
