package io.naryo.infrastructure.store.event.persistence.document.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.parameter.AddressParameter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@NoArgsConstructor
@TypeAlias("address")
public final class AddressParameterDocument
        extends ContractEventParameterDocument<String, AddressParameter> {
    private AddressParameterDocument(
            ParameterType type, boolean indexed, int position, String value) {
        super(type, indexed, position, value);
    }

    public static AddressParameterDocument fromAddressParameter(AddressParameter addressParameter) {
        return new AddressParameterDocument(
                addressParameter.getType(),
                addressParameter.isIndexed(),
                addressParameter.getPosition(),
                addressParameter.getValue());
    }

    @Override
    public AddressParameter toDomain() {
        return new AddressParameter(isIndexed(), getPosition(), getValue());
    }
}
