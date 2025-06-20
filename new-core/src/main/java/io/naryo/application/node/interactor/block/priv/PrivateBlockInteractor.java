package io.naryo.application.node.interactor.block.priv;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;

public interface PrivateBlockInteractor extends BlockInteractor {

    List<Log> getPrivateLogs(String privacyGroupId, BigInteger startBlock, BigInteger endBlock)
            throws IOException;

    List<Log> getPrivateLogs(
            String privacyGroupId, BigInteger startBlock, BigInteger endBlock, List<String> topics)
            throws IOException;

    List<Log> getPrivateLogs(
            String privacyGroupId,
            BigInteger startBlock,
            BigInteger endBlock,
            String contractAddress)
            throws IOException;

    List<Log> getPrivateLogs(
            String privacyGroupId,
            BigInteger startBlock,
            BigInteger endBlock,
            String contractAddress,
            List<String> topics)
            throws IOException;

    List<Log> getPrivateLogs(String privacyGroupId, String blockHash) throws IOException;

    List<Log> getPrivateLogs(String privacyGroupId, String blockHash, String contractAddress)
            throws IOException;

    TransactionReceipt getPrivateTransactionReceipt(String transactionHash) throws IOException;
}
