package io.naryo.domain.filter;

import java.util.Comparator;
import java.util.Set;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.common.normalization.NormalizationUtil;
import io.naryo.domain.filter.event.*;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.normalization.Normalizer;

public final class FilterNormalizer implements Normalizer<Filter> {

    public static final FilterNormalizer INSTANCE = new FilterNormalizer();

    public FilterNormalizer() {}

    @Override
    public Filter normalize(Filter in) {
        return switch (in) {
            case ContractEventFilter ceIn ->
                    ceIn.toBuilder()
                            .name(normalize(ceIn.getName()))
                            .specification(normalize(ceIn.getSpecification()))
                            .statuses(normalizeCs(ceIn.getStatuses()))
                            .visibilityConfiguration(normalize(ceIn.getVisibilityConfiguration()))
                            .contractAddress(NormalizationUtil.normalize(ceIn.getContractAddress()))
                            .build();
            case GlobalEventFilter geIn ->
                    geIn.toBuilder()
                            .name(normalize(geIn.getName()))
                            .specification(normalize(geIn.getSpecification()))
                            .statuses(normalizeCs(geIn.getStatuses()))
                            .visibilityConfiguration(normalize(geIn.getVisibilityConfiguration()))
                            .build();
            case TransactionFilter taIn ->
                    taIn.toBuilder()
                            .name(normalize(taIn.name))
                            .value(NormalizationUtil.normalize(taIn.getValue()))
                            .statuses(normalizeTs(taIn.getStatuses()))
                            .build();
            default -> throw new IllegalStateException("Unexpected value: " + in);
        };
    }

    private FilterName normalize(FilterName in) {
        if (in == null || in.value() == null) {
            return null;
        }
        return new FilterName(NormalizationUtil.normalize(in.value()));
    }

    private EventFilterSpecification normalize(EventFilterSpecification in) {
        if (in == null) {
            return null;
        }
        var builder = in.toBuilder();

        builder.eventName(new EventName(NormalizationUtil.normalize(in.eventName().value())));
        return builder.parameters(
                        NormalizationUtil.normalize(
                                in.parameters(),
                                Comparator.comparingInt(ParameterDefinition::getPosition)))
                .build();
    }

    private Set<ContractEventStatus> normalizeCs(Set<ContractEventStatus> in) {
        if (in == null) {
            return null;
        }
        return NormalizationUtil.normalize(in, Comparator.comparingInt(Enum::ordinal));
    }

    private EventFilterVisibilityConfiguration normalize(EventFilterVisibilityConfiguration in) {
        if (in == null) {
            return null;
        }
        return in.toBuilder()
                .privacyGroupId(NormalizationUtil.normalize(in.getPrivacyGroupId()))
                .build();
    }

    private Set<TransactionStatus> normalizeTs(Set<TransactionStatus> in) {
        if (in == null) {
            return null;
        }
        return NormalizationUtil.normalize(in, Comparator.comparingInt(Enum::ordinal));
    }
}
