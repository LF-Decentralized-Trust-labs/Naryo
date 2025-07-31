package io.naryo.application.filter.util;

import io.naryo.application.common.util.EncryptionUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BloomFilterUtilTest {

    @Test
    void test() {
        String bloomHex =
                "0x1e8f06355ebb584498b98373665c9ced4c7954dc09c056faa238108772abd9d31341d14130c2263210150f87bc370"
                        + "b8c8f1496130eb4ec39b8061cda1c242ca2243af328062561fc433da29ea6e52bb277600026018e0e9597f1"
                        + "1803d6c04700ce6a02482325d761c29ebd10060e3982204a19e6c2068f38cb91aa7d69e43a4746805a25d0b"
                        + "c575fe34b4cfe0da18cac83482a81d09a873e7d454c7594623adce37f934763001020d7114f610204d4cd8a"
                        + "c03d8b09110812a4d069224c3d28156e107c5615610a61524c0a4d2e133e5e27f448fe9ba3ad4b2e101dce0"
                        + "0c76def8c301f20c7c0343b778d18f266bca1d0b98a448c59a6c767131f0304081a4b4a";

        String signatureAscii =
                "DummyStructCreated(uint256,uint256,int256,bool,address,string,bytes32)";

        String topic0 = EncryptionUtil.sha3String(signatureAscii);
        assertEquals("0x3c74b9cf316663d283a8274522fc12e846930fde65cabdcba55b9e9197b81d70", topic0);

        assertTrue(BloomFilterUtil.match(bloomHex, topic0), "topic should match");

        String addressHex = "0x44A7Ed242f352158d8eEa76849FdD35a870fAC13";

        assertTrue(BloomFilterUtil.match(bloomHex, addressHex), "address should match");
        assertTrue(BloomFilterUtil.match(bloomHex, topic0, addressHex), "combined should match");
    }
}
