package io.librevents.application.node;

import io.reactivex.disposables.CompositeDisposable;

public interface NodeRunner {

    CompositeDisposable run();

}
