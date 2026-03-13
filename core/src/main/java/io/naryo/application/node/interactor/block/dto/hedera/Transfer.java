package io.naryo.application.node.interactor.block.dto.hedera;

import java.math.BigInteger;

public record Transfer(String account, BigInteger amount, Boolean isApproval) {}
