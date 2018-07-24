package io.github.piszmog.cloudconfigclient.autoconfig.env

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.CompositePropertySource
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MutablePropertySources
import spock.lang.Specification

/**
 * Created by Piszmog on 7/22/2018
 */
class ConfigResourceInitializerSpec extends Specification
{
    // ============================================================
    // Class Attributes:
    // ============================================================

    private ConfigPropertySourceLocator configPropertySourceLocator
    private ConfigurableApplicationContext configurableApplicationContext

    // ============================================================
    // Setup:
    // ============================================================

    def setup()
    {
        configPropertySourceLocator = Stub( ConfigPropertySourceLocator )
        configurableApplicationContext = Stub( ConfigurableApplicationContext )
    }

    // ============================================================
    // TestS:
    // ============================================================

    def "configuration initializer adds json"()
    {
        given: "A configuration initializer"
        def initializer = new ConfigResourceInitializer()

        and: "the locator is set"
        initializer.setConfigPropertySourceLocator( configPropertySourceLocator )

        and: "the context has an environment"
        def environment = Stub( ConfigurableEnvironment )
        configurableApplicationContext.getEnvironment() >> environment

        and: "environment has property sources"
        def sources = new MutablePropertySources()
        environment.getPropertySources(  ) >> sources

        and: "the environment does not find the enabled flag"
        environment.getProperty( "cloud.config.client.file.enabled" ) >> null

        and: "directories and files are found"
        environment.getProperty( "cloud.config.client.file.resources[0].directory" ) >> "directory"
        environment.getProperty( "cloud.config.client.file.resources[0].files[0]" ) >> "file1.json"

        and: "and property sources are found"
        configPropertySourceLocator.locateConfigResources( _ ) >> new CompositePropertySource( "example" )

        when: "the initializer is ran"
        initializer.initialize( configurableApplicationContext )

        then: "the locator's property source is in the environment's property sources"
        sources.contains( "example" )
    }

    def "configuration initializer adds yml"()
    {
        given: "A configuration initializer"
        def initializer = new ConfigResourceInitializer()

        and: "the locator is set"
        initializer.setConfigPropertySourceLocator( configPropertySourceLocator )

        and: "the context has an environment"
        def environment = Stub( ConfigurableEnvironment )
        configurableApplicationContext.getEnvironment() >> environment

        and: "environment has property sources"
        def sources = new MutablePropertySources()
        environment.getPropertySources(  ) >> sources

        and: "the environment does not find the enabled flag"
        environment.getProperty( "cloud.config.client.file.enabled" ) >> null

        and: "directories and files are found"
        environment.getProperty( "cloud.config.client.file.resources[0].directory" ) >> "directory"
        environment.getProperty( "cloud.config.client.file.resources[0].files[0]" ) >> "file1.yml"

        and: "and property sources are found"
        configPropertySourceLocator.locateConfigResources( _ ) >> new CompositePropertySource( "example" )

        when: "the initializer is ran"
        initializer.initialize( configurableApplicationContext )

        then: "yml files are allowed to be added"
        notThrown( ConfigResourceException )
    }

    def "configuration initializer adds yaml"()
    {
        given: "A configuration initializer"
        def initializer = new ConfigResourceInitializer()

        and: "the locator is set"
        initializer.setConfigPropertySourceLocator( configPropertySourceLocator )

        and: "the context has an environment"
        def environment = Stub( ConfigurableEnvironment )
        configurableApplicationContext.getEnvironment() >> environment

        and: "environment has property sources"
        def sources = new MutablePropertySources()
        environment.getPropertySources(  ) >> sources

        and: "the environment does not find the enabled flag"
        environment.getProperty( "cloud.config.client.file.enabled" ) >> null

        and: "directories and files are found"
        environment.getProperty( "cloud.config.client.file.resources[0].directory" ) >> "directory"
        environment.getProperty( "cloud.config.client.file.resources[0].files[0]" ) >> "file1.yaml"

        and: "and property sources are found"
        configPropertySourceLocator.locateConfigResources( _ ) >> new CompositePropertySource( "example" )

        when: "the initializer is ran"
        initializer.initialize( configurableApplicationContext )

        then: "yaml files are allowed to be added"
        notThrown( ConfigResourceException )
    }

