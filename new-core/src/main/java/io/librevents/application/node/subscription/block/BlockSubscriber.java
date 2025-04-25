package io.librevents.application.node.subscription.block;

import java.math.BigInteger;
import java.util.Objects;

import io.librevents.application.common.Mapper;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.subscription.Subscriber;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.subscription.block.BlockSubscriptionConfiguration;

public abstract class BlockSubscriber implements Subscriber {

    protected final BlockInteractor interactor;
    protected final Dispatcher dispatcher;
    protected final Node node;
    protected final Mapper<Block, BlockEvent> blockMapper;

    protected BlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            Mapper<Block, BlockEvent> blockMapper) {
        Objects.requireNonNull(interactor, "interactor cannot be null");
        Objects.requireNonNull(dispatcher, "dispatcher cannot be null");
        Objects.requireNonNull(node, "node cannot be null");
        Objects.requireNonNull(blockMapper, "blockMapper cannot be null");
        this.interactor = interactor;
        this.dispatcher = dispatcher;
        this.node = node;
        this.blockMapper = blockMapper;
    }

    protected BigInteger getStartBlock() {
        BlockSubscriptionConfiguration configuration =
                (BlockSubscriptionConfiguration) node.getSubscriptionConfiguration();
        if (configuration.getLatestBlock().value().equals(BigInteger.ZERO)) {
            return !configuration.getInitialBlock().isZero()
                    ? configuration.getInitialBlock().value()
                    : interactor.getCurrentBlockNumber();
        }

        BigInteger startBlock = calculateStartBlock(configuration);
        return startBlock.signum() > 0 ? startBlock : BigInteger.ZERO;
    }

    private BigInteger calculateStartBlock(BlockSubscriptionConfiguration configuration) {
        BigInteger startBlock =
                BigInteger.valueOf(configuration.getLatestBlock().value().intValue())
                        .subtract(configuration.getReplayBlockOffset().value());

        BigInteger syncBlockLimit = configuration.getSyncBlockLimit().value();
        if (syncBlockLimit.compareTo(BigInteger.ZERO) > 0) {
            BigInteger currentBlock = interactor.getCurrentBlockNumber();
            if (currentBlock.subtract(startBlock).compareTo(syncBlockLimit) > 0) {
                startBlock = currentBlock.subtract(syncBlockLimit);
            }
        }
        return startBlock;
    }
}
