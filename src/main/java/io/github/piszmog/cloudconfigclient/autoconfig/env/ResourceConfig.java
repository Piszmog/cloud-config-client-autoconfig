package io.github.piszmog.cloudconfigclient.autoconfig.env;

import io.github.piszmog.cloudconfig.client.impl.FileConfigClient;
import io.github.piszmog.cloudconfig.template.ConfigTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Configuration for creating configuration resource beans.
 * <p>
 * Created via {@link org.springframework.cloud.bootstrap.BootstrapConfiguration} so property sources can be loaded
 * before Spring configuration properties are set up.
 * <p>
 * Created by Piszmog on 7/21/2018
 */
@Order
@Configuration
public class ResourceConfig
{
    /**
     * Create a file client. To disable, the property {@code cloud.config.client.file.enabled} must be set via bootstrap
     * configuration.
     *
     * @param configTemplate the config template
     * @return The file client.
     */
    @Bean
    @ConditionalOnProperty( prefix = "cloud.config.client", name = "file.enabled", matchIfMissing = true )
    public FileConfigClient fileConfigClient( final ConfigTemplate configTemplate )
    {
        return new FileConfigClient( configTemplate );
    }

    /**
     * Creates a Configuration PropertySource Locator to finding config files to add to property sources.
     *
     * @param fileConfigClient the file config client to load files from the Config Server
     * @return The locator.
     */
    @Bean
    public ConfigPropertySourceLocator configPropertySourceLocator( final FileConfigClient fileConfigClient )
    {
        return new ConfigPropertySourceLocator( fileConfigClient );
    }
}
