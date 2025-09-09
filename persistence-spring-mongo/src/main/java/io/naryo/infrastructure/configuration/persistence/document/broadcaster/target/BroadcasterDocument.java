package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.Optional;
import java.util.UUID;

import com.mongodb.lang.Nullable;
import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "broadcasters")
@AllArgsConstructor
public final class BroadcasterDocument implements BroadcasterDescriptor {

    private final @MongoId String id;

    private @Nullable String configurationId;

    private @Valid @Nullable BroadcasterTargetDocument target;

    @Override
    public UUID getId() {
        return UUID.fromString(this.id);
    }

    @Override
    public Optional<UUID> getConfigurationId() {
        return this.configurationId == null
                ? Optional.empty()
                : Optional.of(UUID.fromString(this.configurationId));
    }

    @Override
    public Optional<BroadcasterTargetDescriptor> getTarget() {
        return Optional.ofNullable(this.target);
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

        switch (target) {
            case BlockBroadcasterTargetDescriptor blockTarget ->
                    this.target = new BlockBroadcasterTargetDocument(blockTarget.getDestinations());

            case TransactionBroadcasterTargetDescriptor transactionTarget ->
                    this.target =
                            new TransactionBroadcasterTargetDocument(
                                    transactionTarget.getDestinations());

            case ContractEventBroadcasterTargetDescriptor contractEventTarget ->
                    this.target =
                            new ContractEventBroadcasterTargetDocument(
                                    contractEventTarget.getDestinations());

            case FilterBroadcasterTargetDescriptor filterTarget ->
                    this.target =
                            new FilterBroadcasterTargetDocument(
                                    filterTarget.getDestinations(), filterTarget.getFilterId());
            case AllBroadcasterTargetDescriptor allTarget ->
                    this.target = new AllBroadcasterTargetDocument(allTarget.getDestinations());
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported target type: " + target.getClass().getSimpleName());
        }
    }
}
