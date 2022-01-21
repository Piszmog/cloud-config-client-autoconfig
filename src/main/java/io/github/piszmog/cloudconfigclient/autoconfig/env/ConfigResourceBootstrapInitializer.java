package io.github.piszmog.cloudconfigclient.autoconfig.env;

import io.github.piszmog.cloudconfigclient.autoconfig.env.model.Resource;
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
 * If enabled and a list of resources to load exist, then the configuration resources are added as property sources.
 * <p>
 * Only configuration file formats are accepted - .json, .yaml/.yml, and .properties
 * <p>
 * Only loads the configuration files if {@code cloud.config.client.file.enabled} is true or missing and {@link
 * ConfigPropertySourceLocator} bean was injected.
 * <p>
 * Currently, the max size of files to load is set to {@code 1000}. It cannot be determined ahead of time what the
 * actual size of the list is.
 * <p>
 * Created by Piszmog on 7/21/2018
 */
@Configuration
public class ConfigResourceBootstrapInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
    private static final String PROPERTY_FILE = "cloud.config.client.file";
    private static final String PROPERTY_ENABLED = PROPERTY_FILE + ".enabled";
    private static final int MAX_SIZE = 1000;

    private ConfigPropertySourceLocator configPropertySourceLocator;

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        final ConfigurableEnvironment environment = applicationContext.getEnvironment();
        final String isEnabled = environment.getProperty(PROPERTY_ENABLED);
        if ((StringUtils.isBlank(isEnabled) || BooleanUtils.toBoolean(isEnabled))
                && configPropertySourceLocator != null) {
            final List<Resource> resources = new ArrayList<>();
            for (int i = 0; i < MAX_SIZE; i++) {
                final List<String> files = new ArrayList<>();
                final String directory = environment.getProperty(PROPERTY_FILE + ".resources[" + i + "].directory");
                for (int j = 0; j < MAX_SIZE; j++) {
                    final String fileName = environment.getProperty(PROPERTY_FILE + ".resources[" + i + "].files[" + j + "]");
                    if (StringUtils.isBlank(fileName)) {
                        break;
                    }
                    if (StringUtils.isBlank(directory) && StringUtils.isNotBlank(fileName)) {
                        throw new ConfigResourceException("Directory must not be null or blank when file is not blank or null.");
                    }
                    if (!StringUtils.endsWithIgnoreCase(fileName, ".json")
                            && !StringUtils.endsWithIgnoreCase(fileName, ".yml")
                            && !StringUtils.endsWithIgnoreCase(fileName, ".yaml")
                            && !StringUtils.endsWithIgnoreCase(fileName, ".properties")) {
                        throw new ConfigResourceException("File " + fileName + " is not a configuration file." +
                                " Only .json, .yml, .yaml, and .properties are accepted.");
                    }
                    files.add(fileName);
                }
                if (StringUtils.isBlank(directory) && files.isEmpty()) {
                    break;
                }
                final Resource resource = new Resource();
                resource.setDirectory(directory);
                resource.setFiles(files);
                resources.add(resource);
            }
            if (!resources.isEmpty()) {
                final PropertySource<?> propertySource = configPropertySourceLocator.locateConfigResources(resources);
                environment.getPropertySources().addFirst(propertySource);
            }
        }
    }

    @Override
    public int getOrder() {
        //
        // Want to let all other initializers go first. Potentially the config server needs to be loaded first
        // before we can load the files.
        //
        return LOWEST_PRECEDENCE;
    }

    @Autowired(required = false)
    public void setConfigPropertySourceLocator(final ConfigPropertySourceLocator configPropertySourceLocator) {
        this.configPropertySourceLocator = configPropertySourceLocator;
    }
}
