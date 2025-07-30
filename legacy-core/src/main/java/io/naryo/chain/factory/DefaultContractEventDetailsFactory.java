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

package io.naryo.chain.factory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import io.naryo.chain.converter.EventParameterConverter;
import io.naryo.chain.service.domain.TransactionReceipt;
import io.naryo.chain.settings.Node;
import io.naryo.chain.util.Web3jUtil;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.event.filter.ContractEventSpecification;
import io.naryo.dto.event.filter.ParameterDefinition;
import io.naryo.dto.event.parameter.EventParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;

@Slf4j
public class DefaultContractEventDetailsFactory implements ContractEventDetailsFactory {

    private final EventParameterConverter<Type> parameterConverter;
    private final Node node;
    private final String networkName;

    public DefaultContractEventDetailsFactory(
            EventParameterConverter<Type> parameterConverter, Node node, String networkName) {
        this.parameterConverter = parameterConverter;
        this.node = node;
        this.networkName = networkName;
    }

    @Override
    public ContractEventDetails createEventDetails(
            ContractEventFilter eventFilter,
            Log logDetails,
            EthBlock ethBlock,
            TransactionReceipt transactionReceipt) {
        ContractEventDetails eventDetails = createContractEventDetails(eventFilter, logDetails);
        BigInteger timeStamp = null;

        while (timeStamp == null) {
            try {
                EthBlock.Block block = ethBlock.getBlock();
                timeStamp = block.getTimestamp();
            } catch (Exception ex) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.warn("Interrupted!", e);
                    Thread.currentThread().interrupt();
                }
            }
        }
        eventDetails.setTimestamp(timeStamp);
        eventDetails.setBlockTimestamp(timeStamp);
        eventDetails.setFrom(transactionReceipt.getFrom());

        return eventDetails;
    }

    @Override
    public ContractEventDetails createEventDetails(
            ContractEventFilter eventFilter,
            Log log,
            BigInteger blockTimestamp,
            String fromTransactionReceipt) {
        ContractEventDetails eventDetails = createContractEventDetails(eventFilter, log);
        eventDetails.setTimestamp(blockTimestamp);
        eventDetails.setBlockTimestamp(blockTimestamp);
        eventDetails.setFrom(fromTransactionReceipt);

        return eventDetails;
    }

    private ContractEventDetails createContractEventDetails(
            ContractEventFilter eventFilter, Log log) {
        final ContractEventSpecification eventSpec = eventFilter.getEventSpecification();

        final List<EventParameter> nonIndexed =
                typeListToParameterList(getNonIndexedParametersFromLog(eventSpec, log));
        final List<EventParameter> indexed =
                typeListToParameterList(getIndexedParametersFromLog(eventSpec, log));

        final ContractEventDetails eventDetails = new ContractEventDetails();
        eventDetails.setName(eventSpec.getEventName());
        eventDetails.setFilterId(eventFilter.getId());
        eventDetails.setNonIndexedParameters(nonIndexed);
        eventDetails.setIndexedParameters(indexed);
        eventDetails.setAddress(Keys.toChecksumAddress(log.getAddress()));
        eventDetails.setLogIndex(log.getLogIndex());
        eventDetails.setTransactionHash(log.getTransactionHash());
        eventDetails.setBlockNumber(log.getBlockNumber());
        eventDetails.setBlockHash(log.getBlockHash());
        eventDetails.setEventSpecificationSignature(Web3jUtil.getSignature(eventSpec));
        eventDetails.setNetworkName(this.networkName);
        eventDetails.setNodeName(eventFilter.getNode());
        eventDetails.setExtensionData(eventFilter.getExtension());

        if (log.isRemoved()) {
            eventDetails.setStatus(ContractEventStatus.INVALIDATED);
        } else if (node.getBlocksToWaitForConfirmation().equals(BigInteger.ZERO)) {
            // Set confirmed straight away if set to zero confirmations
            eventDetails.setStatus(ContractEventStatus.CONFIRMED);
        } else {
            eventDetails.setStatus(ContractEventStatus.UNCONFIRMED);
        }

        return eventDetails;
    }

    private List<EventParameter> typeListToParameterList(List<Type> typeList) {
        if (isNullOrEmpty(typeList)) {
            return Collections.emptyList();
        }

        return typeList.stream().map(parameterConverter::convert).toList();
    }

    private List<Type> getNonIndexedParametersFromLog(
            ContractEventSpecification eventSpec, Log log) {
        if (StringUtils.hasLength(eventSpec.getWeb3EventSmartContractClass())) {
            return FunctionReturnDecoder.decode(
                    log.getData(),
                    Web3jUtil.getEventFromWeb3SmartContractClassName(
                                    eventSpec.getWeb3EventSmartContractClass(),
                                    eventSpec.getEventName())
                            .getNonIndexedParameters());
        }
        if (isNullOrEmpty(eventSpec.getNonIndexedParameterDefinitions())) {
            return Collections.emptyList();
        }

        List<ParameterDefinition> orderedParams =
                new ArrayList<>(eventSpec.getNonIndexedParameterDefinitions());
        orderedParams.sort((p1, p2) -> p1.getPosition().compareTo(p2.getPosition()));

        return FunctionReturnDecoder.decode(
                log.getData(),
                Utils.convert(Web3jUtil.getTypeReferencesFromParameterDefinitions(orderedParams)));
    }

    private List<Type> getIndexedParametersFromLog(ContractEventSpecification eventSpec, Log log) {
        if (StringUtils.hasLength(eventSpec.getWeb3EventSmartContractClass())) {
            return FunctionReturnDecoder.decode(
                    log.getData(),
                    Web3jUtil.getEventFromWeb3SmartContractClassName(
                                    eventSpec.getWeb3EventSmartContractClass(),
                                    eventSpec.getEventName())
                            .getIndexedParameters());
        }
        if (isNullOrEmpty(eventSpec.getIndexedParameterDefinitions())) {
            return Collections.emptyList();
        }

        final List<String> encodedParameters = log.getTopics().subList(1, log.getTopics().size());
        final List<ParameterDefinition> definitions = eventSpec.getIndexedParameterDefinitions();

        return IntStream.range(0, encodedParameters.size())
                .mapToObj(
                        i ->
                                FunctionReturnDecoder.decodeIndexedValue(
                                        encodedParameters.get(i),
                                        Web3jUtil.getTypeReferenceFromParameterType(
                                                definitions.get(i).getType())))
                .toList();
    }

    private boolean isNullOrEmpty(List<?> toCheck) {
        return toCheck == null || toCheck.isEmpty();
    }
}
