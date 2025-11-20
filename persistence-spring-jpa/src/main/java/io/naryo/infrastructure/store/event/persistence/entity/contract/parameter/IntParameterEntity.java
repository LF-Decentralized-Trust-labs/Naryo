package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import java.math.BigInteger;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.IntParameter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class IntParameterEntity
        extends ContractEventParameterEntity<BigInteger, IntParameter> {
    private IntParameterEntity(
            ParameterType type, boolean indexed, int position, BigInteger value) {
        super(type, indexed, position, value);
    }

    public static IntParameterEntity from(IntParameter intParameter) {
        return new IntParameterEntity(
                intParameter.getType(),
                intParameter.isIndexed(),
                intParameter.getPosition(),
                intParameter.getValue());
    }

    @Override
    public IntParameter toDomain() {
        return new IntParameter(isIndexed(), getPosition(), getValue());
    }
}
