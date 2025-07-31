package io.naryo.application.common.util;

import java.nio.charset.StandardCharsets;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.web3j.utils.Strings;

public final class EncryptionUtil {

    private static final String HEX_PREFIX = "0x";
    private static final char[] HEX_CHAR_MAP = "0123456789abcdef".toCharArray();

    public static byte[] sha3(byte[] input) {
        Keccak.DigestKeccak kecc = new Keccak.Digest256();
        kecc.update(input, 0, input.length);
        return kecc.digest();
    }

    public static byte[] sha3(String input) {
        return sha3(
                containsHexPrefix(input)
                        ? arrayify(input)
                        : input.getBytes(StandardCharsets.UTF_8));
    }

    public static String sha3String(String input) {
        return hexlify(sha3(input));
    }

    public static String hexlify(byte[] data) {
        final String output = new String(toHexCharArray(data, data.length));
        return HEX_PREFIX + output;
    }

    /**
     * This method assumes the input is a valid hex string
     *
     * @param input Hex string
     * @return Byte array
     */
    public static byte[] arrayify(String input) {
        String cleanInput = cleanHexPrefix(input);

        int len = cleanInput.length();

        if (len == 0) {
            return new byte[] {};
        }

        byte[] data;
        int startIdx;
        if (len % 2 != 0) {
            data = new byte[(len / 2) + 1];
            data[0] = (byte) Character.digit(cleanInput.charAt(0), 16);
            startIdx = 1;
        } else {
            data = new byte[len / 2];
            startIdx = 0;
        }

        for (int i = startIdx; i < len; i += 2) {
            data[(i + 1) / 2] =
                    (byte)
                            ((Character.digit(cleanInput.charAt(i), 16) << 4)
                                    + Character.digit(cleanInput.charAt(i + 1), 16));
        }
        return data;
    }

    public static String cleanHexPrefix(String input) {
        if (containsHexPrefix(input)) {
            return input.substring(2);
        } else {
            return input;
        }
    }

    public static boolean containsHexPrefix(String input) {
        return !Strings.isEmpty(input)
                && input.length() > 1
                && input.charAt(0) == '0'
                && input.charAt(1) == 'x';
    }

    private static char[] toHexCharArray(byte[] input, int length) {
        final char[] output = new char[length << 1];
        for (int i = 0, j = 0; i < length; i++, j++) {
            final int v = input[i] & 0xFF;
            output[j++] = HEX_CHAR_MAP[v >>> 4];
            output[j] = HEX_CHAR_MAP[v & 0x0F];
        }
        return output;
    }
}
