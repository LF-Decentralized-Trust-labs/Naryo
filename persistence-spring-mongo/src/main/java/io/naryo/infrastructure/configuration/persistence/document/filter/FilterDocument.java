package io.naryo.infrastructure.configuration.persistence.document.filter;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.ContractEventFilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventSpecificationDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.FilterVisibilityDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.GlobalEventFilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.sync.FilterSyncDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.transaction.TransactionFilterDocument;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "filters")
@Setter
public abstract class FilterDocument implements FilterDescriptor {

    private final @MongoId String id;

    private @Nullable String name;

    private @Nullable String nodeId;

    public FilterDocument(String id, String name, String nodeId) {
        this.id = id;
        this.name = name;
        this.nodeId = nodeId;
    }

    public UUID getId() {
        return UUID.fromString(this.id);
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<UUID> getNodeId() {
        return nodeId == null ? Optional.empty() : Optional.of(UUID.fromString(nodeId));
    }

    @Override
    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId.toString();
    }

    public static FilterDocument fromDomain(Filter source) {
        String id = source.getId().toString();
        String name = source.getName().value();
        String nodeId = source.getNodeId().toString();
        return switch (source) {
            case ContractEventFilter contract ->
                    new ContractEventFilterDocument(
                            id,
                            name,
                            nodeId,
                            EventSpecificationDocument.fromDomain(contract.getSpecification()),
                            contract.getStatuses(),
                            FilterSyncDocument.fromDomain(contract.getFilterSyncState()),
                            FilterVisibilityDocument.fromDomain(
                                    contract.getVisibilityConfiguration()),
                            contract.getContractAddress());
            case GlobalEventFilter global ->
                    new GlobalEventFilterDocument(
                            id,
                            name,
                            nodeId,
                            EventSpecificationDocument.fromDomain(global.getSpecification()),
                            global.getStatuses(),
                            FilterSyncDocument.fromDomain(global.getFilterSyncState()),
                            FilterVisibilityDocument.fromDomain(
                                    global.getVisibilityConfiguration()));
            case TransactionFilter transaction ->
                    new TransactionFilterDocument(
                            id,
                            name,
                            nodeId,
                            transaction.getIdentifierType(),
                            transaction.getValue(),
                            transaction.getStatuses());
            default ->
                    throw new IllegalStateException(
                            "Unsupported filter type: " + source.getClass().getSimpleName());
        };
    }
}
