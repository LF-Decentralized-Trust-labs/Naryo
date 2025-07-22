package fixtures.persistence.filter.event;

import java.math.BigInteger;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.sync.BlockFilterSyncDocument;
import org.instancio.Instancio;

public class BlockFilterSyncDocumentBuilder {

    private BigInteger initialBlock;

    public BlockFilterSyncDocument build() {
        return new BlockFilterSyncDocument(this.getInitialBlock());
    }

    public BlockFilterSyncDocumentBuilder withInitialBlock(BigInteger initialBlock) {
        this.initialBlock = initialBlock;
        return this;
    }

    private BigInteger getInitialBlock() {
        return this.initialBlock == null ? Instancio.create(BigInteger.class) : this.initialBlock;
    }
}
