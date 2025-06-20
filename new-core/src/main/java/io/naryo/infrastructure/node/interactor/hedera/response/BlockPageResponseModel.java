package io.naryo.infrastructure.node.interactor.hedera.response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockPageResponseModel implements Page<BlockResponseModel> {

    private final List<BlockResponseModel> blocks;
    private final Map<String, String> links;

    @JsonCreator
    public BlockPageResponseModel(
            @JsonProperty("blocks") List<BlockResponseModel> blocks,
            @JsonProperty("links") Map<String, String> links) {
        Objects.requireNonNull(blocks, "blocks cannot be null");
        Objects.requireNonNull(links, "links cannot be null");
        this.blocks = blocks;
        this.links = links;
    }

    @Override
    public List<BlockResponseModel> getResults() {
        return blocks;
    }

    @Override
    public Map<String, String> getLinks() {
        return links;
    }
}
