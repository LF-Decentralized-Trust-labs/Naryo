package io.librevents.application.node.trigger.disposable.block;

import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.trigger.Trigger;
import io.librevents.domain.common.ContractEventStatus;
import io.librevents.domain.common.EventName;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.event.contract.ContractEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractEventConfirmationDisposableTriggerTest {

    @Mock
    private Dispatcher dispatcher;

    private ContractEventConfirmationDisposableTrigger trigger;
    private ContractEvent contractEvent;
    private BigInteger requiredConfirmations;

    @BeforeEach
    void setUp() {
        // initialize a sample ContractEvent with UNCONFIRMED status
        contractEvent = new ContractEvent(
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
        trigger = new ContractEventConfirmationDisposableTrigger(
            contractEvent,
            requiredConfirmations,
            dispatcher
        );
    }

    @Test
    void trigger_onOrAfterRequiredBlock_dispatchesConfirmedEvent() {
        // block number equal to required confirmations
        BlockEvent blockEvent = new BlockEvent(
            java.util.UUID.randomUUID(),
            new NonNegativeBlockNumber(requiredConfirmations),
            "0xblockHash", // hash
            "0xparentHash", // parent
            BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO,
            java.util.Collections.emptyList()
        );

        trigger.trigger(blockEvent);

        ArgumentCaptor<ContractEvent> captor = ArgumentCaptor.forClass(ContractEvent.class);
        verify(dispatcher).dispatch(captor.capture());
        ContractEvent dispatched = captor.getValue();
        assertNotNull(dispatched);
        assertEquals(ContractEventStatus.CONFIRMED, dispatched.getStatus());
        assertEquals(contractEvent.getBlockHash(), dispatched.getBlockHash());
    }

    @Test
    void trigger_beforeRequiredBlock_doesNotDispatch() {
        // block number less than required confirmations
        BlockEvent blockEvent = new BlockEvent(
            java.util.UUID.randomUUID(),
            new NonNegativeBlockNumber(requiredConfirmations.subtract(BigInteger.ONE)),
            "0xblockHash",
            "0xparentHash",
            BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO,
            java.util.Collections.emptyList()
        );

        trigger.trigger(blockEvent);

        verify(dispatcher, never()).dispatch(any());
    }

    @Test
    void onDispose_registrationDoesNotThrow() {
        // verify onDispose accepts a consumer
        assertDoesNotThrow(() ->
            trigger.onDispose(evt -> {
                // no-op consumer
            })
        );
    }

    @Test
    void supports_onlySupportsBlockEvent() {
        BlockEvent blockEvent = new BlockEvent(
            java.util.UUID.randomUUID(),
            new NonNegativeBlockNumber(requiredConfirmations),
            "h", "p", BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO,
            java.util.Collections.emptyList()
        );
        assertTrue(trigger.supports(blockEvent));

        // does not support other event types
        Trigger<?> generic = trigger;
        ContractEvent otherEvent = contractEvent;
        assertFalse(trigger.supports(otherEvent));
    }
}
