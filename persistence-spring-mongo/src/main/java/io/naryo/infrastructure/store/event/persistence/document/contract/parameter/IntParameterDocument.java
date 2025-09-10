package io.naryo.infrastructure.store.event.persistence.document.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.IntParameter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@NoArgsConstructor
@TypeAlias("int")
public final class IntParameterDocument
        extends ContractEventParameterDocument<Integer, IntParameter> {
    private IntParameterDocument(ParameterType type, boolean indexed, int position, Integer value) {
        super(type, indexed, position, value);
    }

    public static IntParameterDocument from(IntParameter intParameter) {
        return new IntParameterDocument(
                intParameter.getType(),
                intParameter.isIndexed(),
                intParameter.getPosition(),
                intParameter.getValue());
    }

    @Override
    public IntParameter toDomain() {
        return new IntParameter(isIndexed(), getPosition(), getValue());
    }
}
