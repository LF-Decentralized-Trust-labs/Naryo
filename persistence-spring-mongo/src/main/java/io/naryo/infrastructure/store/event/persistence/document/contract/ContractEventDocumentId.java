package io.naryo.infrastructure.store.event.persistence.document.contract;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@Getter
public final class ContractEventDocumentId {
    private final String transactionHash;
    private final BigInteger logIndex;
}
