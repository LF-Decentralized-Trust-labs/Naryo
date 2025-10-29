package io.naryo.api.broadcaster.get.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.target.*;
import io.naryo.domain.common.Destination;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AllBroadcasterTargetResponse.class, name = "ALL"),
    @JsonSubTypes.Type(value = BlockBroadcasterTargetResponse.class, name = "BLOCK"),
    @JsonSubTypes.Type(value = FilterBroadcasterTargetResponse.class, name = "FILTER"),
    @JsonSubTypes.Type(value = TransactionBroadcasterTargetResponse.class, name = "TRANSACTION"),
    @JsonSubTypes.Type(
            value = ContractEventBroadcasterTargetResponse.class,
            name = "CONTRACT_EVENT")
})
@Schema(
        description = "Base broadcaster",
        discriminatorProperty = "type",
        discriminatorMapping = {
            @DiscriminatorMapping(value = "ALL", schema = AllBroadcasterTargetResponse.class),
            @DiscriminatorMapping(value = "BLOCK", schema = BlockBroadcasterTargetResponse.class),
            @DiscriminatorMapping(value = "FILTER", schema = FilterBroadcasterTargetResponse.class),
            @DiscriminatorMapping(
                    value = "TRANSACTION",
                    schema = TransactionBroadcasterTargetResponse.class),
            @DiscriminatorMapping(
                    value = "CONTRACT_EVENT",
                    schema = ContractEventBroadcasterTargetResponse.class)
        })
@Getter
public abstract class BroadcasterTargetResponse {

    private final List<String> destinations;

    protected BroadcasterTargetResponse(List<Destination> destinations) {
        this.destinations = destinations.stream().map(Destination::value).toList();
    }

    public static BroadcasterTargetResponse fromDomain(BroadcasterTarget target) {
        return switch (target) {
            case AllBroadcasterTarget abt -> AllBroadcasterTargetResponse.fromDomain(abt);
            case BlockBroadcasterTarget abt -> BlockBroadcasterTargetResponse.fromDomain(abt);
            case FilterEventBroadcasterTarget abt ->
                    FilterBroadcasterTargetResponse.fromDomain(abt);
            case TransactionBroadcasterTarget abt ->
                    TransactionBroadcasterTargetResponse.fromDomain(abt);
            case ContractEventBroadcasterTarget abt ->
                    ContractEventBroadcasterTargetResponse.fromDomain(abt);
            default -> throw new IllegalStateException("Unexpected value: " + target);
        };
    }
}
