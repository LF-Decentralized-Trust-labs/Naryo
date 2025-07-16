package fixtures.persistence.filter.event.parameterdefinition;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.IntParameterDefinitionDocument;

public class IntParameterDefinitionDocumentBuilder
    extends IndexedParameterDefinitionDocumentBuilder<IntParameterDefinitionDocumentBuilder, IntParameterDefinitionDocument> {

    private static final int DEFAULT_BIT_SIZE = 8;

    private Integer bitSize;

    @Override
    public IntParameterDefinitionDocumentBuilder self() {
        return this;
    }

    @Override
    public IntParameterDefinitionDocument build() {
        return new IntParameterDefinitionDocument(this.getPosition(), this.isIndexed(), this.getBitSize());
    }

    public IntParameterDefinitionDocumentBuilder withBitSize(int bitSize) {
        this.bitSize = bitSize;
        return this.self();
    }

    private int getBitSize() {
        return this.bitSize == null
            ? DEFAULT_BIT_SIZE
            : this.bitSize;
    }
}
