package io.naryo.application.node.subscription;

import java.io.IOException;

import io.naryo.application.node.interactor.Interactor;
import io.reactivex.disposables.Disposable;

public interface Subscriber {

    Disposable subscribe() throws IOException;

    Interactor getInteractor();
}
