package io.librevents.infrastructure.node.interactor.hedera.response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class ContractResultListResponseModel implements Page {

    private final List<ContractResultResponseModel> results;
    @Setter private Map<String, String> links;

    public ContractResultListResponseModel(
            List<ContractResultResponseModel> results, Map<String, String> links) {
        Objects.requireNonNull(results, "results cannot be null");
        Objects.requireNonNull(links, "links cannot be null");
        this.results = results;
        this.links = links;
    }

    public void add(List<ContractResultResponseModel> newResults) {
        results.addAll(newResults);
    }
}
