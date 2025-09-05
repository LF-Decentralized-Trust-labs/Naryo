package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.IntParameter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class IntParameterEntity extends ContractEventParameterEntity<Integer> {
    private IntParameterEntity(ParameterType type, boolean indexed, int position, Integer value) {
        super(type, indexed, position, value);
    }

    public static ContractEventParameterEntity<?> from(IntParameter intParameter) {
        return new IntParameterEntity(
                intParameter.getType(),
                intParameter.isIndexed(),
                intParameter.getPosition(),
                intParameter.getValue());
    }
}
