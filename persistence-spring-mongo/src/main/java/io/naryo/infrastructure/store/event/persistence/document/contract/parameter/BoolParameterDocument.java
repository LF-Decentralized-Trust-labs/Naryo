package io.naryo.infrastructure.store.event.persistence.document.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.BoolParameter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@NoArgsConstructor
@TypeAlias("bool")
public final class BoolParameterDocument
        extends ContractEventParameterDocument<Boolean, BoolParameter> {

    private BoolParameterDocument(
            ParameterType type, boolean indexed, int position, Boolean value) {
        super(type, indexed, position, value);
    }

    public static BoolParameterDocument from(BoolParameter boolParameter) {
        return new BoolParameterDocument(
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
