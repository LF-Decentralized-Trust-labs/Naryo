package io.naryo.domain.filter.event;

import io.naryo.domain.DomainBuilder;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.filter.event.sync.block.BlockActiveSyncState;
import org.instancio.Instancio;

public class BlockActiveSyncStateBuilder
        implements DomainBuilder<BlockActiveSyncStateBuilder, BlockActiveSyncState> {

    private NonNegativeBlockNumber initialBlock;
    private NonNegativeBlockNumber lastBlockProcessed;

    @Override
    public BlockActiveSyncStateBuilder self() {
        return this;
    }

    @Override
    public BlockActiveSyncState build() {
        return new BlockActiveSyncState(this.getInitialBlock(), this.getLastBlockProcessed());
    }

    public BlockActiveSyncStateBuilder withInitialBlock(NonNegativeBlockNumber initialBlock) {
        this.initialBlock = initialBlock;
        return this;
    }

    public BlockActiveSyncStateBuilder withLastBlockProcessed(
            NonNegativeBlockNumber lastBlockProcessed) {
        this.lastBlockProcessed = lastBlockProcessed;
        return this;
    }

    private NonNegativeBlockNumber getInitialBlock() {
        return this.initialBlock == null
                ? Instancio.create(NonNegativeBlockNumber.class)
                : this.initialBlock;
    }

    private NonNegativeBlockNumber getLastBlockProcessed() {
        return this.lastBlockProcessed == null
                ? Instancio.create(NonNegativeBlockNumber.class)
                : this.lastBlockProcessed;
    }
}
