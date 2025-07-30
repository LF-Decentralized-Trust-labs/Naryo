package io.naryo.privacy.chain.util;

import java.util.Map;

import io.naryo.chain.service.domain.Transaction;
import io.naryo.chain.service.domain.wrapper.Web3jTransaction;
import io.naryo.privacy.chain.model.PrivacyConfFilter;
import io.naryo.privacy.chain.model.PrivacyConfNode;
import io.naryo.utils.ModelMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.web3j.protocol.besu.response.privacy.PrivateTransaction;

@Slf4j
public class PrivacyUtils {

    public static final String PRIVACY_EXTENSION_KEY = "privacy";
    public static final String PRIVACY_ENABLED_KEY = "enabled";
    public static final String PRIVACY_GROUP_ID_KEY = "privacyGroupID";
    public static final String PRIVACY_PRECOMPILED_ADDRESS = "privacyPrecompiledAddress";

    PrivacyUtils() {}

    public static PrivacyConfNode buildPrivacyConfNodeFromExtension(Map<String, Object> extension) {
        if (extension == null) {
            return null;
        }

        Map<String, Object> subMap = (Map<String, Object>) extension.get(PRIVACY_EXTENSION_KEY);

        return subMap == null
                ? null
                : new PrivacyConfNode(
                        Boolean.parseBoolean(subMap.get(PRIVACY_ENABLED_KEY).toString()),
                        (String) subMap.get(PRIVACY_PRECOMPILED_ADDRESS));
    }

    public static PrivacyConfFilter buildPrivacyConfFilterFromExtension(
            Map<String, Object> extension) {
        if (extension == null) {
            return null;
        }

        Map<String, Object> subMap = (Map<String, Object>) extension.get(PRIVACY_EXTENSION_KEY);

        return subMap == null
                ? null
                : new PrivacyConfFilter(
                        Boolean.parseBoolean(subMap.get(PRIVACY_ENABLED_KEY).toString()),
                        (String) subMap.get(PRIVACY_GROUP_ID_KEY));
    }

    public static Transaction buildTransactionFromPrivateTransaction(
            PrivateTransaction privateTransctionReceipt) {
        org.web3j.protocol.core.methods.response.Transaction tx =
                new org.web3j.protocol.core.methods.response.Transaction();
        final ModelMapper modelMapper = ModelMapperFactory.getModelMapper();

        modelMapper.map(privateTransctionReceipt, tx);

        return new Web3jTransaction(tx);
    }
}
