package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.parameter.UintParameterDefinition;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class UintParameterDefinitionBuilder
    extends ParameterDefinitionBuilder<UintParameterDefinitionBuilder, UintParameterDefinition> {

    private static final int DEFAULT_BIT_SIZE = 8;

    private Integer bitSize;

    @Override
    public UintParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public UintParameterDefinition build() {
        InstancioApi<UintParameterDefinition> builder = Instancio.of(UintParameterDefinition.class);

        return super.buildBase(builder, ParameterType.UINT)
            .set(field(UintParameterDefinition::getBitSize), this.getBitSize())
            .create();
    }

    public UintParameterDefinitionBuilder withBitSize(int bitSize) {
        this.bitSize = bitSize;
        return this.self();
    }

    private int getBitSize() {
        return this.bitSize == null
            ? DEFAULT_BIT_SIZE
            : this.bitSize;
    }
}
