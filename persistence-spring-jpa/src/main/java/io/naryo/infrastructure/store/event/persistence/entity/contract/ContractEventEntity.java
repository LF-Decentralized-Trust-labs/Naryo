package io.naryo.infrastructure.store.event.persistence.entity.contract;

import java.math.BigInteger;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
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

    @EmbeddedId private ContractEventEntityId id;

    private @Column(name = "node_id", nullable = false) String nodeId;

    private @Column(name = "name", nullable = false) String name;

    private @Column(name = "parameters", nullable = false) @JdbcTypeCode(SqlTypes.JSON) Set<
                    ContractEventParameterEntity<?, ?>>
            parameters;

    private @Column(name = "block_number", nullable = false) BigInteger blockNumber;

    private @Column(name = "block_hash", nullable = false) String blockHash;

    private @Column(name = "contract_address", nullable = false) String contractAddress;

    private @Column(name = "sender", nullable = false) String sender;

    private @Column(name = "timestamp", nullable = false) BigInteger timestamp;

    private @Column(name = "status", nullable = false) ContractEventStatus status;

    private ContractEventEntity(
            ContractEventEntityId id,
            String nodeId,
            String name,
            Set<ContractEventParameterEntity<?, ?>> parameters,
            String blockHash,
            BigInteger blockNumber,
            String contractAddress,
            String sender,
            BigInteger timestamp,
            ContractEventStatus status) {
        this.id = id;
        this.nodeId = nodeId;
        this.name = name;
        this.parameters = parameters;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.contractAddress = contractAddress;
        this.sender = sender;
        this.timestamp = timestamp;
        this.status = status;
    }

    public static ContractEventEntity fromContractEvent(ContractEvent event) {
        return new ContractEventEntity(
                new ContractEventEntityId(event.getTransactionHash(), event.getLogIndex()),
                event.getNodeId().toString(),
                event.getName().value(),
                event.getParameters().stream()
                        .map(ContractEventParameterEntity::fromDomain)
                        .collect(Collectors.toSet()),
                event.getBlockHash(),
                event.getBlockNumber(),
                event.getContractAddress(),
                event.getSender(),
                event.getTimestamp(),
                event.getStatus());
    }

    public ContractEvent toContractEvent() {
        return new ContractEvent(
                UUID.fromString(nodeId),
                new EventName(name),
                parameters.stream()
                        .map(ContractEventParameterEntity::toDomain)
                        .collect(Collectors.toSet()),
                id.getTransactionHash(),
                id.getLogIndex(),
                blockNumber,
                blockHash,
                contractAddress,
                sender,
                status,
                timestamp);
    }
}
