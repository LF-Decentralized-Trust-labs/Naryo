package io.naryo.infrastructure.store.event.persistence.document.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.StructParameter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@TypeAlias("struct")
public final class StructParameterDocument
        extends ContractEventParameterDocument<
                        List<ContractEventParameterDocument<?, ?>>, StructParameter> {

    private StructParameterDocument(
            ParameterType type,
            boolean indexed,
            int position,
            List<ContractEventParameterDocument<?, ?>> value) {
        super(type, indexed, position, value);
    }

    public static StructParameterDocument from(StructParameter structParameter) {
        return new StructParameterDocument(
                structParameter.getType(),
                structParameter.isIndexed(),
                structParameter.getPosition(),
                structParameter.getValue().stream()
                        .map(ContractEventParameterDocument::fromDomain)
                        .collect(Collectors.toList()));
    }

    @Override
    public StructParameter toDomain() {
        return new StructParameter(
                isIndexed(),
                getPosition(),
                getValue().stream()
                        .map(ContractEventParameterDocument::toDomain)
                        .collect(Collectors.toList()));
    }
}
