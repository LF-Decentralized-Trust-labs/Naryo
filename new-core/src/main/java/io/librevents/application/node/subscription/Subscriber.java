package io.librevents.application.node.subscription;

import io.reactivex.disposables.Disposable;

public interface Subscriber {

    Disposable subscribe();

}
