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

package io.naryo.chain.service.domain.wrapper;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import io.naryo.chain.service.domain.Log;
import io.naryo.chain.service.domain.TransactionReceipt;
import io.naryo.utils.ModelMapperFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

/**
 * A TransactionReceipt that is constructed from a Web3j transaction receipt.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Data
@Slf4j
public class Web3jTransactionReceipt implements TransactionReceipt {

    private String transactionHash;
    private BigInteger transactionIndex;
    private String blockHash;
    private BigInteger blockNumber;
    private BigInteger cumulativeGasUsed;
    private BigInteger gasUsed;
    private String contractAddress;
    private String root;
    private String from;
    private String to;
    private List<Log> logs;
    private String logsBloom;
    private String status;
    private String revertReason;

    public Web3jTransactionReceipt(
            org.web3j.protocol.core.methods.response.TransactionReceipt web3TransactionReceipt) {

        logs = convertLogs(web3TransactionReceipt.getLogs());

        try {
            final ModelMapper modelMapper = ModelMapperFactory.getModelMapper();
            // Skip logs
            modelMapper
                    .getConfiguration()
                    .setPropertyCondition(
                            ctx ->
                                    !ctx.getMapping()
                                            .getLastDestinationProperty()
                                            .getName()
                                            .equals("logs"));
            modelMapper.map(web3TransactionReceipt, this);
        } catch (RuntimeException re) {
            log.error(re.getMessage(), re);
            throw re;
        }
    }

    private List<Log> convertLogs(List<org.web3j.protocol.core.methods.response.Log> logs) {
        return logs.stream().map(Web3jLog::new).collect(Collectors.toList());
    }
}
