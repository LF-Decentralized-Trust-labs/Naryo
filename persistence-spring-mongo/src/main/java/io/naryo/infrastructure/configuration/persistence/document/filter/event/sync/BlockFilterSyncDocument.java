package io.naryo.infrastructure.configuration.persistence.document.filter.event.sync;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.configuration.source.model.filter.event.sync.BlockFilterSyncDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("block_sync")
@Setter
public class BlockFilterSyncDocument extends FilterSyncDocument
        implements BlockFilterSyncDescriptor {

    private @Nullable BigInteger initialBlock;

    public BlockFilterSyncDocument(BigInteger initialBlock) {
        this.initialBlock = initialBlock;
    }

    @Override
    public Optional<BigInteger> getInitialBlock() {
        return Optional.ofNullable(initialBlock);
    }
}
