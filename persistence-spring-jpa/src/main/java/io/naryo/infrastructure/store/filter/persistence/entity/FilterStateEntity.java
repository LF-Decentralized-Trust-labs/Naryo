package io.naryo.infrastructure.store.filter.persistence.entity;

import java.math.BigInteger;
import java.util.UUID;

import io.naryo.application.store.filter.model.FilterState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "filter_state")
@Getter
@NoArgsConstructor
public final class FilterStateEntity {

    private @Id @Column(name = "filter_id", nullable = false) UUID filterId;

    private @Column(name = "latest_block_number", nullable = false) BigInteger latestBlockNumber;

    private @Column(name = "end_block", nullable = false) BigInteger endBlock;

    private @Column(name = "sync", nullable = false) Boolean sync;

    private FilterStateEntity(
            UUID filterId, BigInteger latestBlockNumber, BigInteger endBlock, Boolean sync) {
        this.filterId = filterId;
        this.latestBlockNumber = latestBlockNumber;
        this.endBlock = endBlock;
        this.sync = sync;
    }

    public static FilterStateEntity fromFilterState(FilterState filterState) {
        return new FilterStateEntity(
                filterState.filterId(),
                filterState.latestBlockNumber(),
                filterState.endBlock(),
                filterState.sync());
    }

    public FilterState toFilterState() {
        return new FilterState(filterId, latestBlockNumber, endBlock, sync);
    }
}
