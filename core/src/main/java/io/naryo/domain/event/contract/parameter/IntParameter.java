package io.naryo.domain.event.contract.parameter;

import java.math.BigInteger;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;

public final class IntParameter extends ContractEventParameter<BigInteger> {
    public IntParameter(boolean indexed, int position, BigInteger value) {
        super(ParameterType.INT, indexed, position, value);
    }
}
