package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.BytesParameter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class BytesParameterEntity
        extends ContractEventParameterEntity<byte[], BytesParameter> {
    private BytesParameterEntity(ParameterType type, boolean indexed, int position, byte[] value) {
        super(type, indexed, position, value);
    }

    public static BytesParameterEntity from(BytesParameter bytesParameter) {
        return new BytesParameterEntity(
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
