package io.github.piszmog.cloudconfigclient.autoconfig.template;

import io.github.piszmog.cloudconfig.template.ConfigTemplate;
import io.github.piszmog.cloudconfig.template.impl.OAuth2ConfigTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * Auto-configuration for creating a {@link OAuth2ConfigTemplate}.
 * <p>
 * Created by Piszmog on 5/5/2018
 */
@Configuration
@ConditionalOnClass( OAuth2ProtectedResourceDetails.class )
@ConditionalOnBean( { ConfigClientProperties.class, OAuth2ProtectedResourceDetails.class } )
public class OAuth2ConfigTemplateAutoConfiguration
{
    // ============================================================
    // Beans:
    // ============================================================

    /**
     * Creates a config template using OAuth2 credentials to connect to the config server.
     *
     * @param configClientProperties         the config client properties
     * @param oAuth2ProtectedResourceDetails the OAuth2 details
     * @return The config template.
     */
    @Bean
    @ConditionalOnMissingBean
    public ConfigTemplate oauth2ConfigTemplate( final ConfigClientProperties configClientProperties,
                                                final OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails )
    {
        return new OAuth2ConfigTemplate( configClientProperties, oAuth2ProtectedResourceDetails );
    }
}
