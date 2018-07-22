package io.github.piszmog.cloudconfigclient.autoconfig.env;

import io.github.piszmog.cloudconfigclient.autoconfig.env.model.ApplicationFile;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piszmog on 7/21/2018
 */
@Configuration
public class ApplicationResourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>,
        Ordered
{
    private ApplicationJsonLocator applicationJsonLocator;

    @Autowired
    public void setApplicationJsonLocator( final ApplicationJsonLocator applicationJsonLocator )
    {
        this.applicationJsonLocator = applicationJsonLocator;
    }

    @Override
    public void initialize( final ConfigurableApplicationContext applicationContext )
    {
        final ConfigurableEnvironment environment = applicationContext.getEnvironment();
        final Boolean isEnabled = environment.getProperty( "cloud.config.resource.enabled", Boolean.class );
        if ( BooleanUtils.toBoolean( isEnabled ) )
        {
            final List<ApplicationFile> applicationFiles = new ArrayList<>();
            for ( int i = 0; i < 1000; i++ )
            {
                final String directory = environment.getProperty( "cloud.config.resource.files[" + i + "].directoryPath" );
                final String fileName = environment.getProperty( "cloud.config.resource.files[" + i + "].fileName" );
                if ( StringUtils.isBlank( directory ) || StringUtils.isBlank( fileName ) )
                {
                    break;
                }
                final ApplicationFile applicationFile = new ApplicationFile();
                applicationFile.setDirectoryPath( directory );
                applicationFile.setFileName( fileName );
                applicationFiles.add( applicationFile );
            }
            final PropertySource<?> propertySource = applicationJsonLocator.locate( applicationFiles );
            environment.getPropertySources().addFirst( propertySource );
        }
    }

    @Override
    public int getOrder()
    {
        return LOWEST_PRECEDENCE;
    }
}
