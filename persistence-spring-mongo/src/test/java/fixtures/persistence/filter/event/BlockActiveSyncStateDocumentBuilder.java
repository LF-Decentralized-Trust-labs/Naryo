package fixtures.persistence.filter.event;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.syncstate.BlockActiveSyncStateDocument;
import org.instancio.Instancio;

import java.math.BigInteger;

public class BlockActiveSyncStateDocumentBuilder {

    private BigInteger initialBlock;
    private BigInteger lastBlockProcessed;

    public BlockActiveSyncStateDocument build() {
        return new BlockActiveSyncStateDocument(this.getInitialBlock(), this.getLastBlockProcessed());
    }

    public BlockActiveSyncStateDocumentBuilder withInitialBlock(BigInteger initialBlock) {
        this.initialBlock = initialBlock;
        return this;
    }

    public BlockActiveSyncStateDocumentBuilder withLastBlockProcessed(BigInteger lastBlockProcessed) {
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
