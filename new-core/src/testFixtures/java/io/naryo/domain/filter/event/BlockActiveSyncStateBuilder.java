package io.naryo.domain.filter.event;

import io.naryo.domain.filter.event.sync.block.BlockActiveSyncState;
import org.instancio.Instancio;

import java.math.BigInteger;

import static org.instancio.Select.field;

public class BlockActiveSyncStateBuilder {

    private BigInteger initialBlock;
    private BigInteger lastBlockProcessed;

    public BlockActiveSyncState build() {
        return Instancio.of(BlockActiveSyncState.class)
            .set(field(BlockActiveSyncState::getInitialBlock), this.getInitialBlock())
            .set(field(BlockActiveSyncState::getLastBlockProcessed), this.getLastBlockProcessed())
            .create();
    }

    public BlockActiveSyncStateBuilder withInitialBlock(BigInteger initialBlock) {
        this.initialBlock = initialBlock;
        return this;
    }

    public BlockActiveSyncStateBuilder withLastBlockProcessed(BigInteger lastBlockProcessed) {
        this.lastBlockProcessed = lastBlockProcessed;
        return this;
    }

    private BigInteger getInitialBlock() {
        return this.initialBlock == null
            ? Instancio.create(BigInteger.class)
            : this.initialBlock;
    }

    private BigInteger getLastBlockProcessed() {
        return this.lastBlockProcessed == null
            ? Instancio.create(BigInteger.class)
            : this.lastBlockProcessed;
    }
}
