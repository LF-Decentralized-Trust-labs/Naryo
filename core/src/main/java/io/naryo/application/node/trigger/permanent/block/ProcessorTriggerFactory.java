package io.naryo.application.node.trigger.permanent.block;

import java.util.List;

import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.helper.TransactionEventDispatcherHelper;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.priv.PrivateBlockInteractor;
import io.naryo.application.node.trigger.permanent.PermanentTrigger;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;

public final class ProcessorTriggerFactory {

    private final ContractEventParameterDecoder decoder;

    public ProcessorTriggerFactory(ContractEventParameterDecoder decoder) {
        this.decoder = decoder;
    }

    public PermanentTrigger<?> createBlockTrigger(
            Node node,
            List<EventFilter> filters,
            BlockInteractor interactor,
            ContractEventDispatcherHelper helper) {
        if (node instanceof PrivateEthereumNode privateNode
                && interactor instanceof PrivateBlockInteractor privateInteractor) {
            return new PrivateEthereumBlockProcessorPermanentTrigger(
                    privateNode, filters, privateInteractor, decoder, helper);
        }
        return new BlockProcessorPermanentTrigger<>(node, filters, interactor, decoder, helper);
    }

    public PermanentTrigger<?> createTransactionTrigger(
            Node node,
            List<TransactionFilter> filters,
            BlockInteractor interactor,
            TransactionEventDispatcherHelper helper) {
        if (node instanceof PrivateEthereumNode privateNode
                && interactor instanceof PrivateBlockInteractor privateInteractor) {
            return new PrivateEthereumTransactionProcessorPermanentTrigger(
                    privateNode, filters, helper, privateInteractor);
        }
        return new TransactionProcessorPermanentTrigger<>(node, filters, helper);
    }
}
