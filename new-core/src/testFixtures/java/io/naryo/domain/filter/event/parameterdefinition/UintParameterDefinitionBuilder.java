package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.filter.event.parameter.UintParameterDefinition;

public class UintParameterDefinitionBuilder
        extends ParameterDefinitionBuilder<
                UintParameterDefinitionBuilder, UintParameterDefinition> {

    private static final int DEFAULT_BIT_SIZE = 8;

    private Integer bitSize;

    @Override
    public UintParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public UintParameterDefinition build() {
        return new UintParameterDefinition(this.getBitSize(), this.getPosition(), this.isIndexed());
    }

    public UintParameterDefinitionBuilder withBitSize(int bitSize) {
        this.bitSize = bitSize;
        return this.self();
    }

    private int getBitSize() {
        return this.bitSize == null ? DEFAULT_BIT_SIZE : this.bitSize;
    }
}
