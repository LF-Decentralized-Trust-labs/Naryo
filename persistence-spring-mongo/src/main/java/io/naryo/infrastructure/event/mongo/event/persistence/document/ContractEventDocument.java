package io.naryo.infrastructure.event.mongo.event.persistence.document;

import java.math.BigInteger;
import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.event.contract.ContractEventParameter;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@AllArgsConstructor
public final class ContractEventDocument {
    private final String nodeId;
    private final EventName name;
    private final Set<ContractEventParameter<?>> parameters;
    private final @MongoId String transactionHash;
    private final BigInteger logIndex;
    private final BigInteger blockNumber;
    private final String blockHash;
    private final String contractAddress;
    private final String sender;
    private final BigInteger timestamp;
    private ContractEventStatus status;

    public static ContractEventDocument fromContractEvent(ContractEvent event) {
        return new ContractEventDocument(
                event.getNodeId().toString(),
                event.getName(),
                event.getParameters(),
                event.getTransactionHash(),
                event.getLogIndex(),
                event.getBlockNumber(),
                event.getBlockHash(),
                event.getContractAddress(),
                event.getSender(),
                event.getTimestamp(),
                event.getStatus());
    }

    public ContractEvent toContractEvent() {
        return new ContractEvent(
                UUID.fromString(nodeId),
                name,
                parameters,
                transactionHash,
                logIndex,
                blockNumber,
                blockHash,
                contractAddress,
                sender,
                status,
                timestamp);
    }
}
