package io.github.piszmog.cloudconfigclient.autoconfig.env;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.piszmog.cloudconfig.ConfigException;
import io.github.piszmog.cloudconfig.client.impl.FileConfigClient;
import io.github.piszmog.cloudconfigclient.autoconfig.env.model.Resource;
import io.github.piszmog.cloudconfigclient.autoconfig.env.support.MapFlattener;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Property source locator. Locates the JSON files to be loaded as property sources.
 * <p>
 * Created by Piszmog on 7/21/2018
 */
public class ConfigPropertySourceLocator
{
    private static final transient Log logger = LogFactory.getLog( ConfigPropertySourceLocator.class );

    // ============================================================
    // Class Constants:
    // ============================================================

    private static final String PROPERTY_SOURCE_NAME = "jsonResource";
    private static final String THREAD_NAME = "ConfigLocator";
    private static final ObjectMapper YAML_MAPPER = new ObjectMapper( new YAMLFactory() );
    private static final JavaPropsMapper PROPERTIES_MAPPER = new JavaPropsMapper();

    // ============================================================
    // Class Attributes:
    // ============================================================

    private final FileConfigClient fileConfigClient;
    private final ExecutorService executorService = Executors.newFixedThreadPool( 10,
            new BasicThreadFactory.Builder().namingPattern( THREAD_NAME + "-%d" )
                    .daemon( true )
                    .priority( Thread.MAX_PRIORITY )
                    .build() );

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
        //
        // Load files asynchronously
        //
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        resources.stream().filter( Objects::nonNull ).map( resource ->
                getJsonConfiguration( composite, resource.getDirectory(), resource.getFiles() ) ).forEach( futures::addAll );
        final CompletableFuture<List<Void>> listCompletableFuture = CompletableFuture.allOf( futures.toArray( new CompletableFuture[ 0 ] ) )
                .thenApply( future -> futures.stream().map( CompletableFuture::join ).collect( Collectors.toList() ) );
        try
        {
            //
            // Wait for files to complete -- don't want Spring to start setting up Beans without all property sources
            //
            listCompletableFuture.get();
            logger.info( "Successfully loaded all external configuration files." );
        }
        catch ( InterruptedException | ExecutionException e )
        {
            throw new ConfigResourceException( "Failed to retrieve files.", e );
        }
        finally
        {
            executorService.shutdownNow();
        }
        return composite;
    }

    // ============================================================
    // Private Methods:
    // ============================================================

    private List<CompletableFuture<Void>> getJsonConfiguration( final CompositePropertySource composite,
                                                                final String directoryPath,
                                                                final List<String> files )
    {
        return files.stream().filter( Objects::nonNull ).map( file -> CompletableFuture.supplyAsync( () ->
                loadConfigurationFile( directoryPath, file ), executorService ).thenAccept( mapPropertySource -> {
            if ( mapPropertySource != null )
            {
                composite.addPropertySource( mapPropertySource );
            }
        } ) ).collect( Collectors.toList() );
    }

    @SuppressWarnings( "unchecked" )
    private MapPropertySource loadConfigurationFile( final String directoryPath, final String fileName )
    {
        final String filePath = getFilePath( directoryPath, fileName );
        Map file;
        try
        {
            logger.info( "Loading configuration " + filePath + "..." );
            if ( StringUtils.endsWithIgnoreCase( fileName, ".yml" ) || StringUtils.endsWithIgnoreCase( fileName, ".yaml" ) )
            {
                file = getYAMLFile( directoryPath, fileName );
            }
            else if ( StringUtils.endsWithIgnoreCase( fileName, ".properties" ) )
            {
                file = getPropertiesFile( directoryPath, fileName );
            }
            else
            {
                file = getJSONFile( directoryPath, fileName );
            }
        }
        catch ( ConfigException e )
        {
            throw new ConfigResourceException( "Failed to load " + filePath, e );
        }
        if ( !file.isEmpty() )
        {
            final Map flattenMap = MapFlattener.flatten( file );
            return new MapPropertySource( filePath, flattenMap );
        }
        return null;
    }

    private String getFilePath( final String directoryPath, final String fileName )
    {
        return directoryPath + "/" + fileName;
    }

    private Map getYAMLFile( final String directoryPath, final String fileName ) throws ConfigException
    {
        final Map file;
        final byte[] yamlFile = fileConfigClient.getFileFromDefaultBranch( fileName, directoryPath, byte[].class );
        try
        {
            file = YAML_MAPPER.readValue( yamlFile, Map.class );
        }
        catch ( IOException e )
        {
            throw new ConfigResourceException( "Failed to convert " + fileName + " from YAML format to be loaded in the property sources.", e );
        }
        return file;
    }

    private Map getPropertiesFile( final String directoryPath, final String fileName ) throws ConfigException
    {
        final Map file;
        try
        {
            file = PROPERTIES_MAPPER.readValue( fileConfigClient.getFileFromDefaultBranch( fileName, directoryPath, byte[].class ), Map.class );
        }
        catch ( IOException e )
        {
            throw new ConfigResourceException( "Failed to convert " + fileName + " from PROPERTIES format to be loaded in the property sources.", e );
        }
        return file;
    }

    private Map getJSONFile( final String directoryPath, final String fileName ) throws ConfigException
    {
        return fileConfigClient.getFileFromDefaultBranch( fileName, directoryPath, Map.class );
    }
}
