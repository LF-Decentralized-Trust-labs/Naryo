package io.librevents.application.filter.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static io.librevents.application.common.util.EncryptionUtil.*;

public final class BloomFilterUtil {

    public static List<Integer> getBloomBitsForFilter(
            String contractAddressHex, String eventSignature) {
        byte[] contractAddressHash = keccak256(hexStringToByteArray(contractAddressHex));
        List<Integer> contractAddressBits = getBloomBits(contractAddressHash);

        byte[] eventSpecHash = keccak256(eventSignature.getBytes(StandardCharsets.UTF_8));
        List<Integer> eventSpecBits = getBloomBits(eventSpecHash);

        return Stream.of(contractAddressBits, eventSpecBits).flatMap(Collection::stream).toList();
    }

    public static List<Integer> getBloomBits(byte[] hash) {
        List<Integer> bits = new ArrayList<>();
        for (int i = 0; i < 6; i += 2) {
            int twoBytes = ((hash[i] & 0xFF) << 8) | (hash[i + 1] & 0xFF);
            bits.add(twoBytes & 0x07FF);
        }
        return bits;
    }

    public static boolean bloomFilterMatch(String bloomFilterHex, String eventSignature) {
        byte[] eventSignatureHash = keccak256(eventSignature.getBytes(StandardCharsets.UTF_8));
        List<Integer> eventBits = getBloomBits(eventSignatureHash);
        return bloomFilterMatch(bloomFilterHex, eventBits);
    }

    public static boolean bloomFilterMatch(String bloomFilterHex, List<Integer> bitsToMatch) {
        byte[] bloom = hexStringToByteArray(bloomFilterHex);
        if (bloom.length != 256) {
            throw new IllegalArgumentException("Bloom must be 256 bytes, was " + bloom.length);
        }

        for (int bit : bitsToMatch) {
            int byteIndex = bit >>> 3;
            int bitIndex = bit & 7;
            int b = bloom[byteIndex] & 0xFF;
            if (((b >>> bitIndex) & 1) == 0) {
                return false;
            }
        }
        return true;
    }
}
