package io.naryo.application.node.subscription.block.factory;

import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.subscription.block.BlockSubscriber;
import io.naryo.domain.node.Node;

public interface BlockSubscriberFactory {

    BlockSubscriber create(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            StartBlockCalculator calculator);
}
