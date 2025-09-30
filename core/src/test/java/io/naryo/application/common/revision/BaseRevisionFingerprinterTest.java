package io.naryo.application.common.revision;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import io.naryo.application.configuration.revision.fingerprint.DefaultRevisionFingerprinter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class BaseRevisionFingerprinterTest<T> {

    protected DefaultRevisionFingerprinter<T> fingerprinter;
    protected final Function<T, UUID> idFn;

    protected BaseRevisionFingerprinterTest(Function<T, UUID> idFn) {
        this.idFn = idFn;
    }

    protected abstract T createInput();

    @Test
    void itemHash_outputsHex() {
        var in = createInput();

        String out = fingerprinter.itemHash(in);

        assertNotNull(out);
        assertTrue(out.matches("(?i)^(0x)?[0-9a-f]{64}$"));
    }

    @Test
    void itemHash_withDifferentInputs() {
        T in = null;
        T secIn = null;
        var equals = true;

        while (equals) {
            in = createInput();
            secIn = createInput();
            equals = in.equals(secIn);
        }

        String out = fingerprinter.itemHash(in);
        String secOut = fingerprinter.itemHash(secIn);

        assertNotEquals(secOut, out);
    }

    @Test
    void itemHash_withSameInputs() {
        var in = createInput();

        String out = fingerprinter.itemHash(in);
        String secOut = fingerprinter.itemHash(in);

        assertEquals(out, secOut);
    }

    @Test
    void itemHash_withNullInput() {
        assertThrows(NullPointerException.class, () -> fingerprinter.itemHash(null));
    }

    @Test
    void revisionHash_withDifferentInputs() {
        T in = createInput();
        T secIn = createInput();

        T bIn = createInput();
        T bSecIn = createInput();

        String out = fingerprinter.revisionHash(List.of(in, secIn), idFn);
        String secOut = fingerprinter.revisionHash(List.of(bIn, bSecIn), idFn);

        assertNotEquals(out, secOut);
    }

    @Test
    void revisionHash_withSameInputs() {
        T in = createInput();
        T secIn = createInput();

        String out = fingerprinter.revisionHash(List.of(in, secIn), idFn);
        String secOut = fingerprinter.revisionHash(List.of(in, secIn), idFn);

        assertEquals(out, secOut);
    }
}
