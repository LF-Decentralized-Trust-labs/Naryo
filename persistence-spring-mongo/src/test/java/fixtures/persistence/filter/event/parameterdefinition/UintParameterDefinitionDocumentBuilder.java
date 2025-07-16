package fixtures.persistence.filter.event.parameterdefinition;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.UintParameterDefinitionDocument;

public class UintParameterDefinitionDocumentBuilder
    extends IndexedParameterDefinitionDocumentBuilder<UintParameterDefinitionDocumentBuilder, UintParameterDefinitionDocument> {

    private static final int DEFAULT_BIT_SIZE = 8;

    private Integer bitSize;

    @Override
    public UintParameterDefinitionDocumentBuilder self() {
        return this;
    }

    @Override
    public UintParameterDefinitionDocument build() {
        return new UintParameterDefinitionDocument(this.getPosition(), this.isIndexed(), this.getBitSize());
    }

    public UintParameterDefinitionDocumentBuilder withBitSize(int bitSize) {
        this.bitSize = bitSize;
        return this.self();
    }

    private int getBitSize() {
        return this.bitSize == null
            ? DEFAULT_BIT_SIZE
            : this.bitSize;
    }
}
