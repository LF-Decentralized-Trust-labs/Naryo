package io.librevents.application.node.subscription;

import java.io.IOException;

import io.reactivex.disposables.Disposable;

public interface Subscriber {

    Disposable subscribe() throws IOException;
}
