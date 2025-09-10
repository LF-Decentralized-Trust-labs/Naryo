package io.naryo.infrastructure.store.event.persistence.document.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.StringParameter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@NoArgsConstructor
@TypeAlias("string")
public final class StringParameterDocument
        extends ContractEventParameterDocument<String, StringParameter> {
    private StringParameterDocument(ParameterType type, boolean indexed, int position, String value) {
        super(type, indexed, position, value);
    }

    public static StringParameterDocument from(StringParameter stringParameter) {
        return new StringParameterDocument(
                stringParameter.getType(),
                stringParameter.isIndexed(),
                stringParameter.getPosition(),
                stringParameter.getValue());
    }

    @Override
    public StringParameter toDomain() {
        return new StringParameter(isIndexed(), getPosition(), getValue());
    }
}