    def "configuration initializer adds properties"()
    {
        given: "A configuration initializer"
        def initializer = new ConfigResourceInitializer()

        and: "the locator is set"
        initializer.setConfigPropertySourceLocator( configPropertySourceLocator )

        and: "the context has an environment"
        def environment = Stub( ConfigurableEnvironment )
        configurableApplicationContext.getEnvironment() >> environment

        and: "environment has property sources"
        def sources = new MutablePropertySources()
        environment.getPropertySources(  ) >> sources

        and: "the environment does not find the enabled flag"
        environment.getProperty( "cloud.config.client.file.enabled" ) >> null

        and: "directories and files are found"
        environment.getProperty( "cloud.config.client.file.resources[0].directory" ) >> "directory"
        environment.getProperty( "cloud.config.client.file.resources[0].files[0]" ) >> "file1.properties"

        and: "and property sources are found"
        configPropertySourceLocator.locateConfigResources( _ ) >> new CompositePropertySource( "example" )

        when: "the initializer is ran"
        initializer.initialize( configurableApplicationContext )

        then: "properties files are allowed to be added"
        notThrown( ConfigResourceException )
    }

    def "configuration initializer is provided a text file to add"()
    {
        given: "A configuration initializer"
        def initializer = new ConfigResourceInitializer()

        and: "the locator is set"
        initializer.setConfigPropertySourceLocator( configPropertySourceLocator )

        and: "the context has an environment"
        def environment = Stub( ConfigurableEnvironment )
        configurableApplicationContext.getEnvironment() >> environment

        and: "environment has property sources"
        def sources = new MutablePropertySources()
        environment.getPropertySources(  ) >> sources

        and: "the environment does not find the enabled flag"
        environment.getProperty( "cloud.config.client.file.enabled" ) >> null

        and: "directories and files are found"
        environment.getProperty( "cloud.config.client.file.resources[0].directory" ) >> "directory"
        environment.getProperty( "cloud.config.client.file.resources[0].files[0]" ) >> "file1.txt"

        when: "the initializer is ran"
        initializer.initialize( configurableApplicationContext )

        then: "text files are not allowed to be added"
        thrown( ConfigResourceException )
    }

    def "configuration initializer has a directory as blank"()
    {
        given: "A configuration initializer"
        def initializer = new ConfigResourceInitializer()

        and: "the locator is set"
        initializer.setConfigPropertySourceLocator( configPropertySourceLocator )

        and: "the context has an environment"
        def environment = Stub( ConfigurableEnvironment )
        configurableApplicationContext.getEnvironment() >> environment

        and: "environment has property sources"
        def sources = new MutablePropertySources()
        environment.getPropertySources(  ) >> sources

        and: "the environment does not find the enabled flag"
        environment.getProperty( "cloud.config.client.file.enabled" ) >> null

        and: "directories and files are found"
        environment.getProperty( "cloud.config.client.file.resources[0].directory" ) >> ""
        environment.getProperty( "cloud.config.client.file.resources[0].files[0]" ) >> "file1.json"

        when: "the initializer is ran"
        initializer.initialize( configurableApplicationContext )

        then: "the directory cannot be blank when a file is provided"
        thrown( ConfigResourceException )
    }

    def "configuration initializer has a directory as null"()
    {
        given: "A configuration initializer"
        def initializer = new ConfigResourceInitializer()

        and: "the locator is set"
        initializer.setConfigPropertySourceLocator( configPropertySourceLocator )

        and: "the context has an environment"
        def environment = Stub( ConfigurableEnvironment )
        configurableApplicationContext.getEnvironment() >> environment

        and: "environment has property sources"
        def sources = new MutablePropertySources()
        environment.getPropertySources(  ) >> sources

        and: "the environment does not find the enabled flag"
        environment.getProperty( "cloud.config.client.file.enabled" ) >> null

        and: "directories and files are found"
        environment.getProperty( "cloud.config.client.file.resources[0].directory" ) >> null
        environment.getProperty( "cloud.config.client.file.resources[0].files[0]" ) >> "file1.json"

        when: "the initializer is ran"
        initializer.initialize( configurableApplicationContext )

        then: "the directory cannot be null when a file is provided"
        thrown( ConfigResourceException )
    }

