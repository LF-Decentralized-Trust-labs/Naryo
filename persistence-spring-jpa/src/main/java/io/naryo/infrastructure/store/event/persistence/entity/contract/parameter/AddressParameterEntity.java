package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.AddressParameter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class AddressParameterEntity extends ContractEventParameterEntity<String> {
    private AddressParameterEntity(
            ParameterType type, boolean indexed, int position, String value) {
        super(type, indexed, position, value);
    }

    public static ContractEventParameterEntity<?> from(AddressParameter addressParameter) {
        return new AddressParameterEntity(
                addressParameter.getType(),
                addressParameter.isIndexed(),
                addressParameter.getPosition(),
                addressParameter.getValue());
    }
}
