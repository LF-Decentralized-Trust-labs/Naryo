package fixtures.persistence.filter.event.parameterdefinition;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.ParameterDefinitionDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.StructParameterDefinitionDocument;

import java.util.Set;

public class StructParameterDefinitionDocumentBuilder
    extends ParameterDefinitionDocumentBuilder<StructParameterDefinitionDocumentBuilder, StructParameterDefinitionDocument> {

    private Set<ParameterDefinitionDocument> parameterDefinitions;

    @Override
    public StructParameterDefinitionDocumentBuilder self() {
        return this;
    }

    @Override
    public StructParameterDefinitionDocument build() {
        return new StructParameterDefinitionDocument(this.getPosition(), this.getParameterDefinitions());
    }

    public StructParameterDefinitionDocumentBuilder withParameterDefinitions(Set<ParameterDefinitionDocument> parameterDefinitions) {
        this.parameterDefinitions = parameterDefinitions;
        return this.self();
    }

    private Set<ParameterDefinitionDocument> getParameterDefinitions() {
        return this.parameterDefinitions == null
            ? Set.of(new AddressParameterDefinitionDocumentBuilder().build())
            : this.parameterDefinitions;
    }
}
