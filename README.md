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
    <version>${cloud-config-client-autoconfig.version}</version>
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

### Loading Files
`FileConfigClient` bean allows for the ability to pull down files either from a specified branch or from the default branch.

If pulling from the default branch, files __must__ be located in a subdirectory.

## Adding JSON Files as Property Sources
The Config Server Client will only pull down `.properties` and `.yml`/`.yaml` files for an application's configuration (property sources). As 
more microservices move to _code by configuration_, these files can get very large and hard to maintain. Splitting configurations 
out into JSON files that do not need to follow the Config Server's [naming convention](https://cloud.spring.io/spring-cloud-static/spring-cloud-config/1.3.1.RELEASE/#_quick_start) 
can help to better organize an application configuration.

Leveraging the `FileConfigClient` bean, JSON files can be loaded as property sources. This allows for `ConfigurationProperties` 
to have JSON values be injected into their fields.

To add a file as a property source, update the application configuration to have the following,
```yaml
cloud:
  config:
    client:
      file:
        resources:
          - directory: <sub-directory path>
            files:
              - <file>
              - <file>
              ...
          - directory: <sub-directory path>
            files:
              - <file>
              - <file>
              ...
          ...
```

Where,
- `<sub-directory path>` is the directory path to the following files -- example `nonprod/example` or `configs`
  - Sub-directory __must__ not be `null` or blank.
- `<file>` is the JSON, YAML, or Properties file to load that is located in the specified sub-directory

### Specifying Configuration
Adding JSON files to be property sources can either be done in the `bootstrap.yml` (embedded with the application), in an embedded 
application configuration, or in the Config Server.