package io.librevents.application.filter;

import io.reactivex.disposables.Disposable;

public interface Synchronizer {

    Disposable synchronize();

}
