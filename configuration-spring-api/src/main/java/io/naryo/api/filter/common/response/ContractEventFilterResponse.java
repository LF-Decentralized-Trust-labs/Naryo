package io.naryo.api.filter.common.response;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import lombok.Getter;

@Getter
public final class ContractEventFilterResponse extends FilterResponse {

    private final String signature;
    private final Integer correlationId;
    private final Set<ContractEventStatus> statuses;
    private final Boolean visible;
    private final String privacyGroupId;
    private final String address;

    public ContractEventFilterResponse(
            UUID id,
            String name,
            UUID nodeId,
            String signature,
            Integer correlationId,
            Set<ContractEventStatus> statuses,
            Boolean visible,
            String privacyGroupId,
            String address,
            String currentItemHash) {
        super(id, name, nodeId, currentItemHash);
        this.signature = signature;
        this.correlationId = correlationId;
        this.statuses = statuses;
        this.visible = visible;
        this.privacyGroupId = privacyGroupId;
        this.address = address;
    }
}
