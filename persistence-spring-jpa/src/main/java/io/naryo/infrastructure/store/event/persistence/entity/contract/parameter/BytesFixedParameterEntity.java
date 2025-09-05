package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.BytesFixedParameter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class BytesFixedParameterEntity extends ContractEventParameterEntity<byte[]> {

    private int byteLength;

    private BytesFixedParameterEntity(
            ParameterType type, boolean indexed, int position, byte[] value, int byteLength) {
        super(type, indexed, position, value);
        this.byteLength = byteLength;
    }

    public static ContractEventParameterEntity<?> from(BytesFixedParameter bytesFixedParameter) {
        return new BytesFixedParameterEntity(
                bytesFixedParameter.getType(),
                bytesFixedParameter.isIndexed(),
                bytesFixedParameter.getPosition(),
                bytesFixedParameter.getValue(),
                bytesFixedParameter.getByteLength());
    }
}
