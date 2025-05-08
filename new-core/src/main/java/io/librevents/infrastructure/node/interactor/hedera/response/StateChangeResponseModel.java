package io.librevents.infrastructure.node.interactor.hedera.response;

public record StateChangeResponseModel(
        String address, String contractId, String slot, String valueRead, String valueWritten) {}
