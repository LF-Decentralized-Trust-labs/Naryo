package fixtures.persistence.filter.event.parameterdefinition;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.BytesFixedParameterDefinitionDocument;

public class BytesFixedParameterDefinitionDocumentBuilder
    extends IndexedParameterDefinitionDocumentBuilder<BytesFixedParameterDefinitionDocumentBuilder, BytesFixedParameterDefinitionDocument> {

    private static final int DEFAULT_BYTE_LENGTH = 1;

    private Integer byteLength;

    @Override
    public BytesFixedParameterDefinitionDocumentBuilder self() {
        return this;
    }

    @Override
    public BytesFixedParameterDefinitionDocument build() {
        return new BytesFixedParameterDefinitionDocument(this.getPosition(), this.isIndexed(), this.getByteLength());
    }

    public BytesFixedParameterDefinitionDocumentBuilder withByteLength(int byteLength) {
        this.byteLength = byteLength;
        return this.self();
    }

    private int getByteLength() {
        return this.byteLength == null
            ? DEFAULT_BYTE_LENGTH
            : this.byteLength;
    }
}
