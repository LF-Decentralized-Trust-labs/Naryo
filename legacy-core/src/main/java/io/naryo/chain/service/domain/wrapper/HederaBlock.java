package io.naryo.chain.service.domain.wrapper;

import java.math.BigInteger;
import java.util.List;

import io.naryo.chain.service.domain.Block;
import io.naryo.chain.service.domain.Transaction;
import io.naryo.chain.service.domain.io.ContractResultResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HederaBlock implements Block {

    private static final String NONCE_ZERO = "0x0000000000000000";

    private Integer count;
    private BigInteger gasUsed;
    private String hapiVersion;
    private String hash;
    private String logsBloom;
    private String name;
    private BigInteger number;
    private String previousHash;
    private BigInteger size;
    private String fromTimestamp;
    private String toTimestamp;
    private String parentHash;
    private BigInteger nonce;
    private String sha3Uncles;
    private String transactionsRoot;
    private String stateRoot;
    private String receiptsRoot;
    private String author;
    private String miner;
    private String mixHash;
    private BigInteger difficulty;
    private BigInteger totalDifficulty;
    private String extraData;
    private BigInteger gasLimit;
    private List<Transaction> transactions;
    private List<String> uncles;
    private List<String> sealFields;
    private String nodeName;
    private BigInteger timestamp;
    private List<ContractResultResponse> contractResults;
}
