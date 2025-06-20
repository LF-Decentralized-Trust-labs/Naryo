package io.naryo.application.node.interactor.block;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import io.naryo.application.node.interactor.Interactor;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.reactivex.Flowable;

public interface BlockInteractor extends Interactor {

    Flowable<Block> replayPastBlocks(BigInteger startBlock) throws IOException;

    Flowable<Block> replayPastAndFutureBlocks(BigInteger startBlock);

    Flowable<Block> replyFutureBlocks() throws IOException;

    Block getCurrentBlock() throws IOException;

    BigInteger getCurrentBlockNumber() throws IOException;

    Block getBlock(BigInteger number) throws IOException;

    Block getBlock(String hash) throws IOException;

    List<Log> getLogs(BigInteger startBlock, BigInteger endBlock) throws IOException;

    List<Log> getLogs(BigInteger startBlock, BigInteger endBlock, List<String> topics)
            throws IOException;

    List<Log> getLogs(BigInteger startBlock, BigInteger endBlock, String contractAddress)
            throws IOException;

    List<Log> getLogs(
            BigInteger startBlock, BigInteger endBlock, String contractAddress, List<String> topics)
            throws IOException;

    List<Log> getLogs(String blockHash) throws IOException;

    List<Log> getLogs(String blockHash, String contractAddress) throws IOException;

    TransactionReceipt getTransactionReceipt(String transactionHash) throws IOException;

    String getRevertReason(String transactionHash) throws IOException;
}
