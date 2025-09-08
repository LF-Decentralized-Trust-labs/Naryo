package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.BoolParameter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class BoolParameterEntity
        extends ContractEventParameterEntity<Boolean, BoolParameter> {

    private BoolParameterEntity(ParameterType type, boolean indexed, int position, Boolean value) {
        super(type, indexed, position, value);
    }

    public static BoolParameterEntity from(BoolParameter boolParameter) {
        return new BoolParameterEntity(
                boolParameter.getType(),
                boolParameter.isIndexed(),
                boolParameter.getPosition(),
                boolParameter.getValue());
    }

    @Override
    public BoolParameter toDomain() {
        return new BoolParameter(isIndexed(), getPosition(), getValue());
    }
}
