package io.naryo.infrastructure.node.interactor.hedera.response;

import io.naryo.application.node.interactor.block.dto.hedera.BatchKeyType;

public record BatchKeyResponseModel(BatchKeyType type, String key) {}
