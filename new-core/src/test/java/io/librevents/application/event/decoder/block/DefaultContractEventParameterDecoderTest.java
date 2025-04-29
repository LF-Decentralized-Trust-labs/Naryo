package io.librevents.application.event.decoder.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.librevents.application.event.decoder.ContractEventParameterDecoder;
import io.librevents.domain.common.EventName;
import io.librevents.domain.event.contract.ContractEventParameter;
import io.librevents.domain.event.contract.parameter.*;
import io.librevents.domain.filter.event.CorrelationId;
import io.librevents.domain.filter.event.EventFilterSpecification;
import io.librevents.domain.filter.event.parameter.*;
import org.junit.jupiter.api.Test;

class DefaultContractEventParameterDecoderTest {

    private static final ContractEventParameterDecoder DECODER =
            new DefaultContractEventParameterDecoder();

    @Test
    void testDecode() {
        EventFilterSpecification spec =
                new EventFilterSpecification(
                        new EventName("Test"),
                        new CorrelationId(0),
                        Set.of(
                                new AddressParameterDefinition(0, false),
                                new ArrayParameterDefinition(
                                        1, new AddressParameterDefinition(), null),
                                new BoolParameterDefinition(2, false),
                                new BytesFixedParameterDefinition(32, 3, false),
                                new BytesParameterDefinition(4),
                                new IntParameterDefinition(256, 5, false),
                                new StringParameterDefinition(6),
                                new UintParameterDefinition(256, 7, false)));

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
                        + "00000000000000000000000000000000000000000000000000000000000004d2" // param7: int256(1234)
                        + "00000000000000000000000000000000000000000000000000000000000001a0" // param6: offset → 0x1a0
                        + "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" // param5: uint256(-1)

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

        Set<ContractEventParameter<?>> parameters = DECODER.decode(spec, logData);
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
