package io.librevents.application.node.subscription.block.factory;

import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.subscription.block.BlockSubscriber;
import io.librevents.domain.node.Node;

public interface BlockSubscriberFactory {

    BlockSubscriber create(BlockInteractor interactor, Dispatcher dispatcher, Node node);
}
