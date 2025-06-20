package io.naryo.application.filter.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static io.naryo.application.common.util.EncryptionUtil.*;

public final class BloomFilterUtil {

    private static final int BYTES_LENGTH = 256;

    public static boolean match(String bloomBytes, String... topics) {
        byte[] bytes = arrayify(bloomBytes);
        if (topics == null) {
            throw new IllegalArgumentException("topics can not be null");
        }
        for (String topic : topics) {
            if (!match(bytes, topic)) {
                return false;
            }
        }
        return true;
    }

    public static byte[] buildBloom(String... topics) {
        byte[] bloom = new byte[256];
        for (String topic : topics) {
            BloomValues values = getBloomValues(arrayify(topic));
            for (int i = 0; i < 3; i++) {
                bloom[values.index[i]] |= values.value[i];
            }
        }
        return bloom;
    }

    private static boolean match(byte[] bytes, String topic) {
        BloomValues b = getBloomValues(arrayify(topic));
        return b.value[0] == (b.value[0] & bytes[b.index[0]])
                && b.value[1] == (b.value[1] & bytes[b.index[1]])
                && b.value[2] == (b.value[2] & bytes[b.index[2]]);
    }

    private static BloomValues getBloomValues(byte[] item) {
        byte[] hash = sha3(item);
        byte v1 = (byte) (1 << (hash[1] & 0x7));
        byte v2 = (byte) (1 << (hash[3] & 0x7));
        byte v3 = (byte) (1 << (hash[5] & 0x7));
        ByteBuffer byteBuffer = ByteBuffer.wrap(hash).order(ByteOrder.BIG_ENDIAN);
        int i1 = BYTES_LENGTH - ((byteBuffer.getShort(0) & 0x7ff) >> 3) - 1;
        int i2 = BYTES_LENGTH - ((byteBuffer.getShort(2) & 0x7ff) >> 3) - 1;
        int i3 = BYTES_LENGTH - ((byteBuffer.getShort(4) & 0x7ff) >> 3) - 1;
        return new BloomValues(new byte[] {v1, v2, v3}, new int[] {i1, i2, i3});
    }

    private record BloomValues(byte[] value, int[] index) {}
}
