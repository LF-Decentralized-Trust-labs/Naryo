package io.librevents.application.node;

import java.io.IOException;

import io.reactivex.disposables.CompositeDisposable;

public interface NodeRunner {

    CompositeDisposable run() throws IOException;
}
