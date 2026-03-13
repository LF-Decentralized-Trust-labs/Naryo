package io.naryo.infrastructure.node.interactor.hedera.response;

import java.math.BigInteger;

public record TransferResponseModel(String account, BigInteger amount, Boolean isApproval) {}
