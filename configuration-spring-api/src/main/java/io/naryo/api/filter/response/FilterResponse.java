package io.naryo.api.filter.response;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.event.*;
import io.naryo.domain.filter.transaction.TransactionFilter;

public sealed interface FilterResponse
        permits GlobalEventFilterResponse, ContractEventFilterResponse, TransactionFilterResponse {

    static FilterResponse map(Filter filter, Map<UUID, String> fingerprints) {

        Objects.requireNonNull(filter, "filter must not be null");
        String hash = fingerprints.get(filter.getId());

        return switch (filter) {
            case TransactionFilter tf ->
                    TransactionFilterResponse.builder()
                            .id(tf.getId())
                            .name(tf.getName().value())
                            .nodeId(tf.getNodeId())
                            .identifierType(tf.getIdentifierType())
                            .value(tf.getValue())
                            .statuses(tf.getStatuses())
                            .currentItemHash(hash)
                            .build();
            case EventFilter ef -> {
                EventFilterSpecification spec = ef.getSpecification();
                EventFilterVisibilityConfiguration vis = ef.getVisibilityConfiguration();

                String signature = spec.getEventSignature();
                Integer correlation =
                        spec.correlationId() == null ? null : spec.correlationId().position();
                boolean visible = vis.isVisible();
                String privacy = visible ? null : vis.getPrivacyGroupId();

                if (ef instanceof GlobalEventFilter) {
                    yield GlobalEventFilterResponse.builder()
                            .id(ef.getId())
                            .name(ef.getName().value())
                            .nodeId(ef.getNodeId())
                            .signature(signature)
                            .correlationId(correlation)
                            .statuses(ef.getStatuses())
                            .visible(visible)
                            .privacyGroupId(privacy)
                            .currentItemHash(hash)
                            .build();
                }
                if (ef instanceof ContractEventFilter cef) {
                    yield ContractEventFilterResponse.builder()
                            .id(ef.getId())
                            .name(ef.getName().value())
                            .nodeId(ef.getNodeId())
                            .signature(signature)
                            .correlationId(correlation)
                            .statuses(ef.getStatuses())
                            .visible(visible)
                            .privacyGroupId(privacy)
                            .address(cef.getContractAddress())
                            .currentItemHash(hash)
                            .build();
                } else {
                    throw new IllegalArgumentException(
                            "Unsupported event filter subtype: " + ef.getClass().getName());
                }
            }
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported filter type: " + filter.getClass().getName());
        };
    }
}