    def "configuration initializer adds many json files"()
    {
        given: "A configuration initializer"
        def initializer = new ConfigResourceInitializer()

        and: "the locator is set"
        initializer.setConfigPropertySourceLocator( configPropertySourceLocator )

        and: "the context has an environment"
        def environment = Stub( ConfigurableEnvironment )
        configurableApplicationContext.getEnvironment() >> environment

        and: "environment has property sources"
        def sources = new MutablePropertySources()
        environment.getPropertySources(  ) >> sources

        and: "the environment does not find the enabled flag"
        environment.getProperty( "cloud.config.client.file.enabled" ) >> null

        and: "directories and files are found"
        environment.getProperty( "cloud.config.client.file.resources[0].directory" ) >> "directory"
        environment.getProperty( "cloud.config.client.file.resources[0].files[0]" ) >> "file1.json"
        environment.getProperty( "cloud.config.client.file.resources[0].files[1]" ) >> "file2.json"
        environment.getProperty( "cloud.config.client.file.resources[0].files[2]" ) >> "file3.json"

        and: "and property sources are found"
        configPropertySourceLocator.locateConfigResources( _ ) >> new CompositePropertySource( "example" )

        when: "the initializer is ran"
        initializer.initialize( configurableApplicationContext )

        then: "the locator's property source is in the environment's property sources"
        sources.contains( "example" )
    }

    def "configuration initializer adds many json files in many directories"()
    {
        given: "A configuration initializer"
        def initializer = new ConfigResourceInitializer()

        and: "the locator is set"
        initializer.setConfigPropertySourceLocator( configPropertySourceLocator )

        and: "the context has an environment"
        def environment = Stub( ConfigurableEnvironment )
        configurableApplicationContext.getEnvironment() >> environment

        and: "environment has property sources"
        def sources = new MutablePropertySources()
        environment.getPropertySources(  ) >> sources

        and: "the environment does not find the enabled flag"
        environment.getProperty( "cloud.config.client.file.enabled" ) >> null

        and: "directories and files are found"
        environment.getProperty( "cloud.config.client.file.resources[0].directory" ) >> "directory1"
        environment.getProperty( "cloud.config.client.file.resources[0].files[0]" ) >> "file1.json"
        environment.getProperty( "cloud.config.client.file.resources[0].files[1]" ) >> "file2.json"
        environment.getProperty( "cloud.config.client.file.resources[0].files[2]" ) >> "file3.json"
        environment.getProperty( "cloud.config.client.file.resources[1].directory" ) >> "directory2"
        environment.getProperty( "cloud.config.client.file.resources[1].files[0]" ) >> "file4.json"
        environment.getProperty( "cloud.config.client.file.resources[1].files[1]" ) >> "file5.json"
        environment.getProperty( "cloud.config.client.file.resources[1].files[2]" ) >> "file6.json"
        environment.getProperty( "cloud.config.client.file.resources[2].directory" ) >> "directory3"
        environment.getProperty( "cloud.config.client.file.resources[2].files[0]" ) >> "file7.json"
        environment.getProperty( "cloud.config.client.file.resources[2].files[1]" ) >> "file8.json"
        environment.getProperty( "cloud.config.client.file.resources[2].files[2]" ) >> "file9.json"

        and: "and property sources are found"
        configPropertySourceLocator.locateConfigResources( _ ) >> new CompositePropertySource( "example" )

        when: "the initializer is ran"
        initializer.initialize( configurableApplicationContext )

        then: "the locator's property source is in the environment's property sources"
        sources.contains( "example" )
    }
}
