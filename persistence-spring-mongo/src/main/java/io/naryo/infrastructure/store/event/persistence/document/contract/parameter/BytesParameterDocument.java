package io.naryo.infrastructure.store.event.persistence.document.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.BytesParameter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@NoArgsConstructor
@TypeAlias("bytes")
public final class BytesParameterDocument
        extends ContractEventParameterDocument<byte[], BytesParameter> {
    private BytesParameterDocument(
            ParameterType type, boolean indexed, int position, byte[] value) {
        super(type, indexed, position, value);
    }

    public static BytesParameterDocument from(BytesParameter bytesParameter) {
        return new BytesParameterDocument(
                bytesParameter.getType(),
                bytesParameter.isIndexed(),
                bytesParameter.getPosition(),
                bytesParameter.getValue());
    }

    @Override
    public BytesParameter toDomain() {
        return new BytesParameter(isIndexed(), getPosition(), getValue());
    }
}
