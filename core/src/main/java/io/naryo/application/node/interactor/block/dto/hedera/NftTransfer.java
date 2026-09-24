package io.naryo.application.node.interactor.block.dto.hedera;

public record NftTransfer(
        Boolean isApproval,
        String receiverAccountId,
        String senderAccountId,
        Long serialNumber,
        String tokenId) {}
