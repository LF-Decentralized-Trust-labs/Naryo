package io.naryo.application.filter.block;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.filter.Synchronizer;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.naryo.application.store.filter.FilterStore;
import io.naryo.application.store.filter.model.FilterState;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.sync.block.BlockActiveFilterSyncState;
import io.naryo.domain.node.Node;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static io.naryo.application.common.util.EncryptionUtil.sha3String;

@Slf4j
public final class EventFilterSynchronizer implements Synchronizer {

    private final Node node;
    @Getter private final EventFilter filter;
    private final BlockInteractor interactor;
    private final StartBlockCalculator calculator;
    private final ContractEventParameterDecoder decoder;
    private final ContractEventDispatcherHelper helper;
    private final List<Block> cachedBlocks = new ArrayList<>();
    private final List<TransactionReceipt> cachedTransactions = new ArrayList<>();
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;
    private final @Nullable FilterStore<?> filterStore;
    private final StoreConfiguration storeConfiguration;
    private final FilterState filterState;

    public EventFilterSynchronizer(
            Node node,
            EventFilter filter,
            BlockInteractor interactor,
            StartBlockCalculator calculator,
            ContractEventParameterDecoder decoder,
            ContractEventDispatcherHelper helper,
            CircuitBreaker circuitBreaker,
            Retry retry,
            @Nullable FilterStore<?> filterStore,
            StoreConfiguration storeConfiguration,
            @Nullable FilterState filterState) {
        Objects.requireNonNull(node, "node cannot be null");
        Objects.requireNonNull(filter, "filter cannot be null");
        Objects.requireNonNull(interactor, "interactor cannot be null");
        Objects.requireNonNull(calculator, "calculator cannot be null");
        Objects.requireNonNull(decoder, "decoder cannot be null");
        Objects.requireNonNull(helper, "helper cannot be null");
        Objects.requireNonNull(circuitBreaker, "circuitBreaker cannot be null");
        Objects.requireNonNull(retry, "retry cannot be null");
        Objects.requireNonNull(storeConfiguration, "storeConfiguration cannot be null");
        this.node = node;
        this.filter = filter;
        this.interactor = interactor;
        this.calculator = calculator;
        this.decoder = decoder;
        this.helper = helper;
        this.circuitBreaker = circuitBreaker;
        this.retry = retry;
        this.filterStore = filterStore;
        this.storeConfiguration = storeConfiguration;
        this.filterState = filterState;
    }

    @Override
    public Disposable synchronize() throws IOException {
        if (isFilterStoreEnabled() && filterState != null && filterState.sync()) {
            return Flowable.empty().subscribe();
        }

        final String topic = sha3String(filter.getSpecification().getEventSignature());
        final BigInteger startBlock = getStartBlock();
        final BigInteger endBlock = getEndBlock();

        if (isFilterStoreEnabled() && filterState == null) {
            saveFilterState(startBlock.subtract(BigInteger.ONE), endBlock);
        }

        return applyCircuitBreaker(
                () ->
                        Flowable.fromIterable(extractLogsFromFilter(topic, startBlock, endBlock))
                                .subscribe(
                                        value -> {
                                            Block block = getBlock(value.blockNumber());
                                            TransactionReceipt transaction =
                                                    getTransactionReceipt(value.transactionHash());

                                            ContractEvent contractEvent =
                                                    new ContractEvent(
                                                            filter.getNodeId(),
                                                            filter.getSpecification().eventName(),
                                                            decoder.decode(
                                                                    filter.getSpecification(),
                                                                    value),
                                                            value.transactionHash(),
                                                            value.index(),
                                                            value.blockNumber(),
                                                            value.blockHash(),
                                                            value.address(),
                                                            transaction != null
                                                                    ? transaction.from()
                                                                    : null,
                                                            ContractEventStatus.CONFIRMED,
                                                            block.timestamp());

                                            helper.execute(node, filter, contractEvent);
                                            saveFilterState(value.blockNumber(), endBlock);
                                        }));
    }

    private void saveFilterState(BigInteger currentBlock, BigInteger endBlock) {
        if (!isFilterStoreEnabled()) {
            return;
        }

        UUID id = filter.getId();
        boolean isSync = currentBlock.compareTo(endBlock) == 0;

        @SuppressWarnings("unchecked")
        FilterStore<ActiveStoreConfiguration> typedStore =
                (FilterStore<ActiveStoreConfiguration>) filterStore;
        typedStore.save(
                (ActiveStoreConfiguration) storeConfiguration,
                id,
                new FilterState(id, currentBlock, endBlock, isSync));
    }

    private boolean isFilterStoreEnabled() {
        return storeConfiguration.getState().equals(StoreState.ACTIVE) && filterStore != null;
    }

    private List<Log> extractLogsFromFilter(
            String topicHex, BigInteger startBlock, BigInteger endBlock) throws IOException {
        List<Log> logs;
        if (filter instanceof ContractEventFilter cef) {
            logs =
                    interactor.getLogs(
                            startBlock, endBlock, cef.getContractAddress(), List.of(topicHex));
        } else {
            logs = interactor.getLogs(startBlock, endBlock, List.of(topicHex));
        }
        return logs;
    }

    private BigInteger getEndBlock() throws IOException {
        if (isFilterStoreEnabled() && filterState != null) {
            BigInteger endBlock = filterState.endBlock();
            return endBlock.signum() == 1 && endBlock.compareTo(BigInteger.ZERO) > 0
                    ? endBlock
                    : calculator.getStartBlock();
        }
        return calculator.getStartBlock();
    }

    private BigInteger getStartBlock() {
        BigInteger startBlock =
                ((BlockActiveFilterSyncState) filter.getFilterSyncState())
                        .getInitialBlock()
                        .value();
        if (isFilterStoreEnabled() && filterState != null) {
            BigInteger latestBlock = filterState.latestBlockNumber().add(BigInteger.ONE);
            startBlock =
                    latestBlock.signum() == 1 && latestBlock.compareTo(startBlock) > 0
                            ? latestBlock
                            : startBlock;
        }
        return startBlock;
    }

    private Block getBlock(BigInteger blockNumber) throws IOException {
        for (Block block : cachedBlocks) {
            if (block.number().equals(blockNumber)) {
                return block;
            }
        }
        Block block = interactor.getBlock(blockNumber);
        cachedBlocks.add(block);
        return block;
    }

    private TransactionReceipt getTransactionReceipt(String transactionHash) throws IOException {
        for (TransactionReceipt transactionReceipt : cachedTransactions) {
            if (transactionReceipt.hash().equals(transactionHash)) {
                return transactionReceipt;
            }
        }
        TransactionReceipt transaction = interactor.getTransactionReceipt(transactionHash);
        cachedTransactions.add(transaction);
        return transaction;
    }

    public Disposable applyCircuitBreaker(Callable<Disposable> callable) {
        var decorated =
                Decorators.ofSupplier(
                                () -> {
                                    try {
                                        return callable.call();
                                    } catch (Exception e) {
                                        log.warn(
                                                "Block subscription failed, will be retried if policy allows. Reason: {}",
                                                e.getMessage());
                                        throw new RuntimeException(e);
                                    }
                                })
                        .withCircuitBreaker(circuitBreaker)
                        .withRetry(retry)
                        .decorate();

        try {
            return decorated.get();
        } catch (Exception e) {
            log.error("Subscription for block {} failed after retries", node.getId(), e);
            throw new RuntimeException("Could not subscribe to block stream", e);
        }
    }
}
