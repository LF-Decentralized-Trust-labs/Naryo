package fixtures.persistence.filter.event.parameterdefinition;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.AddressParameterDefinitionDocument;

public class AddressParameterDefinitionDocumentBuilder
    extends IndexedParameterDefinitionDocumentBuilder<AddressParameterDefinitionDocumentBuilder, AddressParameterDefinitionDocument> {

    @Override
    public AddressParameterDefinitionDocumentBuilder self() {
        return this;
    }

    @Override
    public AddressParameterDefinitionDocument build() {
        return new AddressParameterDefinitionDocument(this.getPosition(), this.isIndexed());
    }
}
