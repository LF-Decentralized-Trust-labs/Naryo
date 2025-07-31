package io.naryo.domain.event.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;

public final class StringParameter extends ContractEventParameter<String> {
    public StringParameter(boolean indexed, int position, String value) {
        super(ParameterType.STRING, indexed, position, value);
    }
}
