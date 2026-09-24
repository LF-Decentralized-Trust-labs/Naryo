package io.naryo.application.node.helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.hedera.TransactionName;
import io.naryo.application.node.trigger.Trigger;
import io.naryo.application.node.trigger.disposable.block.TransactionEventConfirmationDisposableTrigger;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.domain.event.transaction.hedera.HederaTransactionEvent;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.filter.transaction.TransactionFilterBuilder;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.hedera.HederaNodeBuilder;
import io.naryo.domain.node.subscription.BlockSubscriptionConfigurationBuilder;
import io.naryo.infrastructure.node.interactor.hedera.HederaMirrorNodeBlockInteractor;
import io.naryo.infrastructure.node.interactor.hedera.exception.EmptyResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionEventDispatcherHelperTest {

    private static final Set<TransactionStatus> DEFAULT_STATUSES =
            Set.of(TransactionStatus.values());
    private static final BigInteger BLOCK_NUMBER = BigInteger.valueOf(40928000);
    private static final BigInteger CONFIRMATION_BLOCKS = BigInteger.ONE;
    private static final String ENTITY_ID = "0.0.7132200";

    @Mock private Dispatcher dispatcher;
    @Mock private BlockInteractor blockInteractor;
    private Node node;
    private TransactionEventDispatcherHelper helper;
    private final List<TransactionStatus> dispatchedStatuses = new ArrayList<>();
    private final List<Trigger<?>> addedTriggers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        node =
                new HederaNodeBuilder()
                        .withSubscriptionConfiguration(
                                new BlockSubscriptionConfigurationBuilder()
                                        .withConfirmationBlocks(
                                                new NonNegativeBlockNumber(CONFIRMATION_BLOCKS))
                                        .build())
                        .build();
        helper = new TransactionEventDispatcherHelper(dispatcher, blockInteractor);
        lenient()
                .doAnswer(
                        invocation -> {
                            TransactionEvent event = invocation.getArgument(0);
                            dispatchedStatuses.add(event.getStatus());
                            return null;
                        })
                .when(dispatcher)
                .dispatch(any());
        lenient()
                .doAnswer(
                        invocation -> {
                            addedTriggers.add(invocation.getArgument(0));
                            return null;
                        })
                .when(dispatcher)
                .addTrigger(any());
    }

    @Test
    void execute_failedHederaTransactionWithDefaultStatuses_dispatchesItOnceAsFailed() {
        HederaTransactionEvent event =
                hederaEvent(TransactionStatus.FAILED, TransactionName.CRYPTOTRANSFER);

        helper.execute(node, filter(DEFAULT_STATUSES), event);

        assertEquals(List.of(TransactionStatus.FAILED), dispatchedStatuses);
        assertEquals(TransactionStatus.FAILED, event.getStatus());
        verify(dispatcher, never()).addTrigger(any());
    }

    @Test
    void execute_failedHederaTransactionWithFilterWithoutFailed_isNotDispatched() {
        HederaTransactionEvent event =
                hederaEvent(TransactionStatus.FAILED, TransactionName.CRYPTOTRANSFER);

        helper.execute(
                node,
                filter(Set.of(TransactionStatus.CONFIRMED, TransactionStatus.UNCONFIRMED)),
                event);

        assertEquals(List.of(), dispatchedStatuses);
        assertEquals(TransactionStatus.FAILED, event.getStatus());
        verify(dispatcher, never()).addTrigger(any());
    }

    @Test
    void execute_failedHederaTransactionWithConfirmedOnlyFilter_isNeverDispatchedAsConfirmed() {
        HederaTransactionEvent event =
                hederaEvent(TransactionStatus.FAILED, TransactionName.CRYPTOTRANSFER);

        helper.execute(node, filter(Set.of(TransactionStatus.CONFIRMED)), event);
        addedTriggers.forEach(this::reachConfirmationBlock);

        assertEquals(List.of(), dispatchedStatuses);
        assertEquals(TransactionStatus.FAILED, event.getStatus());
    }

    @Test
    void execute_failedHederaTransactionWithFailedOnlyFilter_dispatchesItAsFailed() {
        HederaTransactionEvent event =
                hederaEvent(TransactionStatus.FAILED, TransactionName.CRYPTOTRANSFER);

        helper.execute(node, filter(Set.of(TransactionStatus.FAILED)), event);

        assertEquals(List.of(TransactionStatus.FAILED), dispatchedStatuses);
        verify(dispatcher, never()).addTrigger(any());
    }

    @Test
    void execute_failedConsensusSubmitMessage_doesNotLookUpTheTopicMessage() throws Exception {
        HederaMirrorNodeBlockInteractor interactor = mock(HederaMirrorNodeBlockInteractor.class);
        lenient()
                .when(interactor.getMessage(any()))
                .thenThrow(new EmptyResponseException("Not found"));
        TransactionEventDispatcherHelper mirrorNodeHelper =
                new TransactionEventDispatcherHelper(dispatcher, interactor);
        HederaTransactionEvent event =
                hederaEvent(TransactionStatus.FAILED, TransactionName.CONSENSUSSUBMITMESSAGE);

        assertDoesNotThrow(() -> mirrorNodeHelper.execute(node, filter(DEFAULT_STATUSES), event));

        verify(interactor, never()).getMessage(any());
        assertEquals(List.of(TransactionStatus.FAILED), dispatchedStatuses);
        verify(dispatcher, never()).addTrigger(any());
    }

    @Test
    void execute_successfulHederaTransaction_isConfirmedAfterConfirmationBlocks() {
        HederaTransactionEvent event =
                hederaEvent(TransactionStatus.CONFIRMED, TransactionName.CRYPTOTRANSFER);

        helper.execute(node, filter(DEFAULT_STATUSES), event);
        addedTriggers.forEach(this::reachConfirmationBlock);

        assertEquals(
                List.of(TransactionStatus.UNCONFIRMED, TransactionStatus.CONFIRMED),
                dispatchedStatuses);
        assertEquals(1, addedTriggers.size());
    }

    private void reachConfirmationBlock(Trigger<?> trigger) {
        BlockEvent confirmationBlock = mock(BlockEvent.class);
        when(confirmationBlock.getNumber())
                .thenReturn(new NonNegativeBlockNumber(BLOCK_NUMBER.add(CONFIRMATION_BLOCKS)));
        TransactionEventConfirmationDisposableTrigger confirmationTrigger =
                assertInstanceOf(TransactionEventConfirmationDisposableTrigger.class, trigger);
        confirmationTrigger.onDispose(block -> {});
        confirmationTrigger.trigger(confirmationBlock);
    }

    private TransactionFilter filter(Set<TransactionStatus> statuses) {
        return new TransactionFilterBuilder()
                .withNodeId(node.getId())
                .withIdentifierType(IdentifierType.IDENTITY_ID)
                .withValue(ENTITY_ID)
                .withStatuses(statuses)
                .build();
    }

    private HederaTransactionEvent hederaEvent(TransactionStatus status, TransactionName name) {
        return new HederaTransactionEvent(
                node.getId(),
                "bXQ0UxtT7TLqZR0YVV5n6su+7+uZHU8kvOXxQRNzTIP5lP/92qlnLFltLROpPgvr",
                status,
                new NonNegativeBlockNumber(BLOCK_NUMBER),
                BigInteger.valueOf(1790262037),
                ENTITY_ID,
                "0.0.802",
                "1409749",
                null,
                null,
                1409749,
                ENTITY_ID,
                List.of(),
                "",
                name,
                List.of(),
                "0.0.3",
                0,
                null,
                false,
                List.of(),
                List.of(),
                "0.0.7132200-1790262027-497046104",
                List.of(),
                "120",
                "1790262027.497046104",
                "1790262037.497046104",
                null);
    }
}
