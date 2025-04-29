package io.librevents.application.common.util;

import java.nio.charset.StandardCharsets;

import org.bouncycastle.crypto.digests.KeccakDigest;

public final class EncryptionUtil {

    public static byte[] keccak256(byte[] input) {
        KeccakDigest keccak = new KeccakDigest(256);
        keccak.update(input, 0, input.length);
        byte[] out = new byte[32];
        keccak.doFinal(out, 0);
        return out;
    }

    public static String keccak256Hex(String text) {
        byte[] h = keccak256(text.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder("0x");
        for (byte b : h) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("Input hex string is null");
        }
        String s = hex.trim();
        if (s.startsWith("0x") || s.startsWith("0X")) {
            s = s.substring(2);
        }

        s = s.replaceAll("[^0-9A-Fa-f]", "");

        if ((s.length() & 1) == 1) {
            s = "0" + s;
        }

        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            int hi = Character.digit(s.charAt(i), 16);
            int lo = Character.digit(s.charAt(i + 1), 16);
            if (hi < 0 || lo < 0) {
                throw new IllegalArgumentException(
                        "Invalid hex digit at pos "
                                + i
                                + ": '"
                                + s.charAt(i)
                                + s.charAt(i + 1)
                                + "'");
            }
            data[i / 2] = (byte) ((hi << 4) + lo);
        }

        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
