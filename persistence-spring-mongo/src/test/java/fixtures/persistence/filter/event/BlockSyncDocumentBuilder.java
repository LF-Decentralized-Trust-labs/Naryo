package fixtures.persistence.filter.event;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.sync.BlockFilterSyncDocument;
import org.instancio.Instancio;

import java.math.BigInteger;

public class BlockSyncDocumentBuilder {

    private BigInteger initialBlock;

    public BlockFilterSyncDocument build() {
        return new BlockFilterSyncDocument(this.getInitialBlock());
    }

    public BlockSyncDocumentBuilder withInitialBlock(BigInteger initialBlock) {
        this.initialBlock = initialBlock;
        return this;
    }

    private BigInteger getInitialBlock() {
        return this.initialBlock == null
            ? Instancio.create(BigInteger.class)
            : this.initialBlock;
    }

}
