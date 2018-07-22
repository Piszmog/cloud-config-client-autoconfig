package io.github.piszmog.cloudconfigclient.autoconfig.env

import io.github.piszmog.cloudconfig.client.impl.FileConfigClient
import io.github.piszmog.cloudconfigclient.autoconfig.env.model.Resource
import spock.lang.Specification

/**
 * Created by Piszmog on 7/22/2018
 */
class ConfigPropertySourceLocatorSpec extends Specification
{
    // ============================================================
    // Class Constants:
    // ============================================================

    private static final def JSON_FILE_CONTENTS = [ field: "value" ]

    // ============================================================
    // Class Attributes:
    // ============================================================

    private FileConfigClient fileConfigClient

    // ============================================================
    // Setup:
    // ============================================================

    def setup()
    {
        fileConfigClient = Stub( FileConfigClient )
    }

    // ============================================================
    // Tests:
    // ============================================================

    def "load a resource"()
    {
        given: "a configuration source locator"
        def locator = new ConfigPropertySourceLocator( fileConfigClient )

        and: "a resource to load with 1 file"
        def resource = createResource( "directory", "file1.json" )

        and: "files are found"
        fileConfigClient.getFileFromDefaultBranch( _, _, _ ) >> JSON_FILE_CONTENTS

        when: "the locator looks for the resource"
        def propertySource = locator.locateConfigResources( Collections.singletonList( resource ) )

        then: "the file is converter to a properties object"
        notThrown( ConfigResourceException )

        and: "the property source contains the resource"
        !propertySource.getProperties().isEmpty()
    }

    def "load multiple resource files"()
    {
        given: "a configuration source locator"
        def locator = new ConfigPropertySourceLocator( fileConfigClient )

        and: "a resource to load with multiple files"
        def resource = createResource( "directory", "file1.json", "file2.json", "file3.json" )

        and: "a file is found"
        fileConfigClient.getFileFromDefaultBranch( _, _, _ ) >> JSON_FILE_CONTENTS

        when: "the locator looks for the resource"
        def propertySource = locator.locateConfigResources( Collections.singletonList( resource ) )

        then: "the file is converter to a properties object"
        notThrown( ConfigResourceException )

        and: "the property source contains the resource"
        !propertySource.getProperties().isEmpty()
    }

    def "load multiple resources"()
    {
        given: "a configuration source locator"
        def locator = new ConfigPropertySourceLocator( fileConfigClient )

        and: "resources"
        List resources = new ArrayList()

        and: "resource1 to load with 1 file"
        def resource1 = createResource( "directory1", "file1.json" )
        resources.add( resource1 )

        and: "resource2 to load with 1 file"
        def resource2 = createResource( "directory2", "file2.json" )
        resources.add( resource2 )

        and: "resource3 to load with 1 file"
        def resource3 = createResource( "directory3", "file3.json" )
        resources.add( resource3 )

        and: "files are found"
        fileConfigClient.getFileFromDefaultBranch( _, _, _ ) >> JSON_FILE_CONTENTS

        when: "the locator looks for the resource"
        def propertySource = locator.locateConfigResources( resources )

        then: "the file is converter to a properties object"
        notThrown( ConfigResourceException )

        and: "the property source contains the resource"
        !propertySource.getProperties().isEmpty()
    }

    // ============================================================
    // Private Methods:
    // ============================================================

    private static Resource createResource( final String directory, final String... files )
    {
        def resource = new Resource()
        resource.setDirectory( directory )
        resource.setFiles( files as List )
        return resource
    }
}
