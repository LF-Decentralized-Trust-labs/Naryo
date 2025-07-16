package fixtures.persistence.filter.event.parameterdefinition;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.StringParameterDefinitionDocument;

public class StringParameterDefinitionDocumentBuilder extends
    ParameterDefinitionDocumentBuilder<StringParameterDefinitionDocumentBuilder, StringParameterDefinitionDocument> {

    @Override
    public StringParameterDefinitionDocumentBuilder self() {
        return this;
    }

    @Override
    public StringParameterDefinitionDocument build() {
        return new StringParameterDefinitionDocument(this.getPosition());
    }
}
