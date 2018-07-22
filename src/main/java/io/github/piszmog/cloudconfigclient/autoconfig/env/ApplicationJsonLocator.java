package io.github.piszmog.cloudconfigclient.autoconfig.env;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import io.github.piszmog.cloudconfig.ConfigException;
import io.github.piszmog.cloudconfig.client.impl.FileConfigClient;
import io.github.piszmog.cloudconfigclient.autoconfig.env.model.ApplicationFile;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Piszmog on 7/21/2018
 */
public class ApplicationJsonLocator
{
    private final JavaPropsMapper mapper = new JavaPropsMapper();
    private final FileConfigClient fileConfigClient;

    public ApplicationJsonLocator( final FileConfigClient fileConfigClient )
    {
        this.fileConfigClient = fileConfigClient;
    }

    public PropertySource<?> locate( final List<ApplicationFile> applicationFiles )
    {
        CompositePropertySource composite = new CompositePropertySource( "applicationJson" );
        for ( ApplicationFile applicationFile : applicationFiles )
        {
            final String directoryPath = applicationFile.getDirectoryPath();
            final String fileName = applicationFile.getFileName();
            getJsonConfiguration( composite, directoryPath, fileName );
        }
        return composite;
    }

    private void getJsonConfiguration( final CompositePropertySource composite, final String directoryPath, final String fileName )
    {
        final Map<String, Object> map = new HashMap<>();
        try
        {
            final Map file = fileConfigClient.getFileFromDefaultBranch( fileName, directoryPath, Map.class );
            final Properties properties = mapper.writeValueAsProperties( file );
            for ( Map.Entry<Object, Object> entry : properties.entrySet() )
            {
                final String key = (String) entry.getKey();
                final Object value = entry.getValue();
                map.put( key, value );
            }
        }
        catch ( ConfigException | IOException e )
        {
            throw new ApplicationJsonException( "Failed to load the application JSON configuration.", e );
        }
        if ( !map.isEmpty() )
        {
            composite.addPropertySource( new MapPropertySource( directoryPath + "/" + fileName, map ) );
        }
    }
}
