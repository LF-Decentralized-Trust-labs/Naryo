package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.filter.event.parameter.IntParameterDefinition;

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
        return new IntParameterDefinition(this.getBitSize(), this.getPosition(), this.isIndexed());
    }

    public IntParameterDefinitionBuilder withBitSize(int bitSize) {
        this.bitSize = bitSize;
        return this.self();
    }

    private int getBitSize() {
        return this.bitSize == null ? DEFAULT_BIT_SIZE : this.bitSize;
    }
}
