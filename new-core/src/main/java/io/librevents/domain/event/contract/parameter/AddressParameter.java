package io.librevents.domain.event.contract.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.ContractEventParameter;

public final class AddressParameter extends ContractEventParameter<String> {
    public AddressParameter(boolean indexed, int position, String value) {
        super(ParameterType.ADDRESS, indexed, position, value);
    }
}
