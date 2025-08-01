package io.naryo.infrastructure.node.interactor.eth.rpc;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.reactivex.Flowable;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

public class EthereumRpcBlockInteractor implements BlockInteractor {

    protected final Web3j web3j;

    public EthereumRpcBlockInteractor(Web3j web3j) {
        Objects.requireNonNull(web3j, "web3j cannot be null");
        this.web3j = web3j;
    }

    @Override
    public Flowable<Block> replayPastBlocks(BigInteger startBlock) {
        return web3j.replayPastBlocksFlowable(DefaultBlockParameter.valueOf(startBlock), true)
                .map(this::mapToBlock);
    }

    @Override
    public Flowable<Block> replayPastAndFutureBlocks(BigInteger startBlock) {
        return web3j.replayPastAndFutureBlocksFlowable(
                        DefaultBlockParameter.valueOf(startBlock), true)
                .map(this::mapToBlock);
    }

    @Override
    public Flowable<Block> replyFutureBlocks() {
        return web3j.newHeadsNotifications()
                .map(
                        notification -> {
                            var newHead = notification.getParams().getResult();
                            return this.getBlock(newHead.getHash());
                        });
    }

    @Override
    public Block getCurrentBlock() throws IOException {
        return getBlock(getCurrentBlockNumber());
    }

    @Override
    public BigInteger getCurrentBlockNumber() throws IOException {
        return web3j.ethBlockNumber().send().getBlockNumber();
    }

    @Override
    public Block getBlock(BigInteger number) throws IOException {
        return mapToBlock(
                web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(number), true).send());
    }

    @Override
    public Block getBlock(String hash) throws IOException {
        return mapToBlock(web3j.ethGetBlockByHash(hash, true).send());
    }

    @Override
    public List<Log> getLogs(BigInteger startBlock, BigInteger endBlock) throws IOException {
        return getLogs(startBlock, endBlock, List.of(), List.of());
    }

    @Override
    public List<Log> getLogs(BigInteger startBlock, BigInteger endBlock, List<String> topics)
            throws IOException {
        return getLogs(startBlock, endBlock, List.of(), topics);
    }

    @Override
    public List<Log> getLogs(BigInteger startBlock, BigInteger endBlock, String contractAddress)
            throws IOException {
        return getLogs(startBlock, endBlock, List.of(contractAddress), List.of());
    }

    @Override
    public List<Log> getLogs(
            BigInteger startBlock, BigInteger endBlock, String contractAddress, List<String> topics)
            throws IOException {
        return getLogs(startBlock, endBlock, List.of(contractAddress), topics);
    }

    @Override
    public List<Log> getLogs(String blockHash) throws IOException {
        return web3j.ethGetLogs(new EthFilter(blockHash)).send().getLogs().stream()
                .map(this::mapToLog)
                .toList();
    }

    @Override
    public List<Log> getLogs(String blockHash, String contractAddress) throws IOException {
        return web3j.ethGetLogs(new EthFilter(blockHash, contractAddress)).send().getLogs().stream()
                .map(this::mapToLog)
                .toList();
    }

    @Override
    public TransactionReceipt getTransactionReceipt(String transactionHash) throws IOException {
        return mapToTransaction(web3j.ethGetTransactionReceipt(transactionHash).send().getResult());
    }

    @Override
    public String getRevertReason(String transactionHash) throws IOException {
        return null;
    }

    protected List<Log> getLogs(
            BigInteger startBlock, BigInteger endBlock, List<String> addresses, List<String> topics)
            throws IOException {
        var filter =
                new EthFilter(
                        DefaultBlockParameter.valueOf(startBlock),
                        DefaultBlockParameter.valueOf(endBlock),
                        addresses);
        if (topics != null && !topics.isEmpty()) {
            filter.addOptionalTopics(topics.toArray(new String[0]));
        }
        return web3j.ethGetLogs(filter).send().getLogs().stream().map(this::mapToLog).toList();
    }

    protected Block mapToBlock(EthBlock block) {
        return new Block(
                block.getBlock().getNumber(),
                block.getBlock().getHash(),
                block.getBlock().getLogsBloom(),
                block.getBlock().getSize(),
                block.getBlock().getGasUsed(),
                block.getBlock().getTimestamp(),
                block.getBlock().getTransactions().stream()
                        .map(
                                transactionResult -> {
                                    var tx = (EthBlock.TransactionObject) transactionResult;
                                    return new Transaction(
                                            tx.getHash(),
                                            tx.getTransactionIndex(),
                                            tx.getNonce(),
                                            tx.getBlockNumber(),
                                            tx.getBlockHash(),
                                            null,
                                            tx.getFrom(),
                                            tx.getTo(),
                                            tx.getValueRaw(),
                                            tx.getInput(),
                                            null,
                                            null);
                                })
                        .toList());
    }

    protected Log mapToLog(EthLog.LogResult<EthLog.LogObject> result) {
        EthLog.LogObject log = result.get();
        return new Log(
                log.getLogIndex(),
                log.getTransactionIndex(),
                log.getTransactionHash(),
                log.getBlockHash(),
                log.getBlockNumber(),
                log.getAddress(),
                log.getData(),
                log.getType(),
                log.getTopics().stream().map(Object::toString).toList());
    }

    protected Log mapToLog(org.web3j.protocol.core.methods.response.Log result) {
        return new Log(
                result.getLogIndex(),
                result.getTransactionIndex(),
                result.getTransactionHash(),
                result.getBlockHash(),
                result.getBlockNumber(),
                result.getAddress(),
                result.getData(),
                result.getType(),
                result.getTopics().stream().map(Object::toString).toList());
    }

    protected TransactionReceipt mapToTransaction(
            org.web3j.protocol.core.methods.response.TransactionReceipt result) {
        return new TransactionReceipt(
                result.getTransactionHash(),
                result.getTransactionIndex(),
                result.getBlockHash(),
                result.getBlockNumber(),
                result.getCumulativeGasUsed(),
                result.getGasUsed(),
                result.getContractAddress(),
                result.getRoot(),
                result.getFrom(),
                result.getTo(),
                result.getLogs().stream().map(this::mapToLog).toList(),
                result.getLogsBloom(),
                result.getStatus(),
                result.getRevertReason());
    }
}
