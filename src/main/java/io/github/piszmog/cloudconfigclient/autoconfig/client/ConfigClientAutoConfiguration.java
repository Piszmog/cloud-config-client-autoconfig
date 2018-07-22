package io.github.piszmog.cloudconfigclient.autoconfig.client;

import io.github.piszmog.cloudconfig.client.impl.DecryptConfigClient;
import io.github.piszmog.cloudconfig.client.impl.EncryptConfigClient;
import io.github.piszmog.cloudconfig.client.impl.FileConfigClient;
import io.github.piszmog.cloudconfig.client.impl.PublicKeyClient;
import io.github.piszmog.cloudconfig.template.ConfigTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Auto-configurations for creating config clients.
 * <p>
 * Created by Piszmog on 5/5/2018
 */
@Configuration
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
     * Create a file client.
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
