package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.UintParameter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class UintParameterEntity
        extends ContractEventParameterEntity<Integer, UintParameter> {
    private UintParameterEntity(ParameterType type, boolean indexed, int position, Integer value) {
        super(type, indexed, position, value);
    }

    public static UintParameterEntity from(UintParameter uintParameter) {
        return new UintParameterEntity(
                uintParameter.getType(),
                uintParameter.isIndexed(),
                uintParameter.getPosition(),
                uintParameter.getValue());
    }

    @Override
    public UintParameter toDomain() {
        return new UintParameter(isIndexed(), getPosition(), getValue());
    }
}
