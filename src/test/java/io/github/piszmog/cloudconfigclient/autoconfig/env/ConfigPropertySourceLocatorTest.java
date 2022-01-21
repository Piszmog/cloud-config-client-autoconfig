package io.github.piszmog.cloudconfigclient.autoconfig.env;

import io.github.piszmog.cloudconfig.ConfigException;
import io.github.piszmog.cloudconfig.client.impl.FileConfigClient;
import io.github.piszmog.cloudconfigclient.autoconfig.env.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.CompositePropertySource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ConfigPropertySourceLocator}.
 */
@ExtendWith(value = MockitoExtension.class)
public class ConfigPropertySourceLocatorTest {
    private static final Map<String, Object> JSON_FILE_CONTENTS = Collections.singletonMap("field", "value");

    @Mock
    private FileConfigClient client;
    private ConfigPropertySourceLocator locator;

    @BeforeEach
    public void setUp() {
        locator = new ConfigPropertySourceLocator(client);
    }

    @Test
    public void testLocateConfigResources() throws ConfigException {
        Resource resource = createResource("directory", "file1.json");
        when(client.getFileFromDefaultBranch(anyString(), anyString(), any())).thenReturn(JSON_FILE_CONTENTS);

        CompositePropertySource propertySource = (CompositePropertySource) locator.locateConfigResources(Collections.singletonList(resource));

        assertThat(propertySource).isNotNull();
        assertFalse(propertySource.getPropertySources().isEmpty());
    }

    @Test
    public void testLocateConfigResources_multipleResourceFiles() throws ConfigException {
        Resource resource = createResource("directory", "file1.json", "file2.json", "file3.json");
        when(client.getFileFromDefaultBranch(anyString(), anyString(), any())).thenReturn(JSON_FILE_CONTENTS);

        CompositePropertySource propertySource = (CompositePropertySource) locator.locateConfigResources(Collections.singletonList(resource));

        assertThat(propertySource).isNotNull();
        assertFalse(propertySource.getPropertySources().isEmpty());
    }

    @Test
    public void testLocateConfigResources_multipleResources() throws ConfigException {
        List<Resource> resources = new ArrayList<>();
        Resource resource1 = createResource("directory", "file1.json");
        Resource resource2 = createResource("directory", "file2.json");
        Resource resource3 = createResource("directory", "file3.json");
        resources.add(resource1);
        resources.add(resource2);
        resources.add(resource3);
        when(client.getFileFromDefaultBranch(anyString(), anyString(), any())).thenReturn(JSON_FILE_CONTENTS);

        CompositePropertySource propertySource = (CompositePropertySource) locator.locateConfigResources(resources);

        assertThat(propertySource).isNotNull();
        assertFalse(propertySource.getPropertySources().isEmpty());
    }

    private static Resource createResource(final String directory, final String... files) {
        return new Resource(directory, Arrays.asList(files));
    }
}
