package io.naryo.infrastructure.node.interactor.hedera.response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class ContractResultListResponseModel implements Page<ContractResultResponseModel> {

    private final List<ContractResultResponseModel> results;
    @Setter private Map<String, String> links;

    @JsonCreator
    public ContractResultListResponseModel(
            @JsonProperty("results") List<ContractResultResponseModel> results,
            @JsonProperty("links") Map<String, String> links) {
        Objects.requireNonNull(results, "results cannot be null");
        Objects.requireNonNull(links, "links cannot be null");
        this.results = results;
        this.links = links;
    }

    public void add(List<ContractResultResponseModel> newResults) {
        results.addAll(newResults);
    }
}
