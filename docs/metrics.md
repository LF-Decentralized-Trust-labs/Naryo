# Metrics

## Prometheus

Naryo includes a prometheus metrics export endpoint.

It includes standard jvm, tomcat metrics enabled by
spring-boot https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-metrics-export-prometheus https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-metrics-meter.

Added to the standard metrics, custom metrics have been added:

* naryo_%Network%_syncing: 1 if node is syncing (latestBlock + syncingThresholds < currentBlock). 0 if not syncing
* naryo_%Network%_latestBlock: latest block read by Naryo
* naryo_%Network%_currentBlock: Current node block
* naryo_%Network%_status: Current node status. 0 = Subscribed, 1 = Connected, 2 = Down

All metrics include application="Naryo",environment="local" tags.

The endpoint is: GET /monitoring/prometheus

## Next Steps

- [Known Caveats / Issues](issues.md)

## Previous Steps

- [Getting started](getting_started.md)
- [Configuration](configuration.md)
- [Usage](usage.md)
- [Metrics](metrics.md)
