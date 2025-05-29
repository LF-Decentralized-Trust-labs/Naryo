package io.librevents.application.node.interactor.block.factory;

import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.domain.node.Node;

public interface BlockInteractorFactory {

    BlockInteractor create(Node node);
}
