package fixtures.persistence.filter.event.parameterdefinition;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.ArrayParameterDefinitionDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.ParameterDefinitionDocument;

public class ArrayParameterDefinitionDocumentBuilder
    extends ParameterDefinitionDocumentBuilder<ArrayParameterDefinitionDocumentBuilder, ArrayParameterDefinitionDocument> {

    private ParameterDefinitionDocument elementType;
    private Integer fixedLength;

    @Override
    public ArrayParameterDefinitionDocumentBuilder self() {
        return this;
    }

    @Override
    public ArrayParameterDefinitionDocument build() {
        return new ArrayParameterDefinitionDocument(this.getPosition(), this.getElementType(), this.getFixedLength());
    }

    public ArrayParameterDefinitionDocumentBuilder withElementType(ParameterDefinitionDocument elementType) {
        this.elementType = elementType;
        return this.self();
    }

    public ArrayParameterDefinitionDocumentBuilder withFixedLength(Integer fixedLength) {
        this.fixedLength = fixedLength;
        return this.self();
    }

    private ParameterDefinitionDocument getElementType() {
        return this.elementType == null
            ? new AddressParameterDefinitionDocumentBuilder().build()
            : this.elementType;
    }

    private Integer getFixedLength() {
        return this.fixedLength == null
            ? 1
            : this.fixedLength;
    }
}
