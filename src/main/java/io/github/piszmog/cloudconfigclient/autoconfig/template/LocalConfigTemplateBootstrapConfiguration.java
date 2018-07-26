package io.github.piszmog.cloudconfigclient.autoconfig.template;

import io.github.piszmog.cloudconfig.template.ConfigTemplate;
import io.github.piszmog.cloudconfig.template.impl.LocalConfigTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServiceBootstrapConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 * Auto-configuration for creating a {@link LocalConfigTemplate}.
 * <p>
 * Created by Piszmog on 5/5/2018
 */
@Order
@Configuration
@ConditionalOnProperty( value = "spring.cloud.config.client.oauth2.client-id", matchIfMissing = true )
@Import( ConfigServiceBootstrapConfiguration.class )
public class LocalConfigTemplateBootstrapConfiguration
{
    // ============================================================
    // Beans:
    // ============================================================

    /**
     * Creates a local config template bean.
     *
     * @param configClientProperties the config client properties.
     * @return The config template.
     */
    @Order
    @Bean
    @ConditionalOnMissingBean
    public ConfigTemplate localConfigTemplate( final ConfigClientProperties configClientProperties )
    {
        return new LocalConfigTemplate( configClientProperties );
    }
}
