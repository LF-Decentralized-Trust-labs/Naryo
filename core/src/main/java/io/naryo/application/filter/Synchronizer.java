package io.naryo.application.filter;

import java.io.IOException;

import io.reactivex.disposables.Disposable;

public interface Synchronizer {

    Disposable synchronize() throws IOException;
}
