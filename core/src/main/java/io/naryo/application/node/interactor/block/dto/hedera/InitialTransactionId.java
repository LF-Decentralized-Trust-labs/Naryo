package io.naryo.application.node.interactor.block.dto.hedera;

import java.math.BigInteger;

public record InitialTransactionId(
        String accountId, BigInteger nonce, Boolean scheduled, String transactionValidStart) {}
