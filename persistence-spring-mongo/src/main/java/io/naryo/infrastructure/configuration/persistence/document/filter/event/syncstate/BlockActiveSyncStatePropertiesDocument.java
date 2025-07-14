package io.naryo.infrastructure.configuration.persistence.document.filter.event.syncstate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

import java.math.BigInteger;

@TypeAlias("block_active_sync_state")
@Getter
public class BlockActiveSyncStatePropertiesDocument extends SyncStatePropertiesDocument {

    @NotNull
    private BigInteger initialBlock;

    @NotNull
    private BigInteger lastBlockProcessed;

}
