/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.naryo.chain.service.strategy;

import java.math.BigInteger;
import java.util.Objects;

import io.naryo.chain.service.HederaService;
import io.naryo.chain.service.block.BlockNumberService;
import io.naryo.chain.service.domain.Block;
import io.naryo.chain.service.domain.wrapper.Web3jBlock;
import io.naryo.chain.settings.NodeType;
import io.naryo.service.AsyncTaskService;
import io.naryo.utils.JSON;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

@Slf4j
public class PollingBlockSubscriptionStrategy extends AbstractBlockSubscriptionStrategy<EthBlock> {

    private final HederaService hederaService;
    private final Long pollingInterval;

    public PollingBlockSubscriptionStrategy(
            Web3j web3j,
            String nodeName,
            String nodeType,
            AsyncTaskService asyncService,
            BlockNumberService blockNumberService,
            Long pollingInterval) {
        super(web3j, nodeName, nodeType, asyncService, blockNumberService);
        this.hederaService = null;
        this.pollingInterval = pollingInterval;
    }

    public PollingBlockSubscriptionStrategy(
            Web3j web3j,
            String nodeName,
            String nodeType,
            AsyncTaskService asyncService,
            BlockNumberService blockNumberService,
            HederaService hederaService,
            Long pollingInterval) {
        super(web3j, nodeName, nodeType, asyncService, blockNumberService);
        this.hederaService = hederaService;
        this.pollingInterval = pollingInterval;
    }

    @Override
    public Disposable subscribe() {

        final BigInteger startBlock = getStartBlock();

        log.info("Starting block polling, from block {}", startBlock);

        final DefaultBlockParameter blockParam = DefaultBlockParameter.valueOf(startBlock);

        switch (NodeType.valueOf(nodeType)) {
            case NORMAL:
                blockSubscription =
                        web3j.replayPastAndFutureBlocksFlowable(blockParam, true)
                                .doOnError(error -> onError(blockSubscription, error))
                                .subscribe(
                                        this::triggerListeners,
                                        error -> onError(blockSubscription, error));
                break;
            case MIRROR:
                blockSubscription =
                        Objects.requireNonNull(hederaService)
                                .blocksFlowable(
                                        ((DefaultBlockParameterNumber) blockParam).getBlockNumber(),
                                        pollingInterval)
                                .doOnError(error -> onError(blockSubscription, error))
                                .subscribe(
                                        this::triggerListeners,
                                        error -> onError(blockSubscription, error));
                break;
            default:
                break;
        }

        return blockSubscription;
    }

    @Override
    Block convertToNaryoBlock(EthBlock blockObject) {
        // Infura is sometimes returning null blocks...just ignore in this case.
        if (blockObject == null || blockObject.getBlock() == null) {
            return null;
        }

        try {
            return new Web3jBlock(blockObject.getBlock(), nodeName);
        } catch (RuntimeException t) {
            log.error("Error converting block: {}", JSON.stringify(blockObject), t);
            throw t;
        }
    }
}
