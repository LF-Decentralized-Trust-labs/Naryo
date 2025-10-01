package io.naryo.application.node;

import java.io.IOException;

import io.naryo.domain.node.Node;
import io.reactivex.disposables.CompositeDisposable;

public interface NodeRunner {

    CompositeDisposable run() throws IOException;

    Node getNode();
}
