package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameter.StructParameterDefinition;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import java.util.Set;

import static org.instancio.Select.field;

public class StructParameterDefinitionBuilder
    extends ParameterDefinitionBuilder<StructParameterDefinitionBuilder, StructParameterDefinition> {

    private Set<ParameterDefinition> parameterDefinitions;

    @Override
    public StructParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public StructParameterDefinition build() {
        InstancioApi<StructParameterDefinition> builder = Instancio.of(StructParameterDefinition.class);

        return super.buildBase(builder, ParameterType.STRUCT)
            .set(field(StructParameterDefinition::getParameterDefinitions), this.getParameterDefinitions())
            .create();
    }

    public StructParameterDefinitionBuilder withParameterDefinitions(Set<ParameterDefinition> parameterDefinitions) {
        this.parameterDefinitions = parameterDefinitions;
        return this.self();
    }

    private Set<ParameterDefinition> getParameterDefinitions() {
        return this.parameterDefinitions == null
            ? Set.of(new AddressParameterDefinitionBuilder().build())
            : this.parameterDefinitions;
    }
}
