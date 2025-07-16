package fixtures.persistence.filter.event.parameterdefinition;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.BytesParameterDefinitionDocument;

public class BytesParameterDefinitionDocumentBuilder
    extends ParameterDefinitionDocumentBuilder<BytesParameterDefinitionDocumentBuilder, BytesParameterDefinitionDocument> {

    @Override
    public BytesParameterDefinitionDocumentBuilder self() {
        return this;
    }

    @Override
    public BytesParameterDefinitionDocument build() {
        return new BytesParameterDefinitionDocument(this.getPosition());
    }
}
