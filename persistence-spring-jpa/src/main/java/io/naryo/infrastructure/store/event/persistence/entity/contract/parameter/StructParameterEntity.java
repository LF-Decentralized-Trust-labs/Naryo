package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import java.util.List;
import java.util.stream.Collectors;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.StructParameter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class StructParameterEntity
        extends ContractEventParameterEntity<
                List<ContractEventParameterEntity<?, ?>>, StructParameter> {

    private StructParameterEntity(
            ParameterType type,
            boolean indexed,
            int position,
            List<ContractEventParameterEntity<?, ?>> value) {
        super(type, indexed, position, value);
    }

    public static StructParameterEntity from(StructParameter structParameter) {
        return new StructParameterEntity(
                structParameter.getType(),
                structParameter.isIndexed(),
                structParameter.getPosition(),
                structParameter.getValue().stream()
                        .map(ContractEventParameterEntity::fromDomain)
                        .collect(Collectors.toList()));
    }

    @Override
    public StructParameter toDomain() {
        return new StructParameter(
                isIndexed(),
                getPosition(),
                getValue().stream()
                        .map(ContractEventParameterEntity::toDomain)
                        .collect(Collectors.toList()));
    }
}
