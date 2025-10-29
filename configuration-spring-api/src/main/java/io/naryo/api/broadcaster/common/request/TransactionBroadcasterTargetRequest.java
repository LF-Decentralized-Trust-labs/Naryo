package io.naryo.api.broadcaster.common.request;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.target.TransactionBroadcasterTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Schema(description = "Transaction broadcaster target request")
@Getter
public final class TransactionBroadcasterTargetRequest extends BroadcasterTargetRequest {

    public TransactionBroadcasterTargetRequest(@NotEmpty List<String> destinations) {
        super(destinations);
    }

    @Override
    public BroadcasterTarget toDomain() {
        return new TransactionBroadcasterTarget(getDomainDestinations());
    }
}
