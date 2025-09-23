package io.naryo.application.broadcaster.revision;

import io.naryo.application.common.revision.DefaultRevisionFingerprinter;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterNormalizer;

public final class BroadcasterRevisionFingerprinter
        extends DefaultRevisionFingerprinter<Broadcaster> {

    public BroadcasterRevisionFingerprinter() {
        this(BroadcasterNormalizer.INSTANCE);
    }

    BroadcasterRevisionFingerprinter(BroadcasterNormalizer normalizer) {
        super(normalizer);
    }
}
