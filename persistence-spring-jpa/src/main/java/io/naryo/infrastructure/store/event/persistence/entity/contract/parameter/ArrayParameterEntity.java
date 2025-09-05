package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.ArrayParameter;
import io.naryo.domain.event.contract.parameter.StructParameter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class ArrayParameterEntity<V extends ContractEventParameterEntity<?>>
        extends ContractEventParameterEntity<List<V>> {

    private ArrayParameterEntity(
            ParameterType type,
            boolean indexed,
            int position,
            List<V> value) {
        super(type, indexed, position, value);
    }

    public static ContractEventParameterEntity<?> from(ArrayParameter<?> arrayParameter) {
        return new ArrayParameterEntity<>(
                arrayParameter.getType(),
                arrayParameter.isIndexed(),
                arrayParameter.getPosition(),
                arrayParameter.getValue().stream()
                        .map(ContractEventParameterEntity::from)
                        .collect(Collectors.toList()));
    }
}
