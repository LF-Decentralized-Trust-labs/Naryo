package io.naryo.infrastructure.node.interactor.hedera.map;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.naryo.application.node.interactor.block.dto.hedera.HederaTransaction;
import io.naryo.application.node.interactor.block.dto.hedera.TransactionName;
import io.naryo.infrastructure.node.interactor.hedera.response.TransactionListResponseModel;
import io.naryo.infrastructure.node.interactor.hedera.response.TransactionResponseModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionConverterTest {

    private static final BigInteger BLOCK_NUMBER = BigInteger.valueOf(40902448);

    /**
     * Two entries, verbatim, of the testnet mirror node listing for block 40902448
     * (/api/v1/transactions?timestamp=gte:1790207998.647706030&timestamp=lte:1790208000.451662768,
     * fetched on 2026-09-24). The first is the end-of-staking-period record the network emits once
     * a day (nonce 1, no transfers); the second is the user transaction it was attached to.
     */
    private static final String MIRROR_NODE_RESPONSE =
            """
            {
              "transactions": [
                {
                  "batch_key": null,
                  "bytes": null,
                  "charged_tx_fee": 0,
                  "consensus_timestamp": "1790208000.027803565",
                  "entity_id": null,
                  "high_volume": false,
                  "high_volume_pricing_multiplier": 0,
                  "max_fee": "0",
                  "max_custom_fees": [],
                  "memo_base64": "RW5kIG9mIHN0YWtpbmcgcGVyaW9kIGNhbGN1bGF0aW9uIHJlY29yZA==",
                  "name": "NODESTAKEUPDATE",
                  "nft_transfers": [],
                  "node": null,
                  "nonce": 1,
                  "parent_consensus_timestamp": null,
                  "result": "SUCCESS",
                  "scheduled": false,
                  "staking_reward_transfers": [],
                  "token_transfers": [],
                  "transaction_hash": "sVo0E8IOphQ9ADjfu9fP9ym4Ffyoc/1F8lHHA4wFcHA+3YjyNcugoxLLqoq9GfBJ",
                  "transaction_id": "0.0.5146699-1790207990-410574817",
                  "transfers": [],
                  "valid_duration_seconds": null,
                  "valid_start_timestamp": "1790207990.410574817"
                },
                {
                  "batch_key": null,
                  "bytes": null,
                  "charged_tx_fee": 339005,
                  "consensus_timestamp": "1790208000.027803566",
                  "entity_id": "0.0.5146701",
                  "high_volume": false,
                  "high_volume_pricing_multiplier": 0,
                  "max_fee": "200000000",
                  "max_custom_fees": [],
                  "memo_base64": "",
                  "name": "CONSENSUSSUBMITMESSAGE",
                  "nft_transfers": [],
                  "node": "0.0.7",
                  "nonce": 0,
                  "parent_consensus_timestamp": null,
                  "result": "SUCCESS",
                  "scheduled": false,
                  "staking_reward_transfers": [],
                  "token_transfers": [],
                  "transaction_hash": "LMREzK1slPPggwJWG58vUifjKYiUsWK8Lg6RvWaaRHMbaPzbdifPc5meoxRT2irF",
                  "transaction_id": "0.0.5146699-1790207990-410574817",
                  "transfers": [
                    {
                      "account": "0.0.802",
                      "amount": 339005,
                      "is_approval": false
                    },
                    {
                      "account": "0.0.5146699",
                      "amount": -339005,
                      "is_approval": false
                    }
                  ],
                  "valid_duration_seconds": "120",
                  "valid_start_timestamp": "1790207990.410574817"
                }
              ],
              "links": {
                "next": null
              }
            }
            """;

    @Test
    void mapsTransactionWithoutTransfersToNullSender() throws Exception {
        List<TransactionResponseModel> models = readMirrorNodeResponse();

        HederaTransaction nodeStakeUpdate = TransactionConverter.map(models.get(0), BLOCK_NUMBER);

        assertEquals(TransactionName.NODESTAKEUPDATE, nodeStakeUpdate.getName());
        assertTrue(nodeStakeUpdate.getTransfers().isEmpty());
        assertEquals("0.0.5146699-1790207990-410574817", nodeStakeUpdate.getTransactionId());
        assertNull(nodeStakeUpdate.getFrom());
        assertNull(nodeStakeUpdate.getTo());
        assertNull(nodeStakeUpdate.getValue());
    }

    @Test
    void mapsMostNegativeTransferToSender() throws Exception {
        List<TransactionResponseModel> models = readMirrorNodeResponse();

        HederaTransaction submitMessage = TransactionConverter.map(models.get(1), BLOCK_NUMBER);

        assertEquals(TransactionName.CONSENSUSSUBMITMESSAGE, submitMessage.getName());
        assertEquals("0.0.5146699", submitMessage.getFrom());
        assertEquals("0.0.802", submitMessage.getTo());
        assertEquals("339005", submitMessage.getValue());
    }

    private static List<TransactionResponseModel> readMirrorNodeResponse() throws Exception {
        ObjectMapper mapper =
                new ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TransactionListResponseModel page =
                mapper.readerFor(new TypeReference<TransactionListResponseModel>() {})
                        .readValue(MIRROR_NODE_RESPONSE);
        return page.getResults();
    }
}
