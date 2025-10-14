package io.naryo.api.filter.common.response;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import lombok.Builder;

@Builder
public record ContractEventFilterResponse(
        UUID id,
        String name,
        UUID nodeId,
        String signature,
        Integer correlationId,
        Set<ContractEventStatus> statuses,
        Boolean visible,
        String privacyGroupId,
        String address,
        String currentItemHash)
        implements FilterResponse {}
