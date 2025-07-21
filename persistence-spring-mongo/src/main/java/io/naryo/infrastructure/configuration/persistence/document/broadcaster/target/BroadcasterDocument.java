package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "broadcasters")
@AllArgsConstructor
public final class BroadcasterDocument implements BroadcasterDescriptor {

    @NotNull
    private final String id;

    @NotNull
    private String configurationId;

    @Valid
    @NotNull
    private BroadcasterTargetDescriptor target;


    @Override
    public UUID id() {
        return UUID.fromString(this.id);
    }

    @Override
    public UUID configurationId() {
        return UUID.fromString(this.configurationId);
    }

    @Override
    public BroadcasterTargetDescriptor target() {
        return this.target;
    }

    @Override
    public void setConfigurationId(UUID configurationId) {
        this.configurationId = configurationId.toString();
    }

    @Override
    public void setTarget(BroadcasterTargetDescriptor target) {
        if (target == null) {
            this.target = null;
            return;
        }

        switch (target.getType()) {
            case BLOCK -> this.target = new BlockBroadcasterTargetDocument(target.getDestination());

            case TRANSACTION -> this.target = new TransactionBroadcasterTargetDocument(target.getDestination());

            case CONTRACT_EVENT -> this.target = new ContractEventBroadcasterTargetDocument(target.getDestination());

            case FILTER -> {
                if (target instanceof FilterBroadcasterTargetDescriptor filterTarget) {
                    this.target = new FilterBroadcasterTargetDocument(
                        filterTarget.getDestination(),
                        filterTarget.getFilterId());
                }
            }

            case ALL -> this.target = new AllBroadcasterTargetDocument(target.getDestination());
        }
    }

}
