package io.naryo.api.filter.common.response;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.event.*;
import io.naryo.domain.filter.transaction.TransactionFilter;
import lombok.Getter;

@Getter
public abstract class FilterResponse {

    protected UUID id;
    protected String name;
    protected UUID nodeId;
    protected String currentItemHash;

    protected FilterResponse(UUID id, String name, UUID nodeId, String currentItemHash) {
        this.id = id;
        this.name = name;
        this.nodeId = nodeId;
        this.currentItemHash = currentItemHash;
    }

    public static FilterResponse map(Filter filter, Map<UUID, String> fingerprints) {

        Objects.requireNonNull(filter, "filter must not be null");
        String hash = fingerprints.get(filter.getId());

        return switch (filter) {
            case TransactionFilter tf ->
                    new TransactionFilterResponse(
                            tf.getId(),
                            tf.getName().value(),
                            tf.getNodeId(),
                            tf.getIdentifierType(),
                            tf.getValue(),
                            tf.getStatuses(),
                            hash);
            case EventFilter ef -> {
                EventFilterSpecification spec = ef.getSpecification();
                EventFilterVisibilityConfiguration vis = ef.getVisibilityConfiguration();

                String signature = spec.getEventSignature();
                Integer correlation =
                        spec.correlationId() == null ? null : spec.correlationId().position();
                boolean visible = vis.isVisible();
                String privacy = visible ? null : vis.getPrivacyGroupId();

                if (ef instanceof GlobalEventFilter) {
                    yield new GlobalEventFilterResponse(
                            ef.getId(),
                            ef.getName().value(),
                            ef.getNodeId(),
                            signature,
                            correlation,
                            ef.getStatuses(),
                            visible,
                            privacy,
                            hash);
                }
                if (ef instanceof ContractEventFilter cef) {
                    yield new ContractEventFilterResponse(
                            ef.getId(),
                            ef.getName().value(),
                            ef.getNodeId(),
                            signature,
                            correlation,
                            ef.getStatuses(),
                            visible,
                            privacy,
                            cef.getContractAddress(),
                            hash);
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
