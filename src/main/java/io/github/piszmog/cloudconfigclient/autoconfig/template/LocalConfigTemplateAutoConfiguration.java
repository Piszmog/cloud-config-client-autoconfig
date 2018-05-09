package io.github.piszmog.cloudconfigclient.autoconfig.template;

import io.github.piszmog.cloudconfig.template.ConfigTemplate;
import io.github.piszmog.cloudconfig.template.impl.LocalConfigTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for creating a {@link LocalConfigTemplate}.
 * <p>
 * Created by Piszmog on 5/5/2018
 */
@Configuration
@ConditionalOnBean( ConfigClientProperties.class )
@ConditionalOnMissingBean( type = "org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails" )
@ConditionalOnMissingClass( "org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails" )
public class LocalConfigTemplateAutoConfiguration
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
    @Bean
    @ConditionalOnMissingBean
    public ConfigTemplate localConfigTemplate( final ConfigClientProperties configClientProperties )
    {
        return new LocalConfigTemplate( configClientProperties );
    }
}
