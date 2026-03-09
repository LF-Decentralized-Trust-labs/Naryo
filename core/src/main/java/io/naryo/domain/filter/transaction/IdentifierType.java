package io.naryo.domain.filter.transaction;

public enum IdentifierType {
    HASH,
    TO_ADDRESS,
    FROM_ADDRESS,
    // TODO: Decouple Hedera and Ethereum logic
    IDENTITY_ID
}
