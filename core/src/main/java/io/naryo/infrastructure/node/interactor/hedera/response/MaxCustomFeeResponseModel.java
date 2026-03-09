package io.naryo.infrastructure.node.interactor.hedera.response;

import java.math.BigInteger;

public record MaxCustomFeeResponseModel(
        String accountId, BigInteger amount, String denominatingTokenId) {}
