package io.github.piszmog.cloudconfigclient.autoconfig.template;

import io.github.piszmog.cloudconfig.template.ConfigTemplate;
import io.pivotal.spring.cloud.config.client.ConfigClientOAuth2Properties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;


public class OAuth2ConfigTemplateBootStrapConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(OAuth2ConfigTemplateBootStrapConfiguration.class));

    @Test
    public void testConfigurationNotCreated() {
        this.contextRunner
                .withUserConfiguration(OAuth2ConfigTemplateBootStrapConfiguration.class)
                .run((context) -> assertThat(context).doesNotHaveBean(OAuth2ConfigTemplateBootStrapConfiguration.class));
    }

    @Test
    public void testBeanCreated() {
        this.contextRunner
                .withUserConfiguration(OAuth2ConfigTemplateBootStrapConfiguration.class)
                .withBean(ConfigClientOAuth2Properties.class, () -> {
                    ConfigClientOAuth2Properties properties = new ConfigClientOAuth2Properties();
                    properties.setClientId("clientId");
                    properties.setClientSecret("clientSecret");
                    properties.setAccessTokenUri("tokenUri");
                    return properties;
                })
                .withPropertyValues(
                        "spring.cloud.config.client.oauth2.client-id=test-client-id",
                        "spring.cloud.config.client.oauth2.client-secret=test-client-secret",
                        "spring.cloud.config.client.oauth2.access-token-uri=test-access-token-uri"
                )
                .run((context) -> {
                    assertThat(context).hasSingleBean(OAuth2ConfigTemplateBootStrapConfiguration.class);
                    assertThat(context).hasSingleBean(ConfigTemplate.class);
                });
    }
}
