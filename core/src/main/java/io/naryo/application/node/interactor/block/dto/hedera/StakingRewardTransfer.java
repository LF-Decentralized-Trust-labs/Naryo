package io.naryo.application.node.interactor.block.dto.hedera;

import java.math.BigInteger;

public record StakingRewardTransfer(String account, BigInteger amount) {}
