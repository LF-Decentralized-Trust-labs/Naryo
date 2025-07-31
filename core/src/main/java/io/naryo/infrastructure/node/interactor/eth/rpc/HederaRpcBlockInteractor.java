package io.naryo.infrastructure.node.interactor.eth.rpc;

import java.math.BigInteger;

import io.naryo.application.node.interactor.block.dto.Block;
import io.reactivex.Flowable;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;

public final class HederaRpcBlockInteractor extends EthereumRpcBlockInteractor {

    public HederaRpcBlockInteractor(Web3j web3j) {
        super(web3j);
    }

    @Override
    public Flowable<Block> replayPastAndFutureBlocks(BigInteger startBlock) {
        return web3j.replayPastBlocksFlowable(
                        DefaultBlockParameter.valueOf(startBlock),
                        true,
                        web3j.ethBlockHashFlowable()
                                .flatMap(
                                        blockHash ->
                                                web3j.ethGetBlockByHash(
                                                                parseToEthBlockHash(blockHash),
                                                                true)
                                                        .flowable()))
                .map(this::mapToBlock);
    }

    /**
     * This is a hack to get around the fact that the RPC returns the block hash in the mirror node
     * format. NOTE: This can be removed once the PR
     * (https://github.com/hiero-ledger/hiero-json-rpc-relay/pull/3863) is merged
     *
     * @param blockHash Mirror node block hash
     * @return Ethereum block hash
     */
    private String parseToEthBlockHash(String blockHash) {
        return blockHash.substring(0, 66);
    }
}
