package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.parameter.IntParameterDefinition;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class IntParameterDefinitionBuilder
    extends ParameterDefinitionBuilder<IntParameterDefinitionBuilder, IntParameterDefinition> {

    private static final int DEFAULT_BIT_SIZE = 8;

    private Integer bitSize;

    @Override
    public IntParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public IntParameterDefinition build() {
        InstancioApi<IntParameterDefinition> builder = Instancio.of(IntParameterDefinition.class);

        return super.buildBase(builder, ParameterType.INT)
            .set(field(IntParameterDefinition::getBitSize), this.getBitSize())
            .create();
    }

    public IntParameterDefinitionBuilder withBitSize(int bitSize) {
        this.bitSize = bitSize;
        return this.self();
    }

    private int getBitSize() {
        return this.bitSize == null
            ? DEFAULT_BIT_SIZE
            : this.bitSize;
    }
}
