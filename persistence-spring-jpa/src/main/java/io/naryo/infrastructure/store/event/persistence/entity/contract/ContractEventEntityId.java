package io.naryo.infrastructure.store.event.persistence.entity.contract;

import java.math.BigInteger;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class ContractEventEntityId {
    private @Column(name = "transaction_hash", nullable = false) String transactionHash;

    private @Column(name = "log_index", nullable = false) BigInteger logIndex;
}
