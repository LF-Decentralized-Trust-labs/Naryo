package io.naryo.chain.service.domain.io;

import java.math.BigInteger;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StakingRewardTransferResponse {

    String account;

    BigInteger amount;
}
