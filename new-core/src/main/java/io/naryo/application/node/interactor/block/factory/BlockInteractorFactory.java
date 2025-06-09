package io.naryo.application.node.interactor.block.factory;

import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.domain.node.Node;

public interface BlockInteractorFactory {

    BlockInteractor create(Node node);
}
