package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.BytesParameter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class BytesParameterEntity extends ContractEventParameterEntity<byte[]> {
    private BytesParameterEntity(ParameterType type, boolean indexed, int position, byte[] value) {
        super(type, indexed, position, value);
    }

    public static ContractEventParameterEntity<?> from(BytesParameter bytesParameter) {
        return new BytesParameterEntity(
                bytesParameter.getType(),
                bytesParameter.isIndexed(),
                bytesParameter.getPosition(),
                bytesParameter.getValue());
    }
}
