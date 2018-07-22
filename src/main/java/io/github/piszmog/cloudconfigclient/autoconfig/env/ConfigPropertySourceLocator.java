package io.github.piszmog.cloudconfigclient.autoconfig.env;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import io.github.piszmog.cloudconfig.ConfigException;
import io.github.piszmog.cloudconfig.client.impl.FileConfigClient;
import io.github.piszmog.cloudconfigclient.autoconfig.env.model.Resource;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * JSON property source locator. Locates the JSON files to be loaded as property sources.
 * <p>
 * Created by Piszmog on 7/21/2018
 */
public class ConfigPropertySourceLocator
{
    // ============================================================
    // Class Constants:
    // ============================================================

    private static final String PROPERTY_SOURCE_NAME = "jsonResource";

    // ============================================================
    // Class Attributes:
    // ============================================================

    private final FileConfigClient fileConfigClient;
    private final JavaPropsMapper mapper = new JavaPropsMapper();

    // ============================================================
    // Constructors:
    // ============================================================

    /**
     * Creates a new instance of the locator.
     *
     * @param fileConfigClient the file config client to load files from the config server.
     */
    public ConfigPropertySourceLocator( final FileConfigClient fileConfigClient )
    {
        this.fileConfigClient = fileConfigClient;
    }

    // ============================================================
    // Public Methods:
    // ============================================================

    /**
     * Locates the JSON resources to load as property sources.
     *
     * @param resources the resources to load
     * @return The property source with the JSON resources loaded.
     */
    public PropertySource<?> locateConfigResources( final List<Resource> resources )
    {
        CompositePropertySource composite = new CompositePropertySource( PROPERTY_SOURCE_NAME );
        for ( Resource resource : resources )
        {
            final String directoryPath = resource.getDirectory();
            final List<String> files = resource.getFiles();
            getJsonConfiguration( composite, directoryPath, files );
        }
        return composite;
    }

    // ============================================================
    // Private Methods:
    // ============================================================

    private void getJsonConfiguration( final CompositePropertySource composite, final String directoryPath, final List<String> files )
    {
        final Map<String, Object> map = new HashMap<>();
        for ( String fileName : files )
        {
            final String filePath = getFilePath( directoryPath, fileName );
            try
            {
                final Map file = fileConfigClient.getFileFromDefaultBranch( fileName, directoryPath, Map.class );
                final Properties properties;
                try
                {
                    properties = mapper.writeValueAsProperties( file );
                }
                catch ( IOException e )
                {
                    throw new ConfigResourceException( "Failed to convert " + filePath + " to properties format to be loaded in the property sources." );
                }
                for ( Map.Entry<Object, Object> entry : properties.entrySet() )
                {
                    final String key = (String) entry.getKey();
                    final Object value = entry.getValue();
                    map.put( key, value );
                }
            }
            catch ( ConfigException e )
            {
                throw new ConfigResourceException( "Failed to load " + filePath, e );
            }
            if ( !map.isEmpty() )
            {
                composite.addPropertySource( new MapPropertySource( filePath, map ) );
            }
        }
    }

    private String getFilePath( final String directoryPath, final String fileName )
    {
        return directoryPath + "/" + fileName;
    }
}
