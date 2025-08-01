package io.naryo.application.event.decoder.block;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.domain.event.contract.ContractEventParameter;
import io.naryo.domain.event.contract.parameter.*;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.EventFilterSpecificationBuilder;
import io.naryo.domain.filter.event.parameterdefinition.*;
import org.junit.jupiter.api.Test;

class DefaultContractEventParameterDecoderTest {

    private static final ContractEventParameterDecoder DECODER =
            new DefaultContractEventParameterDecoder();

    @Test
    void testDecode() {
        EventFilterSpecification spec =
                new EventFilterSpecificationBuilder()
                        .withParameters(
                                Set.of(
                                        new AddressParameterDefinitionBuilder()
                                                .withPosition(0)
                                                .build(),
                                        new ArrayParameterDefinitionBuilder()
                                                .withPosition(1)
                                                .withElementType(
                                                        new AddressParameterDefinitionBuilder()
                                                                .build())
                                                .withFixedLength(null)
                                                .build(),
                                        new BoolParameterDefinitionBuilder()
                                                .withPosition(2)
                                                .build(),
                                        new BytesFixedParameterDefinitionBuilder()
                                                .withByteLength(32)
                                                .withPosition(3)
                                                .build(),
                                        new BytesParameterDefinitionBuilder()
                                                .withPosition(4)
                                                .build(),
                                        new IntParameterDefinitionBuilder()
                                                .withBitSize(256)
                                                .withPosition(5)
                                                .build(),
                                        new StringParameterDefinitionBuilder()
                                                .withPosition(6)
                                                .build(),
                                        new UintParameterDefinitionBuilder()
                                                .withBitSize(256)
                                                .withPosition(7)
                                                .build()))
                        .build();

        // @formatter:off
        String logData =
                "0x"
                        // ──────────────────────────────────────── head (8 slots)
                        // ────────────────────────────────────────
                        + "0000000000000000000000001111111111111111111111111111111111111111" // param0: address
                        + "0000000000000000000000000000000000000000000000000000000000000100" // param1: offset → 0x100
                        + "0000000000000000000000000000000000000000000000000000000000000000" // param2: bool(false)
                        + "deadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeef" // param3: bytes32
                        + "0000000000000000000000000000000000000000000000000000000000000160" // param4: offset → 0x160
                        + "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" // param5: int256(-1)
                        + "00000000000000000000000000000000000000000000000000000000000001a0" // param6: offset → 0x1a0
                        + "00000000000000000000000000000000000000000000000000000000000004d2" // param7: uint256(1234)

                        // ──────────────────────────────── tail @ 0x100 (array)
                        // ────────────────────────────────
                        + "0000000000000000000000000000000000000000000000000000000000000002" // length = 2
                        + "0000000000000000000000002222222222222222222222222222222222222222" // element[0]
                        + "0000000000000000000000003333333333333333333333333333333333333333" // element[1]

                        // ──────────────────────────────── tail @ 0x160 (bytes)
                        // ────────────────────────────────
                        + "0000000000000000000000000000000000000000000000000000000000000003" // length = 3
                        + "0102030000000000000000000000000000000000000000000000000000000000" // data
                        // +
                        // padding

                        // ──────────────────────────────── tail @ 0x1a0 (string)
                        // ────────────────────────────────
                        + "0000000000000000000000000000000000000000000000000000000000000005" // length = 5
                        + "776f726c64000000000000000000000000000000000000000000000000000000" // "world" + padding
                ;
        // @formatter:on

        Log log =
                new Log(
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "transactionHash",
                        "transactionIndex",
                        BigInteger.ZERO,
                        "topics",
                        logData,
                        "type",
                        List.of(
                                "0x1111111111111111111111111111111111111111111111111111111111111111"));
        Set<ContractEventParameter<?>> parameters = DECODER.decode(spec, log);
        assert parameters.size() == 8;

        List<ContractEventParameter<?>> list = new ArrayList<>(parameters);

        assert list.get(0) instanceof AddressParameter;
        assert list.get(1) instanceof ArrayParameter;

        Object val1 = list.get(1).getValue();
        assert val1 instanceof List<?>;
        List<?> nested = (List<?>) val1;
        assert nested.size() == 2;
        assert nested.getFirst() instanceof AddressParameter;

        assert list.get(2) instanceof BoolParameter;
        assert list.get(3) instanceof BytesFixedParameter;
        assert list.get(4) instanceof BytesParameter;
        assert list.get(5) instanceof IntParameter;
        assert list.get(6) instanceof StringParameter;
        assert list.get(7) instanceof UintParameter;
    }
}
