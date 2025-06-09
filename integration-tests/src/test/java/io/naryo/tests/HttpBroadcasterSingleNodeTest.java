package io.naryo.tests;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"mongo", "http"})
public class HttpBroadcasterSingleNodeTest extends BaseHttpBroadcasterTest {}
