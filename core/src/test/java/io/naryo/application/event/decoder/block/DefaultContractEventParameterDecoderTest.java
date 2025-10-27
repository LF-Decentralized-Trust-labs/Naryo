package io.naryo.application.event.decoder.block;

import java.math.BigInteger;
import java.util.*;

import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.domain.event.contract.ContractEventParameter;
import io.naryo.domain.event.contract.parameter.*;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.EventFilterSpecificationBuilder;
import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameterdefinition.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DefaultContractEventParameterDecoderTest {

    private static final ContractEventParameterDecoder DECODER =
            new DefaultContractEventParameterDecoder();

    @Test
    void testDecodeIndexedParameters() {
        EventFilterSpecification spec =
                new EventFilterSpecificationBuilder()
                        .withParameters(
                                Set.of(
                                        new AddressParameterDefinitionBuilder()
                                                .withPosition(0)
                                                .withIndexed(true)
                                                .build(),
                                        new BoolParameterDefinitionBuilder()
                                                .withPosition(1)
                                                .withIndexed(true)
                                                .build(),
                                        new UintParameterDefinitionBuilder()
                                                .withBitSize(256)
                                                .withPosition(2)
                                                .withIndexed(true)
                                                .build(),
                                        new IntParameterDefinitionBuilder()
                                                .withBitSize(256)
                                                .withPosition(3)
                                                .withIndexed(true)
                                                .build(),
                                        new BytesFixedParameterDefinitionBuilder()
                                                .withByteLength(32)
                                                .withPosition(4)
                                                .withIndexed(true)
                                                .build(),
                                        new StringParameterDefinitionBuilder()
                                                .withPosition(5)
                                                .withIndexed(true)
                                                .build(),
                                        new BytesParameterDefinitionBuilder()
                                                .withPosition(6)
                                                .withIndexed(true)
                                                .build()))
                        .build();

        Log log =
                new Log(
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "transactionHash",
                        "transactionIndex",
                        BigInteger.ZERO,
                        "address",
                        "0x",
                        "type",
                        List.of(
                                "0x6bf60ad9f26b52fed767dbedcfa89323a558106febcc5a66b0cde8a0637756c8",
                                "0x0000000000000000000000001111111111111111111111111111111111111111",
                                "0x0000000000000000000000000000000000000000000000000000000000000001",
                                "0x000000000000000000000000000000000000000000000000000000000000002a",
                                "0xfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff9",
                                "0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                                "0xf4ceeeec3149dcacf7d3ff4fd3e1f2289ded4a0c7fb35d172c22596837203738",
                                "0xa6885b3731702da62e8e4a8f584ac46a7f6822f4e2ba50fba902f67b1588d23b"));
        Set<ContractEventParameter<?>> parameters = DECODER.decode(spec, log);
        assert parameters.size() == 7;

        List<ContractEventParameter<?>> list = new ArrayList<>(parameters);
        assert list.get(0) instanceof AddressParameter;
        assert list.get(1) instanceof BoolParameter;
        assert list.get(2) instanceof UintParameter;
        assert list.get(3) instanceof IntParameter;
        assert list.get(4) instanceof StringParameter;
        assert list.get(5) instanceof StringParameter;
        assert list.get(6) instanceof StringParameter;
    }

    @Test
    void testDecodeNonIndexedParameters() {
        EventFilterSpecification spec =
                new EventFilterSpecificationBuilder()
                        .withParameters(
                                Set.of(
                                        // 0: address addr
                                        new AddressParameterDefinitionBuilder()
                                                .withPosition(0)
                                                .build(),

                                        // 1: bool flag
                                        new BoolParameterDefinitionBuilder()
                                                .withPosition(1)
                                                .build(),

                                        // 2: bytes dynBytes   (dynamic)
                                        new BytesParameterDefinitionBuilder()
                                                .withPosition(2)
                                                .build(),

                                        // 3: bytes32 fixedBytes   (fixed)
                                        new BytesFixedParameterDefinitionBuilder()
                                                .withByteLength(32)
                                                .withPosition(3)
                                                .build(),

                                        // 4: string name
                                        new StringParameterDefinitionBuilder()
                                                .withPosition(4)
                                                .build(),

                                        // 5: uint value (uint256)
                                        new UintParameterDefinitionBuilder()
                                                .withBitSize(256)
                                                .withPosition(5)
                                                .build(),

                                        // 6: int signed (int256)
                                        new IntParameterDefinitionBuilder()
                                                .withBitSize(256)
                                                .withPosition(6)
                                                .build(),

                                        // 7: address[2] fixedAddrArray   (fixed-length array)
                                        new ArrayParameterDefinitionBuilder()
                                                .withPosition(7)
                                                .withElementType(
                                                        new AddressParameterDefinitionBuilder()
                                                                .build())
                                                .withFixedLength(2)
                                                .build(),

                                        // 8: uint[3] fixedUintArray   (fixed-length array)
                                        new ArrayParameterDefinitionBuilder()
                                                .withPosition(8)
                                                .withElementType(
                                                        new UintParameterDefinitionBuilder()
                                                                .withBitSize(256)
                                                                .build())
                                                .withFixedLength(3)
                                                .build(),

                                        // 9: string[] dynStringArray   (dynamic array)
                                        new ArrayParameterDefinitionBuilder()
                                                .withPosition(9)
                                                .withElementType(
                                                        new StringParameterDefinitionBuilder()
                                                                .build())
                                                .withFixedLength(null)
                                                .build(),

                                        // 10: bytes[] dynBytesArray   (dynamic array)
                                        new ArrayParameterDefinitionBuilder()
                                                .withPosition(10)
                                                .withElementType(
                                                        new BytesParameterDefinitionBuilder()
                                                                .build())
                                                .withFixedLength(null)
                                                .build(),

                                        // 11: (string s, uint n, bytes32 b) simpleTuple
                                        new StructParameterDefinitionBuilder()
                                                .withPosition(11)
                                                .withParameterDefinitions(
                                                        Set.of(
                                                                new StringParameterDefinitionBuilder()
                                                                        .withPosition(0)
                                                                        .build(),
                                                                new UintParameterDefinitionBuilder()
                                                                        .withBitSize(256)
                                                                        .withPosition(1)
                                                                        .build(),
                                                                new BytesFixedParameterDefinitionBuilder()
                                                                        .withByteLength(32)
                                                                        .withPosition(2)
                                                                        .build()))
                                                .build(),

                                        // 12: (uint n, bytes32 b, boolean c) simpleStaticTuple
                                        new StructParameterDefinitionBuilder()
                                                .withPosition(12)
                                                .withParameterDefinitions(
                                                        Set.of(
                                                                new UintParameterDefinitionBuilder()
                                                                        .withBitSize(256)
                                                                        .withPosition(0)
                                                                        .build(),
                                                                new BytesFixedParameterDefinitionBuilder()
                                                                        .withByteLength(32)
                                                                        .withPosition(1)
                                                                        .build(),
                                                                new BoolParameterDefinitionBuilder()
                                                                        .withPosition(2)
                                                                        .build()))
                                                .build(),

                                        // 13: (string title, (string inner, uint m) nested, uint[]
                                        // nums) nestedTuple
                                        new StructParameterDefinitionBuilder()
                                                .withPosition(13)
                                                .withParameterDefinitions(
                                                        Set.of(
                                                                // title: string
                                                                new StringParameterDefinitionBuilder()
                                                                        .withPosition(0)
                                                                        .build(),
                                                                // nested: (string inner, uint m)
                                                                new StructParameterDefinitionBuilder()
                                                                        .withPosition(1)
                                                                        .withParameterDefinitions(
                                                                                Set.of(
                                                                                        new StringParameterDefinitionBuilder()
                                                                                                .withPosition(
                                                                                                        0)
                                                                                                .build(),
                                                                                        new UintParameterDefinitionBuilder()
                                                                                                .withBitSize(
                                                                                                        256)
                                                                                                .withPosition(
                                                                                                        1)
                                                                                                .build()))
                                                                        .build(),
                                                                // nums: uint[] (dynamic)
                                                                new ArrayParameterDefinitionBuilder()
                                                                        .withPosition(2)
                                                                        .withElementType(
                                                                                new UintParameterDefinitionBuilder()
                                                                                        .withBitSize(
                                                                                                256)
                                                                                        .build())
                                                                        .withFixedLength(null)
                                                                        .build()))
                                                .build(),

                                        // 14: (string t, uint n)[] arrayOfTuples   (dynamic array
                                        // of structs)
                                        new ArrayParameterDefinitionBuilder()
                                                .withPosition(14)
                                                .withElementType(
                                                        new StructParameterDefinitionBuilder()
                                                                .withParameterDefinitions(
                                                                        Set.of(
                                                                                new StringParameterDefinitionBuilder()
                                                                                        .withPosition(
                                                                                                0)
                                                                                        .build(),
                                                                                new UintParameterDefinitionBuilder()
                                                                                        .withBitSize(
                                                                                                256)
                                                                                        .withPosition(
                                                                                                1)
                                                                                        .build()))
                                                                .build())
                                                .withFixedLength(null)
                                                .build()))
                        .build();

        // @formatter:off
        String logData =
                "0x"
                        + "0000000000000000000000001111111111111111111111111111111111111111" // [0]
                        // @0x000 (0 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000001" // [1]
                        // @0x020 (32 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000280" // [2]
                        // @0x040 (64 bytes)
                        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" // [3]
                        // @0x060 (96 bytes)
                        + "00000000000000000000000000000000000000000000000000000000000002c0" // [4]
                        // @0x080 (128 bytes)
                        + "000000000000000000000000000000000000000000000000000000000000002a" // [5]
                        // @0x0a0 (160 bytes)
                        + "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff9" // [6]
                        // @0x0c0 (192 bytes)
                        + "0000000000000000000000001111111111111111111111111111111111111111" // [7]
                        // @0x0e0 (224 bytes)
                        + "0000000000000000000000002222222222222222222222222222222222222222" // [8]
                        // @0x100 (256 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000001" // [9]
                        // @0x120 (288 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000002" // [10]
                        // @0x140 (320 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000003" // [11]
                        // @0x160 (352 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000300" // [12]
                        // @0x180 (384 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000440" // [13]
                        // @0x1a0 (416 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000560" // [14]
                        // @0x1c0 (448 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000032" // [15]
                        // @0x1e0 (480 bytes)
                        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" // [16]
                        // @0x200 (512 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000000" // [17]
                        // @0x220 (544 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000600" // [18]
                        // @0x240 (576 bytes)
                        + "00000000000000000000000000000000000000000000000000000000000007a0" // [19]
                        // @0x260 (608 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000004" // [20]
                        // @0x280 (640 bytes)
                        + "0102030400000000000000000000000000000000000000000000000000000000" // [21]
                        // @0x2a0 (672 bytes)
                        + "000000000000000000000000000000000000000000000000000000000000000b" // [22]
                        // @0x2c0 (704 bytes)
                        + "48656c6c6f2c2041424921000000000000000000000000000000000000000000" // [23]
                        // @0x2e0 (736 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000003" // [24]
                        // @0x300 (768 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000060" // [25]
                        // @0x320 (800 bytes)
                        + "00000000000000000000000000000000000000000000000000000000000000a0" // [26]
                        // @0x340 (832 bytes)
                        + "00000000000000000000000000000000000000000000000000000000000000e0" // [27]
                        // @0x360 (864 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000003" // [28]
                        // @0x380 (896 bytes)
                        + "666f6f0000000000000000000000000000000000000000000000000000000000" // [29]
                        // @0x3a0 (928 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000003" // [30]
                        // @0x3c0 (960 bytes)
                        + "6261720000000000000000000000000000000000000000000000000000000000" // [31]
                        // @0x3e0 (992 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000003" // [32]
                        // @0x400 (1024 bytes)
                        + "62617a0000000000000000000000000000000000000000000000000000000000" // [33]
                        // @0x420 (1056 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000003" // [34]
                        // @0x440 (1088 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000060" // [35]
                        // @0x460 (1120 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000080" // [36]
                        // @0x480 (1152 bytes)
                        + "00000000000000000000000000000000000000000000000000000000000000c0" // [37]
                        // @0x4a0 (1184 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000000" // [38]
                        // @0x4c0 (1216 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000004" // [39]
                        // @0x4e0 (1248 bytes)
                        + "deadbeef00000000000000000000000000000000000000000000000000000000" // [40]
                        // @0x500 (1280 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000003" // [41]
                        // @0x520 (1312 bytes)
                        + "abcdef0000000000000000000000000000000000000000000000000000000000" // [42]
                        // @0x540 (1344 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000060" // [43]
                        // @0x560 (1376 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000007" // [44]
                        // @0x580 (1408 bytes)
                        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" // [45]
                        // @0x5a0 (1440 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000003" // [46]
                        // @0x5c0 (1472 bytes)
                        + "53594d0000000000000000000000000000000000000000000000000000000000" // [47]
                        // @0x5e0 (1504 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000060" // [48]
                        // @0x600 (1536 bytes)
                        + "00000000000000000000000000000000000000000000000000000000000000a0" // [49]
                        // @0x620 (1568 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000120" // [50]
                        // @0x640 (1600 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000005" // [51]
                        // @0x660 (1632 bytes)
                        + "5469746c65000000000000000000000000000000000000000000000000000000" // [52]
                        // @0x680 (1664 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000040" // [53]
                        // @0x6a0 (1696 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000002" // [54]
                        // @0x6c0 (1728 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000005" // [55]
                        // @0x6e0 (1760 bytes)
                        + "496e6e6572000000000000000000000000000000000000000000000000000000" // [56]
                        // @0x700 (1792 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000003" // [57]
                        // @0x720 (1824 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000005" // [58]
                        // @0x740 (1856 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000006" // [59]
                        // @0x760 (1888 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000007" // [60]
                        // @0x780 (1920 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000003" // [61]
                        // @0x7a0 (1952 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000060" // [62]
                        // @0x7c0 (1984 bytes)
                        + "00000000000000000000000000000000000000000000000000000000000000e0" // [63]
                        // @0x7e0 (2016 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000160" // [64]
                        // @0x800 (2048 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000040" // [65]
                        // @0x820 (2080 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000001" // [66]
                        // @0x840 (2112 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000001" // [67]
                        // @0x860 (2144 bytes)
                        + "4100000000000000000000000000000000000000000000000000000000000000" // [68]
                        // @0x880 (2176 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000040" // [69]
                        // @0x8a0 (2208 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000002" // [70]
                        // @0x8c0 (2240 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000001" // [71]
                        // @0x8e0 (2272 bytes)
                        + "4200000000000000000000000000000000000000000000000000000000000000" // [72]
                        // @0x900 (2304 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000040" // [73]
                        // @0x920 (2336 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000003" // [74]
                        // @0x940 (2368 bytes)
                        + "0000000000000000000000000000000000000000000000000000000000000001" // [75]
                        // @0x960 (2400 bytes)
                        + "4300000000000000000000000000000000000000000000000000000000000000"; // [76] @0x980 (2432 bytes)
        // @formatter:on

        Log log =
                new Log(
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "transactionHash",
                        "transactionIndex",
                        BigInteger.ZERO,
                        "address",
                        logData,
                        "type",
                        List.of(
                                "0xf43dc5b698a0eca5ec149d80a9d34ede4840344b04652ec356f720e51f55619e"));
        Set<ContractEventParameter<?>> parameters = DECODER.decode(spec, log);
        assert parameters.size() == 15;

        List<ContractEventParameter<?>> list = new ArrayList<>(parameters);
        assert list.get(0) instanceof AddressParameter;
        assert list.get(1) instanceof BoolParameter;
        assert list.get(2) instanceof BytesParameter;
        assert list.get(3) instanceof BytesFixedParameter;
        assert list.get(4) instanceof StringParameter;
        assert list.get(5) instanceof UintParameter;
        assert list.get(6) instanceof IntParameter;
        assert list.get(7) instanceof ArrayParameter;
        assert list.get(8) instanceof ArrayParameter;
        assert list.get(9) instanceof ArrayParameter;
        assert list.get(10) instanceof ArrayParameter;
        assert list.get(11) instanceof StructParameter;
        assert list.get(12) instanceof StructParameter;
        assert list.get(13) instanceof StructParameter;
        assert list.get(14) instanceof ArrayParameter;
    }

    @Test
    void decodesStaticOnlyStructInPlaceWithoutOffset() {
        // This test defines a struct S = (uint256 a [pos 0], bool b [pos 1], bytes32 c [pos 2])
        ParameterDefinition staticOnlyStruct =
                new StructParameterDefinitionBuilder()
                        .withPosition(0)
                        .withParameterDefinitions(
                                Set.of(
                                        new UintParameterDefinitionBuilder()
                                                .withBitSize(256)
                                                .withPosition(0)
                                                .build(),
                                        new BoolParameterDefinitionBuilder()
                                                .withPosition(1)
                                                .build(),
                                        new BytesFixedParameterDefinitionBuilder()
                                                .withByteLength(32)
                                                .withPosition(2)
                                                .build()))
                        .build();

        EventFilterSpecification spec =
                new EventFilterSpecificationBuilder()
                        .withParameters(Set.of(staticOnlyStruct))
                        .build();

        // ABI-encoded data for S at head:
        // [0x00] uint256 a = 0x2a
        // [0x20] bool b = 1
        // [0x40] bytes32 c = 0x01020304... (first 4 bytes set, rest zero)
        String data =
                "0x"
                        // a = 42
                        + "000000000000000000000000000000000000000000000000000000000000002a"
                        // b = true
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        // c = 0x01020304 padded to 32 bytes
                        + "0102030400000000000000000000000000000000000000000000000000000000";

        Log log =
                new Log(
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "txHash",
                        "txIndex",
                        BigInteger.ZERO,
                        "address",
                        data,
                        "type",
                        List.of(/*irrelevant to this test */ ));

        Set<ContractEventParameter<?>> decoded = DECODER.decode(spec, log);
        assertEquals(1, decoded.size());

        ContractEventParameter<?> only = new ArrayList<>(decoded).get(0);
        assertTrue(only instanceof StructParameter);

        StructParameter struct = (StructParameter) only;
        List<ContractEventParameter<?>> fields = struct.getValue();

        assertEquals(3, fields.size());

        // a: uint256 = 42
        assertTrue(fields.get(0) instanceof UintParameter);
        assertEquals(42, ((UintParameter) fields.get(0)).getValue());

        // b: bool = true
        assertTrue(fields.get(1) instanceof io.naryo.domain.event.contract.parameter.BoolParameter);
        assertEquals(true, ((BoolParameter) fields.get(1)).getValue());

        // c: bytes32 begins with 0x01020304...
        assertTrue(fields.get(2) instanceof BytesFixedParameter);
        byte[] c = ((BytesFixedParameter) fields.get(2)).getValue();
        assertEquals(32, c.length);
        assertEquals((byte) 0x01, c[0]);
        assertEquals((byte) 0x02, c[1]);
        assertEquals((byte) 0x03, c[2]);
        assertEquals((byte) 0x04, c[3]);
    }
}
