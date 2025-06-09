package io.naryo.application.node.helper;

import java.math.BigInteger;
import java.util.Objects;

import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.trigger.disposable.DisposableTrigger;
import io.naryo.application.node.trigger.disposable.block.ContractEventConfirmationDisposableTrigger;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ContractEventDispatcherHelper {

    private final Dispatcher dispatcher;
    private final BlockInteractor interactor;

    public ContractEventDispatcherHelper(Dispatcher dispatcher, BlockInteractor interactor) {
        Objects.requireNonNull(dispatcher, "dispatcher cannot be null");
        Objects.requireNonNull(interactor, "block interactor cannot be null");
        this.interactor = interactor;
        this.dispatcher = dispatcher;
    }

    public void execute(Node node, EventFilter filter, ContractEvent contractEvent) {
        BlockSubscriptionConfiguration configuration =
                (BlockSubscriptionConfiguration) node.getSubscriptionConfiguration();

        if (configuration
                        .getConfirmationBlocks()
                        .isGreaterThan(new NonNegativeBlockNumber(BigInteger.ZERO))
                && filter.getStatuses().contains(ContractEventStatus.CONFIRMED)) {
            contractEvent.setStatus(ContractEventStatus.UNCONFIRMED);

            log.debug("Adding confirmation trigger for {}", contractEvent);
            DisposableTrigger<?> trigger =
                    new ContractEventConfirmationDisposableTrigger(
                            contractEvent,
                            configuration.getConfirmationBlocks(),
                            configuration.getMissingTxRetryBlocks(),
                            configuration.getEventInvalidationBlockThreshold(),
                            dispatcher,
                            interactor);
            dispatcher.addTrigger(trigger);
        }

        if (filter.getStatuses().contains(ContractEventStatus.UNCONFIRMED)) {
            dispatcher.dispatch(contractEvent);
        }
    }
}
