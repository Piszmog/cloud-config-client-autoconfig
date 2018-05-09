# Config Server Commons

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.piszmog/cloud-config-client-autoconfig/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.github.piszmog/cloud-config-client-autoconfig)

## Description
Spring Auto-configuration library for [Cloud Config Client](https://github.com/Piszmog/cloud-config-client).

Creates Spring Beans for `DecryptConfigClient`, `EncryptConfigClient`, `FileConfigClient`, and `PublicKeyClient`. Simply
inject the beans where needed.

## Usage
To use, add the following as a dependency,

```xml
<dependency>
    <groupId>io.github.piszmog</groupId>
    <artifactId>cloud-config-client-autoconfig</artifactId>
    <version>#{cloud-config-client-autoconfig.version}</version>
</dependency>
```

### Local Config Server
Local Config Server is considered a locally running application setup as a Config Server. 
(see [Spring Config Server](https://github.com/spring-cloud/spring-cloud-config/tree/master/spring-cloud-config-server)).

### Spring Cloud Config Server (PCF)
When deploying applications to PCF, a Config Server service can be created to. Once created,
applications deployed to the space can bind to the service.

### Disabling Clients
To disable any of the clients, add the following to the application's configuration file.

```yaml
cloud:
  config:
    client:
      decrypt:
        enabled: false
      encrypt:
        enabled: false
      file:
        enabled: false
      publickey:
        enabled: false
```