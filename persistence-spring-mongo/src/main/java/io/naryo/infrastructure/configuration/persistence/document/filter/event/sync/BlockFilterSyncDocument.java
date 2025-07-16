package io.naryo.infrastructure.configuration.persistence.document.filter.event.sync;

import io.naryo.application.configuration.source.model.filter.event.sync.BlockFilterSyncDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import java.math.BigInteger;

@TypeAlias("block_sync")
@Getter
@Setter
public class BlockFilterSyncDocument extends FilterSyncDocument implements BlockFilterSyncDescriptor {

    @NotNull
    private BigInteger initialBlock;

    public BlockFilterSyncDocument(BigInteger initialBlock) {
        this.initialBlock = initialBlock;
    }
}
