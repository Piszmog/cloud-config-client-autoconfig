package io.github.piszmog.cloudconfigclient.autoconfig.client;

import io.github.piszmog.cloudconfig.client.impl.DecryptConfigClient;
import io.github.piszmog.cloudconfig.client.impl.EncryptConfigClient;
import io.github.piszmog.cloudconfig.client.impl.PublicKeyClient;
import io.github.piszmog.cloudconfig.template.ConfigTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configurations for creating config clients.
 * <p>
 * {@link io.github.piszmog.cloudconfig.client.impl.FileConfigClient} is required for loading JSON files from the Config
 * Server. To add the JSON files as property sources, the client must be created before Spring has refreshed the
 * context.
 * <p>
 * Created by Piszmog on 5/5/2018
 */
@Configuration
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
    @ConditionalOnProperty( prefix = "cloud.config.client", name = "decrypt.enabled", matchIfMissing = true )
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
    @ConditionalOnProperty( prefix = "cloud.config.client", name = "encrypt.enabled", matchIfMissing = true )
    public EncryptConfigClient encryptConfigClient( final ConfigTemplate configTemplate )
    {
        return new EncryptConfigClient( configTemplate );
    }

    /**
     * Create a public key client.
     *
     * @param configTemplate the config template
     * @return The public key client.
     */
    @Bean
    @ConditionalOnProperty( prefix = "cloud.config.client", name = "publickey.enabled", matchIfMissing = true )
    public PublicKeyClient publicKeyClient( final ConfigTemplate configTemplate )
    {
        return new PublicKeyClient( configTemplate );
    }
}
