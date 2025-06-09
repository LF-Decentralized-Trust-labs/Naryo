package io.naryo.domain.event.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;

public final class AddressParameter extends ContractEventParameter<String> {
    public AddressParameter(boolean indexed, int position, String value) {
        super(ParameterType.ADDRESS, indexed, position, value);
    }
}
