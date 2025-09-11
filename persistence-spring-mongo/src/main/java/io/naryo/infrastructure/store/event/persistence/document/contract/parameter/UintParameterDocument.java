package io.naryo.infrastructure.store.event.persistence.document.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.UintParameter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@NoArgsConstructor
@TypeAlias("uint")
public final class UintParameterDocument
        extends ContractEventParameterDocument<Integer, UintParameter> {
    private UintParameterDocument(
            ParameterType type, boolean indexed, int position, Integer value) {
        super(type, indexed, position, value);
    }

    public static UintParameterDocument from(UintParameter uintParameter) {
        return new UintParameterDocument(
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
