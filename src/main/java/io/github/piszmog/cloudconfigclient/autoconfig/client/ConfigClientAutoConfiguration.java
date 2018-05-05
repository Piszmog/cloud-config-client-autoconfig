package io.github.piszmog.cloudconfigclient.autoconfig.client;

import io.github.piszmog.cloudconfig.client.ConfigClient;
import io.github.piszmog.cloudconfig.client.impl.DecryptConfigClient;
import io.github.piszmog.cloudconfig.client.impl.EncryptConfigClient;
import io.github.piszmog.cloudconfig.client.impl.FileConfigClient;
import io.github.piszmog.cloudconfig.client.impl.PublicKeyClient;
import io.github.piszmog.cloudconfig.template.ConfigTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configurations for creating config clients.
 * <p>
 * Created by Piszmog on 5/5/2018
 */
@Configuration
@ConditionalOnClass( ConfigClient.class )
@ConditionalOnBean( ConfigTemplate.class )
public class ConfigClientAutoConfiguration
{
    // ============================================================
    // Beans:
    // ============================================================

    /**
     * Create a decryption client.
     *
     * @param configTemplate the config template
     * @return The decryption client.
     */
    @Bean
    public DecryptConfigClient decryptConfigClient( final ConfigTemplate configTemplate )
    {
        return new DecryptConfigClient( configTemplate );
    }

    /**
     * Create a encryption client.
     *
     * @param configTemplate the config template
     * @return The encryption client.
     */
    @Bean
    public EncryptConfigClient encryptConfigClient( final ConfigTemplate configTemplate )
    {
        return new EncryptConfigClient( configTemplate );
    }

    /**
     * Create a file client.
     *
     * @param configTemplate the config template
     * @return The file client.
     */
    @Bean
    public FileConfigClient fileConfigClient( final ConfigTemplate configTemplate )
    {
        return new FileConfigClient( configTemplate );
    }

    /**
     * Create a public key client.
     *
     * @param configTemplate the config template
     * @return The public key client.
     */
    @Bean
    public PublicKeyClient publicKeyClient( final ConfigTemplate configTemplate )
    {
        return new PublicKeyClient( configTemplate );
    }
}
