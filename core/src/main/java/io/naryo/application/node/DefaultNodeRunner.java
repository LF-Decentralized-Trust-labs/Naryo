package io.naryo.application.node;

import java.io.IOException;

import io.naryo.application.filter.Synchronizer;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.subscription.Subscriber;
import io.naryo.domain.node.Node;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lombok.Getter;

public final class DefaultNodeRunner implements NodeRunner {

    private @Getter final Node node;
    private final @Getter Subscriber subscriber;
    private final @Getter Synchronizer synchronizer;
    private final @Getter Dispatcher dispatcher;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public DefaultNodeRunner(
            Node node, Subscriber subscriber, Synchronizer synchronizer, Dispatcher dispatcher) {
        this.node = node;
        this.subscriber = subscriber;
        this.synchronizer = synchronizer;
        this.dispatcher = dispatcher;
    }

    @Override
    public CompositeDisposable run() throws IOException {
        disposables.add(subscriber.subscribe());
        disposables.add(synchronizer.synchronize());
        return disposables;
    }

    @Override
    public void addWorkflow(Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    public void stop() {
        disposables.dispose();
    }

    @Override
    public boolean isRunning() {
        return !disposables.isDisposed();
    }
}
