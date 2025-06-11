package io.naryo.infrastructure.node.interactor.eth.rpc;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.naryo.application.node.interactor.block.priv.PrivateBlockInteractor;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;

public final class PrivateEthereumRpcBlockInteractor extends EthereumRpcBlockInteractor
        implements PrivateBlockInteractor {

    private final Besu besu;

    public PrivateEthereumRpcBlockInteractor(Web3j web3j, Besu besu) {
        super(web3j);
        Objects.requireNonNull(besu, "besu cannot be null");
        this.besu = besu;
    }

    @Override
    public List<Log> getPrivateLogs(
            String privacyGroupId, BigInteger startBlock, BigInteger endBlock) throws IOException {
        return getPrivateLogs(privacyGroupId, startBlock, endBlock, List.of());
    }

    @Override
    public List<Log> getPrivateLogs(
            String privacyGroupId, BigInteger startBlock, BigInteger endBlock, List<String> topics)
            throws IOException {
        return getPrivateLogs(privacyGroupId, startBlock, endBlock, List.of(), topics);
    }

    @Override
    public List<Log> getPrivateLogs(
            String privacyGroupId,
            BigInteger startBlock,
            BigInteger endBlock,
            String contractAddress)
            throws IOException {
        return getPrivateLogs(
                privacyGroupId, startBlock, endBlock, List.of(contractAddress), List.of());
    }

    @Override
    public List<Log> getPrivateLogs(
            String privacyGroupId,
            BigInteger startBlock,
            BigInteger endBlock,
            String contractAddress,
            List<String> topics)
            throws IOException {
        return getPrivateLogs(
                privacyGroupId, startBlock, endBlock, List.of(contractAddress), topics);
    }

    @Override
    public List<Log> getPrivateLogs(String privacyGroupId, String blockHash) throws IOException {
        return besu.privGetLogs(privacyGroupId, new EthFilter(blockHash)).send().getLogs().stream()
                .map(this::mapToLog)
                .toList();
    }

    @Override
    public List<Log> getPrivateLogs(String privacyGroupId, String blockHash, String contractAddress)
            throws IOException {
        return besu
                .privGetLogs(privacyGroupId, new EthFilter(blockHash, contractAddress))
                .send()
                .getLogs()
                .stream()
                .map(this::mapToLog)
                .toList();
    }

    @Override
    public TransactionReceipt getPrivateTransactionReceipt(String transactionHash)
            throws IOException {
        return besu.privGetTransactionReceipt(transactionHash)
                .send()
                .getTransactionReceipt()
                .map(this::mapToTransaction)
                .orElse(null);
    }

    private List<Log> getPrivateLogs(
            String privacyGroupId,
            BigInteger startBlock,
            BigInteger endBlock,
            List<String> addresses,
            List<String> topics)
            throws IOException {
        var filter =
                new EthFilter(
                        DefaultBlockParameter.valueOf(startBlock),
                        DefaultBlockParameter.valueOf(endBlock),
                        addresses);
        if (topics != null && !topics.isEmpty()) {
            filter.addOptionalTopics(topics.toArray(new String[0]));
        }
        return besu.privGetLogs(privacyGroupId, filter).send().getLogs().stream()
                .map(this::mapToLog)
                .toList();
    }
}
