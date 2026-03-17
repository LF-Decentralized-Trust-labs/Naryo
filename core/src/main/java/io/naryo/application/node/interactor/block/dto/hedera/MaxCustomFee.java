package io.naryo.application.node.interactor.block.dto.hedera;

import java.math.BigInteger;

public record MaxCustomFee(String accountId, BigInteger amount, String denominatingTokenId) {}
