package io.naryo.infrastructure.store.event.persistence.document.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.BytesFixedParameter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@Getter
@NoArgsConstructor
@TypeAlias("bytes_fixed")
public final class BytesFixedParameterDocument
        extends ContractEventParameterDocument<byte[], BytesFixedParameter> {

    private int byteLength;

    private BytesFixedParameterDocument(
            ParameterType type, boolean indexed, int position, byte[] value, int byteLength) {
        super(type, indexed, position, value);
        this.byteLength = byteLength;
    }

    public static BytesFixedParameterDocument from(BytesFixedParameter bytesFixedParameter) {
        return new BytesFixedParameterDocument(
                bytesFixedParameter.getType(),
                bytesFixedParameter.isIndexed(),
                bytesFixedParameter.getPosition(),
                bytesFixedParameter.getValue(),
                bytesFixedParameter.getByteLength());
    }

    @Override
    public BytesFixedParameter toDomain() {
        return new BytesFixedParameter(isIndexed(), getPosition(), getValue(), getByteLength());
    }
}
