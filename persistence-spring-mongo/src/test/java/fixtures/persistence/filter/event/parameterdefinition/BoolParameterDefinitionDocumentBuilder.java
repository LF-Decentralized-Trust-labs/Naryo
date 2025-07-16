package fixtures.persistence.filter.event.parameterdefinition;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.BoolParameterDefinitionDocument;

public class BoolParameterDefinitionDocumentBuilder
    extends IndexedParameterDefinitionDocumentBuilder<BoolParameterDefinitionDocumentBuilder, BoolParameterDefinitionDocument> {

    @Override
    public BoolParameterDefinitionDocumentBuilder self() {
        return this;
    }

    @Override
    public BoolParameterDefinitionDocument build() {
        return new BoolParameterDefinitionDocument(this.getPosition(), this.isIndexed());
    }
}
