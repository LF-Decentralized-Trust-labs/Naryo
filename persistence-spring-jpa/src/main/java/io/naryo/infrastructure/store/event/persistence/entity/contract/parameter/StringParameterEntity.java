package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.StringParameter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class StringParameterEntity extends ContractEventParameterEntity<String> {
    private StringParameterEntity(ParameterType type, boolean indexed, int position, String value) {
        super(type, indexed, position, value);
    }

    public static ContractEventParameterEntity<?> from(StringParameter stringParameter) {
        return new StringParameterEntity(
                stringParameter.getType(),
                stringParameter.isIndexed(),
                stringParameter.getPosition(),
                stringParameter.getValue());
    }
}
