package io.naryo.application.broadcaster.revision;

import io.naryo.application.common.revision.BaseRevisionFingerprinterTest;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterBuilder;
import org.junit.jupiter.api.BeforeEach;

class BroadcasterRevisionFingerprinterTest extends BaseRevisionFingerprinterTest<Broadcaster> {

    protected BroadcasterRevisionFingerprinterTest() {
        super(Broadcaster::getId);
    }

    @BeforeEach
    void setUp() {
        fingerprinter = new BroadcasterRevisionFingerprinter();
    }

    @Override
    protected Broadcaster createInput() {
        return new BroadcasterBuilder().build();
    }
}
