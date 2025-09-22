package io.naryo.application.configuration.revision;

import java.util.Map;

public interface LiveView<T> {

    Revision<T> revision();

    Map<String, T> byId();

    Map<String, String> itemFingerprintById();
}
