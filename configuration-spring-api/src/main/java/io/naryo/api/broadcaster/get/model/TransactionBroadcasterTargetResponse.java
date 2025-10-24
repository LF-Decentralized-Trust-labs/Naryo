package io.naryo.api.broadcaster.get.model;

import java.util.List;

import io.naryo.domain.broadcaster.target.TransactionBroadcasterTarget;
import io.naryo.domain.common.Destination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Transaction broadcaster target")
@Getter
public final class TransactionBroadcasterTargetResponse extends BroadcasterTargetResponse {

    private TransactionBroadcasterTargetResponse(List<Destination> destinations) {
        super(destinations);
    }

    public static TransactionBroadcasterTargetResponse fromDomain(
            TransactionBroadcasterTarget transactionBroadcasterTarget) {
        return new TransactionBroadcasterTargetResponse(
                transactionBroadcasterTarget.getDestinations());
    }
}
