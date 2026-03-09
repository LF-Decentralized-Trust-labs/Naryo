package io.naryo.infrastructure.node.interactor.hedera.response;

public record NftTransferResponseModel(
        Boolean isApproval,
        String receiverAccountId,
        String senderAccountId,
        Integer serialNumber,
        String tokenId) {}
