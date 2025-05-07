package io.librevents.application.node.interactor.block;

import io.librevents.application.node.interactor.Interactor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.dto.Log;
import io.librevents.application.node.interactor.block.dto.Transaction;
import io.reactivex.Flowable;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface BlockInteractor extends Interactor {

    Flowable<Block> replayPastBlocks(BigInteger startBlock);

    Flowable<Block> replayPastAndFutureBlocks(BigInteger startBlock);

    Flowable<Block> replyFutureBlocks();

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

    Transaction getTransactionReceipt(String transactionHash) throws IOException;
}
