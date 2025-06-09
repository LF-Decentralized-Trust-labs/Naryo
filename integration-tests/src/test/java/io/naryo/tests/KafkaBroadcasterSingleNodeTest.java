package io.naryo.tests;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"mongo", "kafka"})
public class KafkaBroadcasterSingleNodeTest extends BaseKafkaBroadcasterTest {}
