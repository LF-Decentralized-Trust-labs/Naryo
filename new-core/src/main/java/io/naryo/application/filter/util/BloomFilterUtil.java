package io.naryo.application.filter.util;

import static io.naryo.application.common.util.EncryptionUtil.*;

public final class BloomFilterUtil {

    public static boolean bloomFilterMatch(String bloomHex, String eventSignature) {
        return checkBitsInBloom(arrayify(bloomHex), sha3(eventSignature));
    }

    public static boolean bloomFilterMatch(
            String bloomHex, String eventSignature, String contractAddress) {
        return bloomFilterMatch(bloomHex, eventSignature)
                && checkBitsInBloom(arrayify(bloomHex), sha3(contractAddress));
    }

    public static boolean checkBitsInBloom(byte[] bloom, byte[] hash) {
        int[] bits = getBloomBits(hash);
        boolean result = true;

        for (int bit : bits) {
            int bytePos = bloom.length - 1 - (bit / 8);
            int offset = 1 << (bit % 8);
            int value = bloom[bytePos] & 0xFF;

            System.out.printf(
                    "Bit %d (byte %d, bit %d): byte=0x%02x (%8s), required=0x%02x -> %s\n",
                    bit,
                    bytePos,
                    (bit % 8),
                    value,
                    String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0'),
                    offset,
                    ((value & offset) == offset ? "ACTIVE" : "NOT SET"));

            if ((value & offset) != offset) {
                result = false;
            }
        }
        return result;
    }

    public static byte[] buildBloom(String input) {
        byte[] bloom = new byte[256];
        int[] bits = getBloomBits(sha3(input));
        for (int bitpos : bits) {
            int bytePos = bloom.length - 1 - (bitpos / 8);
            int offset = 1 << (bitpos % 8);
            bloom[bytePos] |= (byte) offset;
        }
        return bloom;
    }

    public static int[] getBloomBits(byte[] hash) {
        int[] bits = new int[3];
        for (int i = 0; i < 3; i++) {
            int hi = hash[i * 2] & 0xFF;
            int lo = hash[i * 2 + 1] & 0xFF;
            bits[i] = ((hi << 8) | lo) & 2047;
        }
        return bits;
    }
}
