package io.naryo.infrastructure.configuration.persistence.document.filter.event.syncstate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

import java.math.BigInteger;

@TypeAlias("block_active_sync_state")
@Getter
public class BlockActiveSyncStateDocument extends SyncStateDocument {

    @NotNull
    private BigInteger initialBlock;

    @NotNull
    private BigInteger lastBlockProcessed;

    public BlockActiveSyncStateDocument(BigInteger initialBlock, BigInteger lastBlockProcessed) {
        this.initialBlock = initialBlock;
        this.lastBlockProcessed = lastBlockProcessed;
    }
}
