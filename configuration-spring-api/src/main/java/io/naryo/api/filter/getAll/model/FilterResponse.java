package io.naryo.api.filter.getAll.model;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.event.*;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TransactionFilterResponse.class, name = "TRANSACTION"),
    @JsonSubTypes.Type(value = ContractEventFilterResponse.class, name = "EVENT_CONTRACT"),
    @JsonSubTypes.Type(value = GlobalEventFilterResponse.class, name = "EVENT_GLOBAL")
})
@Schema(
        description = "Base filter",
        discriminatorProperty = "type",
        discriminatorMapping = {
            @DiscriminatorMapping(value = "TRANSACTION", schema = TransactionFilterResponse.class),
            @DiscriminatorMapping(
                    value = "EVENT_CONTRACT",
                    schema = ContractEventFilterResponse.class),
            @DiscriminatorMapping(value = "EVENT_GLOBAL", schema = GlobalEventFilterResponse.class)
        })
@Getter
public abstract class FilterResponse {

    protected UUID id;
    protected String name;
    protected UUID nodeId;
    protected String currentItemHash;

    protected FilterResponse(UUID id, String name, UUID nodeId, String currentItemHash) {
        this.id = id;
        this.name = name;
        this.nodeId = nodeId;
        this.currentItemHash = currentItemHash;
    }

    public static FilterResponse map(Filter filter, Map<UUID, String> fingerprints) {
        Objects.requireNonNull(filter, "filter must not be null");
        String currentItemHash = fingerprints.get(filter.getId());

        return switch (filter) {
            case TransactionFilter tf -> TransactionFilterResponse.fromDomain(tf, currentItemHash);
            case EventFilter ef -> EventFilterResponse.fromDomain(ef, currentItemHash);
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported filter type: " + filter.getClass().getName());
        };
    }
}
