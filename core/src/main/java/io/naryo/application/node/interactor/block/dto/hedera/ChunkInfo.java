package io.naryo.application.node.interactor.block.dto.hedera;

import java.math.BigInteger;

public record ChunkInfo(
        InitialTransactionId initialTransactionId, BigInteger number, BigInteger total) {}
