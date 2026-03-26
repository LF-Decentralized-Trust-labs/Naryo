# Configuration API Overview

This document describes how to configure **Naryo** using a configuration API via the `configuration-spring-api` module.
This API allows introducing changes that will be directly applied at runtime without the need to restart the service.

## Table of Contents

- [Static swagger file](#static-swagger-file)
- [Swagger UI](#swagger-ui)

## Static swagger file

You can find the swagger file in [./swagger.json](./swagger.json) or in the jar file under `META-INF/resources/swagger.json`.

Use this file to generate the client code.

## Swagger UI

Swagger can be accessed in `{host:port}/swagger-ui/index.html`.

All the endpoints, request and response models can be found there.

## Next steps

1. [Tutorials](../../tutorials/index.md)
2. [Configuration Overview](../index.md)
