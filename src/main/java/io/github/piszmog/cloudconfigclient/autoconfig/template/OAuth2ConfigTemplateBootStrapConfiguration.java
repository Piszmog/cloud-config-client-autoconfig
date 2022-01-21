package io.github.piszmog.cloudconfigclient.autoconfig.template;

import io.github.piszmog.cloudconfig.template.ConfigTemplate;
import io.github.piszmog.cloudconfig.template.impl.OAuth2ConfigTemplate;
import io.pivotal.spring.cloud.config.client.ConfigClientOAuth2Properties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServiceBootstrapConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 * Auto-configuration for creating a {@link OAuth2ConfigTemplate}.
 * <p>
 * Created by Piszmog on 5/5/2018
 */
@Order
@Configuration
@ConditionalOnClass(ConfigClientOAuth2Properties.class)
@ConditionalOnProperty(prefix = "spring.cloud.config.client.oauth2", name = {"client-id", "client-secret", "access-token-uri"})
@Import({ConfigServiceBootstrapConfiguration.class})
public class OAuth2ConfigTemplateBootStrapConfiguration {
    /**
     * Creates a config template using OAuth2 credentials to connect to the config server.
     *
     * @param configClientProperties       the config client properties
     * @param configClientOAuth2Properties the OAuth2 details
     * @return The config template.
     */
    @Order
    @Bean
    @ConditionalOnMissingBean
    public ConfigTemplate oauth2ConfigTemplate(final ConfigClientProperties configClientProperties,
                                               final ConfigClientOAuth2Properties configClientOAuth2Properties) {
        return new OAuth2ConfigTemplate(configClientProperties, configClientOAuth2Properties);
    }
}
