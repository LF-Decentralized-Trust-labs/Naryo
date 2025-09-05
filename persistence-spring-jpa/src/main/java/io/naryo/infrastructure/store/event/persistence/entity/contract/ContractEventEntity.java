package io.naryo.infrastructure.store.event.persistence.entity.contract;

import java.math.BigInteger;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.infrastructure.store.event.persistence.entity.contract.parameter.ContractEventParameterEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "contract_event")
@Getter
@NoArgsConstructor
public final class ContractEventEntity {

    private @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "id") UUID id;

    private @Column(name = "node_id", nullable = false) String nodeId;

    private @Column(name = "name", nullable = false) String name;

    private @Column(name = "parameters", nullable = false) @JdbcTypeCode(SqlTypes.JSON) Set<
                    ContractEventParameterEntity<?>>
            parameters;

    private @Column(name = "transaction_hash", nullable = false) String transactionHash;

    private @Column(name = "log_index", nullable = false) BigInteger logIndex;

    private @Column(name = "block_number", nullable = false) BigInteger blockNumber;

    private @Column(name = "block_hash", nullable = false) String blockHash;

    private @Column(name = "contract_address", nullable = false) String contractAddress;

    private @Column(name = "sender", nullable = false) String sender;

    private @Column(name = "timestamp", nullable = false) BigInteger timestamp;

    private @Column(name = "status", nullable = false) ContractEventStatus status;

    private ContractEventEntity(
            String nodeId,
            String name,
            Set<ContractEventParameterEntity<?>> parameters,
            String transactionHash,
            BigInteger logIndex,
            BigInteger blockNumber,
            String blockHash,
            String contractAddress,
            String sender,
            BigInteger timestamp,
            ContractEventStatus status) {
        this.nodeId = nodeId;
        this.name = name;
        this.parameters = parameters;
        this.transactionHash = transactionHash;
        this.logIndex = logIndex;
        this.blockNumber = blockNumber;
        this.blockHash = blockHash;
        this.contractAddress = contractAddress;
        this.sender = sender;
        this.timestamp = timestamp;
        this.status = status;
    }

    public static ContractEventEntity from(ContractEvent event) {
        return new ContractEventEntity(
                event.getNodeId().toString(),
                event.getName().value(),
                event.getParameters().stream()
                        .map(ContractEventParameterEntity::from)
                        .collect(Collectors.toSet()),
                event.getTransactionHash(),
                event.getLogIndex(),
                event.getBlockNumber(),
                event.getBlockHash(),
                event.getContractAddress(),
                event.getSender(),
                event.getTimestamp(),
                event.getStatus());
    }
}
