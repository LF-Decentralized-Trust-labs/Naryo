package io.naryo.infrastructure.node.interactor.hedera.response;

import java.math.BigInteger;

public record StakingRewardTransferResponseModel(String account, BigInteger amount) {}
