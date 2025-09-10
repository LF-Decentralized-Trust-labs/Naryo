package io.naryo.infrastructure.store.event.persistence.document.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;
import io.naryo.domain.event.contract.parameter.ArrayParameter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@TypeAlias("array")
public final class ArrayParameterDocument<V extends ContractEventParameterDocument<?, ?>>
        extends ContractEventParameterDocument<List<V>, ArrayParameter<?>> {

    private ArrayParameterDocument(ParameterType type, boolean indexed, int position, List<V> value) {
        super(type, indexed, position, value);
    }

    public static ArrayParameterDocument<?> fromArrayParameter(ArrayParameter<?> arrayParameter) {
        return new ArrayParameterDocument<>(
                arrayParameter.getType(),
                arrayParameter.isIndexed(),
                arrayParameter.getPosition(),
                arrayParameter.getValue().stream()
                        .map(ContractEventParameterDocument::fromDomain)
                        .toList());
    }

    @Override
    public ArrayParameter<?> toDomain() {
        List<ContractEventParameter<?>> value = new ArrayList<>();

        for (ContractEventParameterDocument<?, ?> entityValue : getValue()) {
            value.add(entityValue.toDomain());
        }

        return new ArrayParameter<>(isIndexed(), getPosition(), value);
    }
}
