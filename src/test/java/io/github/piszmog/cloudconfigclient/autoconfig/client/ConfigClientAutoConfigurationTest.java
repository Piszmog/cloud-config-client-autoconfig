package io.github.piszmog.cloudconfigclient.autoconfig.client;

import io.github.piszmog.cloudconfig.client.impl.DecryptConfigClient;
import io.github.piszmog.cloudconfig.client.impl.EncryptConfigClient;
import io.github.piszmog.cloudconfig.client.impl.PublicKeyClient;
import io.github.piszmog.cloudconfig.template.ConfigTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link ConfigClientAutoConfiguration}.
 */
public class ConfigClientAutoConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ConfigClientAutoConfiguration.class));

    @Test
    public void testAutoConfigurationNotCreated() {
        this.contextRunner
                .withUserConfiguration(ConfigClientAutoConfiguration.class)
                .run((context) -> assertThat(context).doesNotHaveBean(ConfigClientAutoConfiguration.class));
    }

    @Test
    public void testAutoConfigurationCreated() {
        this.contextRunner
                .withUserConfiguration(ConfigClientAutoConfiguration.class)
                .withBean(ConfigTemplate.class, () -> mock(ConfigTemplate.class))
                .run((context) -> {
                    assertThat(context).hasSingleBean(ConfigClientAutoConfiguration.class);
                    assertThat(context).hasSingleBean(DecryptConfigClient.class);
                    assertThat(context).hasSingleBean(EncryptConfigClient.class);
                    assertThat(context).hasSingleBean(PublicKeyClient.class);
                });
    }

    @Test
    public void testBeansEnabled() {
        this.contextRunner
                .withUserConfiguration(ConfigClientAutoConfiguration.class)
                .withBean(ConfigTemplate.class, () -> mock(ConfigTemplate.class))
                .withPropertyValues(
                        "cloud.config.client.decrypt.enabled=true",
                        "cloud.config.client.encrypt.enabled=true",
                        "cloud.config.client.publickey.enabled=true"
                )
                .run((context) -> {
                    assertThat(context).hasSingleBean(ConfigClientAutoConfiguration.class);
                    assertThat(context).hasSingleBean(DecryptConfigClient.class);
                    assertThat(context).hasSingleBean(EncryptConfigClient.class);
                    assertThat(context).hasSingleBean(PublicKeyClient.class);
                });
    }

    @Test
    public void testBeansDisabled() {
        this.contextRunner
                .withUserConfiguration(ConfigClientAutoConfiguration.class)
                .withBean(ConfigTemplate.class, () -> mock(ConfigTemplate.class))
                .withPropertyValues(
                        "cloud.config.client.decrypt.enabled=false",
                        "cloud.config.client.encrypt.enabled=false",
                        "cloud.config.client.publickey.enabled=false"
                )
                .run((context) -> {
                    assertThat(context).hasSingleBean(ConfigClientAutoConfiguration.class);
                    assertThat(context).doesNotHaveBean(DecryptConfigClient.class);
                    assertThat(context).doesNotHaveBean(EncryptConfigClient.class);
                    assertThat(context).doesNotHaveBean(PublicKeyClient.class);
                });
    }
}
