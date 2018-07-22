package io.github.piszmog.cloudconfigclient.autoconfig.env;

import io.github.piszmog.cloudconfig.client.impl.FileConfigClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Piszmog on 7/21/2018
 */
@Configuration
public class ApplicationJsonConfig
{
    @Bean
    public ApplicationJsonLocator locator( final FileConfigClient fileConfigClient )
    {
        return new ApplicationJsonLocator( fileConfigClient );
    }
}
