package io.naryo.infrastructure.store.event.persistence.document.contract;

import java.math.BigInteger;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.infrastructure.store.event.persistence.document.contract.parameter.ContractEventParameterDocument;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contract_event")
@CompoundIndex(
        name = "uk_transactionHash_logIndex",
        def = "{'transactionHash': 1, 'logIndex': 1}",
        unique = true)
@AllArgsConstructor
public final class ContractEventDocument {
    private final String nodeId;
    private final EventName name;
    private final Set<ContractEventParameterDocument<?, ?>> parameters;
    private final String transactionHash;
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
                event.getParameters().stream()
                        .map(ContractEventParameterDocument::fromDomain)
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

    public ContractEvent toContractEvent() {
        return new ContractEvent(
                UUID.fromString(nodeId),
                name,
                parameters.stream()
                        .map(ContractEventParameterDocument::toDomain)
                        .collect(Collectors.toSet()),
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
