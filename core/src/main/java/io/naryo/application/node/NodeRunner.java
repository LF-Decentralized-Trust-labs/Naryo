package io.naryo.application.node;

import java.io.IOException;

import io.naryo.application.filter.Synchronizer;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.subscription.Subscriber;
import io.naryo.domain.node.Node;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public interface NodeRunner {

    CompositeDisposable run() throws IOException;

    Node getNode();

    Subscriber getSubscriber();

    Synchronizer getSynchronizer();

    Dispatcher getDispatcher();

    void addWorkflow(Disposable disposable);

    void stop();

    boolean isRunning();
}
