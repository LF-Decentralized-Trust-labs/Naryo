package io.naryo.application.filter.block;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.sync.block.BlockActiveSyncState;
import io.naryo.domain.node.Node;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
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

    public EventFilterSynchronizer(
            Node node,
            EventFilter filter,
            BlockInteractor interactor,
            StartBlockCalculator calculator,
            ContractEventParameterDecoder decoder,
            ContractEventDispatcherHelper helper,
            CircuitBreaker circuitBreaker,
            Retry retry) {
        Objects.requireNonNull(node, "node cannot be null");
        Objects.requireNonNull(filter, "filter cannot be null");
        Objects.requireNonNull(interactor, "interactor cannot be null");
        Objects.requireNonNull(calculator, "calculator cannot be null");
        Objects.requireNonNull(decoder, "decoder cannot be null");
        Objects.requireNonNull(helper, "helper cannot be null");
        Objects.requireNonNull(circuitBreaker, "circuitBreaker cannot be null");
        Objects.requireNonNull(retry, "retry cannot be null");
        this.node = node;
        this.filter = filter;
        this.interactor = interactor;
        this.calculator = calculator;
        this.decoder = decoder;
        this.helper = helper;
        this.circuitBreaker = circuitBreaker;
        this.retry = retry;
    }

    @Override
    public Disposable synchronize() throws IOException {
        String topic = sha3String(filter.getSpecification().getEventSignature());

        return Flowable.fromIterable(extractLogsFromFilter(topic))
                .subscribe(
                        value -> {
                            Block block = getBlock(value.blockNumber());
                            TransactionReceipt transaction =
                                    getTransactionReceipt(value.transactionHash());

                            ContractEvent contractEvent =
                                    new ContractEvent(
                                            filter.getNodeId(),
                                            filter.getSpecification().eventName(),
                                            decoder.decode(filter.getSpecification(), value.data()),
                                            value.transactionHash(),
                                            value.index(),
                                            value.blockNumber(),
                                            value.blockHash(),
                                            value.address(),
                                            transaction != null ? transaction.from() : null,
                                            ContractEventStatus.CONFIRMED,
                                            block.timestamp());

                            helper.execute(node, filter, contractEvent);
                        });
    }

    private List<Log> extractLogsFromFilter(String topicHex) throws IOException {
        List<Log> logs;
        BigInteger startBlock =
                ((BlockActiveSyncState) filter.getSyncState()).getInitialBlock().value();
        BigInteger endBlock = calculator.getStartBlock();
        if (filter instanceof ContractEventFilter cef) {
            logs =
                    interactor.getLogs(
                            startBlock, endBlock, cef.getContractAddress(), List.of(topicHex));
        } else {
            logs = interactor.getLogs(startBlock, endBlock, List.of(topicHex));
        }
        return logs;
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
