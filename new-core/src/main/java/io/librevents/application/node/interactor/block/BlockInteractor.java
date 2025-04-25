package io.librevents.application.node.interactor.block;

import java.math.BigInteger;
import java.util.List;

import io.librevents.application.node.interactor.Interactor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.dto.Log;
import io.reactivex.Flowable;

public interface BlockInteractor extends Interactor {

    Flowable<Block> replayPastBlocks(BigInteger startBlock);

    Flowable<Block> replayPastAndFutureBlocks(BigInteger startBlock);

    Flowable<Block> replyFutureBlocks();

    Block getCurrentBlock();

    BigInteger getCurrentBlockNumber();

    Block getBlock(BigInteger number);

    Block getBlock(String hash);

    List<Log> getLogs(BigInteger startBlock, BigInteger endBlock);

    List<Log> getLogs(BigInteger startBlock, BigInteger endBlock, String contractAddress);

    List<Log> getLogs(String blockHash);

    List<Log> getLogs(String blockHash, String contractAddress);
}
