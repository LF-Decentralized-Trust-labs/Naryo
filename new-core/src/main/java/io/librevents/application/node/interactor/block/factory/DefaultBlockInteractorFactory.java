package io.librevents.application.node.interactor.block.factory;

import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.factory.eth.EthereumRpcBlockInteractorFactory;
import io.librevents.application.node.interactor.block.factory.hedera.HederaMirrorNodeBlockInteractorFactory;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.interaction.InteractionStrategy;
import io.librevents.domain.node.interaction.block.BlockInteractionConfiguration;

public final class DefaultBlockInteractorFactory implements BlockInteractorFactory {

    private final EthereumRpcBlockInteractorFactory ethRpcFactory;
    private final HederaMirrorNodeBlockInteractorFactory hederaFactory;

    public DefaultBlockInteractorFactory(
            EthereumRpcBlockInteractorFactory ethRpcFactory,
            HederaMirrorNodeBlockInteractorFactory hederaFactory) {
        this.ethRpcFactory = ethRpcFactory;
        this.hederaFactory = hederaFactory;
    }

    @Override
    public BlockInteractor create(Node node) {
        if (node.getInteractionConfiguration()
                .getStrategy()
                .equals(InteractionStrategy.BLOCK_BASED)) {
            BlockInteractionConfiguration conf =
                    (BlockInteractionConfiguration) node.getInteractionConfiguration();
            return switch (conf.getMode()) {
                case ETHEREUM_RPC -> ethRpcFactory.create(node);
                case HEDERA_MIRROR_NODE -> hederaFactory.create(node);
            };
        } else {
            throw new IllegalArgumentException(
                    "Unsupported interaction strategy: "
                            + node.getInteractionConfiguration().getStrategy());
        }
    }
}
