package io.naryo.api.broadcaster.common.request;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTargetType;
import jakarta.validation.constraints.NotEmpty;

public final class TransactionBroadcasterTargetDTO extends BroadcasterTargetDTO {
    TransactionBroadcasterTargetDTO(@NotEmpty List<String> destinations) {
        super(BroadcasterTargetType.TRANSACTION, destinations);
    }
}
