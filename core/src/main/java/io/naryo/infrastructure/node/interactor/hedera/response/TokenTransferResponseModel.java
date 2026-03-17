package io.naryo.infrastructure.node.interactor.hedera.response;

import java.math.BigInteger;

public record TokenTransferResponseModel(
        String tokenId, String account, BigInteger amount, Boolean isApproval) {}
