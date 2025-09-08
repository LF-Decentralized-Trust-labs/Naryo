package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import java.util.ArrayList;
import java.util.List;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;
import io.naryo.domain.event.contract.parameter.ArrayParameter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ArrayParameterEntity<V extends ContractEventParameterEntity<?, ?>>
        extends ContractEventParameterEntity<List<V>, ArrayParameter<?>> {

    private ArrayParameterEntity(ParameterType type, boolean indexed, int position, List<V> value) {
        super(type, indexed, position, value);
    }

    public static ArrayParameterEntity<?> fromArrayParameter(ArrayParameter<?> arrayParameter) {
        return new ArrayParameterEntity<>(
                arrayParameter.getType(),
                arrayParameter.isIndexed(),
                arrayParameter.getPosition(),
                arrayParameter.getValue().stream()
                        .map(ContractEventParameterEntity::fromDomain)
                        .toList());
    }

    @Override
    public ArrayParameter<?> toDomain() {
        List<ContractEventParameter<?>> value = new ArrayList<>();

        for (ContractEventParameterEntity<?, ?> entityValue : getValue()) {
            value.add(entityValue.toDomain());
        }

        return new ArrayParameter<>(isIndexed(), getPosition(), value);
    }
}
