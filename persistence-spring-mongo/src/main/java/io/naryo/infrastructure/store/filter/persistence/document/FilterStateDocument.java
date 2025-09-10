package io.naryo.infrastructure.store.filter.persistence.document;

import java.math.BigInteger;
import java.util.UUID;

import io.naryo.application.store.filter.model.FilterState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "filter_state")
@AllArgsConstructor
@Getter
public final class FilterStateDocument {
    private final @MongoId String filterId;
    private final BigInteger latestBlockNumber;
    private final BigInteger endBlock;
    private final Boolean sync;

    public static FilterStateDocument fromFilterState(FilterState filterState) {
        return new FilterStateDocument(
                filterState.filterId().toString(),
                filterState.latestBlockNumber(),
                filterState.endBlock(),
                filterState.sync());
    }

    public FilterState toFilterState() {
        return new FilterState(UUID.fromString(filterId), latestBlockNumber, endBlock, sync);
    }
}
