package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.Optional;
import java.util.UUID;

import com.mongodb.lang.Nullable;
import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Document(collection = "broadcasters")
@AllArgsConstructor
public final class BroadcasterDocument implements BroadcasterDescriptor {

    @MongoId private final String id;

    @Nullable private String configurationId;

    @Valid @Nullable private BroadcasterTargetDocument target;

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

        switch (target.getType()) {
            case BLOCK ->
                    this.target =
                            new BlockBroadcasterTargetDocument(
                                    valueOrNull(target.getDestination()));

            case TRANSACTION ->
                    this.target =
                            new TransactionBroadcasterTargetDocument(
                                    valueOrNull(target.getDestination()));

            case CONTRACT_EVENT ->
                    this.target =
                            new ContractEventBroadcasterTargetDocument(
                                    valueOrNull(target.getDestination()));

            case FILTER -> {
                if (target instanceof FilterBroadcasterTargetDescriptor filterTarget) {
                    this.target =
                            new FilterBroadcasterTargetDocument(
                                    valueOrNull(filterTarget.getDestination()),
                                    filterTarget.getFilterId());
                }
            }

            case ALL ->
                    this.target =
                            new AllBroadcasterTargetDocument(valueOrNull(target.getDestination()));
        }
    }
}
