package io.librevents.application.filter.block;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.librevents.application.common.util.EncryptionUtil;
import io.librevents.application.event.decoder.ContractEventParameterDecoder;
import io.librevents.application.filter.Synchronizer;
import io.librevents.application.node.calculator.StartBlockCalculator;
import io.librevents.application.node.helper.ContractEventDispatcherHelper;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.dto.Log;
import io.librevents.application.node.interactor.block.dto.Transaction;
import io.librevents.domain.common.ContractEventStatus;
import io.librevents.domain.event.contract.ContractEvent;
import io.librevents.domain.filter.event.ContractEventFilter;
import io.librevents.domain.filter.event.EventFilter;
import io.librevents.domain.filter.event.sync.block.BlockActiveSyncState;
import io.librevents.domain.node.Node;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EventFilterSynchronizer implements Synchronizer {

    private final Node node;
    @Getter private final EventFilter filter;
    private final BlockInteractor interactor;
    private final StartBlockCalculator calculator;
    private final ContractEventParameterDecoder decoder;
    private final ContractEventDispatcherHelper helper;
    private final List<Block> cachedBlocks = new ArrayList<>();
    private final List<Transaction> cachedTransactions = new ArrayList<>();

    public EventFilterSynchronizer(
            Node node,
            EventFilter filter,
            BlockInteractor interactor,
            StartBlockCalculator calculator,
            ContractEventParameterDecoder decoder,
            ContractEventDispatcherHelper helper) {
        Objects.requireNonNull(node, "node cannot be null");
        Objects.requireNonNull(filter, "filter cannot be null");
        Objects.requireNonNull(interactor, "interactor cannot be null");
        Objects.requireNonNull(calculator, "calculator cannot be null");
        Objects.requireNonNull(decoder, "decoder cannot be null");
        Objects.requireNonNull(helper, "helper cannot be null");
        this.node = node;
        this.filter = filter;
        this.interactor = interactor;
        this.calculator = calculator;
        this.decoder = decoder;
        this.helper = helper;
    }

    @Override
    public Disposable synchronize() throws IOException {
        String topic = EncryptionUtil.keccak256Hex(filter.getSpecification().getEventSignature());

        return Flowable.fromIterable(extractLogsFromFilter(topic))
                .subscribe(
                        value -> {
                            Block block = getBlock(value.blockNumber());
                            Transaction transaction = getTransaction(value.transactionHash());

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

    private Transaction getTransaction(String transactionHash) throws IOException {
        for (Transaction transaction : cachedTransactions) {
            if (transaction.hash().equals(transactionHash)) {
                return transaction;
            }
        }
        Transaction transaction = interactor.getTransactionReceipt(transactionHash);
        cachedTransactions.add(transaction);
        return transaction;
    }
}
