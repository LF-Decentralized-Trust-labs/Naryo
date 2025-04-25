package io.librevents.application.node;

import io.librevents.application.filter.Synchronizer;
import io.librevents.application.node.subscription.Subscriber;
import io.librevents.domain.node.Node;
import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;

public final class DefaultNodeRunner implements NodeRunner {

    @Getter private final Node node;
    private final Subscriber subscriber;
    private final Synchronizer synchronizer;

    public DefaultNodeRunner(Node node, Subscriber subscriber, Synchronizer synchronizer) {
        this.node = node;
        this.subscriber = subscriber;
        this.synchronizer = synchronizer;
    }

    @Override
    public CompositeDisposable run() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(subscriber.subscribe());
        disposable.add(synchronizer.synchronize());
        return disposable;
    }

}
