package io.naryo.api.broadcaster.common.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.target.*;
import io.naryo.domain.common.Destination;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AllBroadcasterTargetRequest.class, name = "ALL"),
    @JsonSubTypes.Type(value = BlockBroadcasterTargetRequest.class, name = "BLOCK"),
    @JsonSubTypes.Type(value = FilterBroadcasterTargetRequest.class, name = "FILTER"),
    @JsonSubTypes.Type(value = TransactionBroadcasterTargetRequest.class, name = "TRANSACTION"),
    @JsonSubTypes.Type(value = ContractEventBroadcasterTargetRequest.class, name = "CONTRACT_EVENT")
})
@Schema(
        description = "Base class for broadcaster request",
        discriminatorProperty = "type",
        discriminatorMapping = {
            @DiscriminatorMapping(value = "ALL", schema = AllBroadcasterTargetRequest.class),
            @DiscriminatorMapping(value = "BLOCK", schema = BlockBroadcasterTargetRequest.class),
            @DiscriminatorMapping(value = "FILTER", schema = FilterBroadcasterTargetRequest.class),
            @DiscriminatorMapping(
                    value = "TRANSACTION",
                    schema = TransactionBroadcasterTargetRequest.class),
            @DiscriminatorMapping(
                    value = "CONTRACT_EVENT",
                    schema = ContractEventBroadcasterTargetRequest.class)
        })
@Getter
public abstract class BroadcasterTargetRequest {

    private final @NotEmpty List<String> destinations;

    protected BroadcasterTargetRequest(@NotEmpty List<String> destinations) {
        this.destinations = destinations;
    }

    public abstract BroadcasterTarget toDomain();

    public static BroadcasterTargetRequest fromDomain(BroadcasterTarget target) {
        List<String> destinations =
                target.getDestinations().stream().map(Destination::value).toList();
        return switch (target.getType()) {
            case ALL -> new AllBroadcasterTargetRequest(destinations);
            case BLOCK -> new BlockBroadcasterTargetRequest(destinations);
            case FILTER ->
                    new FilterBroadcasterTargetRequest(
                            destinations, ((FilterEventBroadcasterTarget) target).getFilterId());
            case TRANSACTION -> new TransactionBroadcasterTargetRequest(destinations);
            case CONTRACT_EVENT -> new ContractEventBroadcasterTargetRequest(destinations);
        };
    }

    protected List<Destination> getDomainDestinations() {
        return this.destinations.stream().map(Destination::new).toList();
    }
}
