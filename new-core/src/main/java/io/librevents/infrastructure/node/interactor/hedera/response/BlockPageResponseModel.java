package io.librevents.infrastructure.node.interactor.hedera.response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BlockPageResponseModel implements Page<BlockResponseModel> {

    private final List<BlockResponseModel> blocks;
    private final Map<String, String> links;

    public BlockPageResponseModel(List<BlockResponseModel> blocks, Map<String, String> links) {
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
