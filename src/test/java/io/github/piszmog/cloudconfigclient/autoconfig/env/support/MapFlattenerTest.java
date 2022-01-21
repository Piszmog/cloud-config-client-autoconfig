package io.github.piszmog.cloudconfigclient.autoconfig.env.support;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MapFlattener}.
 */
public class MapFlattenerTest {
    @Test
    public void testFlatten_singleMap() {
        Map<String, Object> topLevelMap = new HashMap<>();
        topLevelMap.put("key1", "value1");
        topLevelMap.put("key2", "value2");

        Map<String, Object> flatten = MapFlattener.flatten(topLevelMap);

        assertThat(flatten).containsEntry("key1", "value1");
        assertThat(flatten).containsEntry("key2", "value2");
    }

    @Test
    public void testFlatten_nested() {
        Map<String, Object> topLevelMap = new HashMap<>();
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("key1", "value1");
        nestedMap.put("key2", "value2");
        topLevelMap.put("key3", nestedMap);

        Map<String, Object> flatten = MapFlattener.flatten(topLevelMap);

        assertThat(flatten).containsEntry("key3.key1", "value1");
        assertThat(flatten).containsEntry("key3.key2", "value2");
    }

    @Test
    public void testFlatten_withList() {
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        Map<String, Object> level1Map = new HashMap<>();
        level1Map.put("list", list);
        Map<String, Object> topLevelMap = new HashMap<>();
        topLevelMap.put("level1", level1Map);

        Map<String, Object> flatten = MapFlattener.flatten(topLevelMap);

        assertThat(flatten).containsEntry("level1.list[0]", "value1");
        assertThat(flatten).containsEntry("level1.list[1]", "value2");
    }

    @Test
    public void testFlatten_specialCharacters() {
        Map<String, Object> level2Map = new HashMap<>();
        level2Map.put("level2.Key1", "value1");
        level2Map.put("level2:Key2", "value2");
        level2Map.put("level2*Key3", "value3");
        level2Map.put("\\$level2Key4", "value4");
        level2Map.put("#level2Key5", "value5");
        level2Map.put("%level2Key6", "value6");
        level2Map.put("!level2Key7", "value7");
        level2Map.put("^level2Key8", "value8");
        level2Map.put("@level2Key9", "value9");
        level2Map.put("&level2Key10", "value10");
        Map<String, Object> level1Map = new HashMap<>();
        level1Map.put("level2", level2Map);
        Map<String, Object> topLevelMap = new HashMap<>();
        topLevelMap.put("level1", level1Map);

        Map<String, Object> flattenMap = MapFlattener.flatten(topLevelMap);

        assertThat(flattenMap).containsEntry("level1.level2.[level2.Key1]", "value1");
        assertThat(flattenMap).containsEntry("level1.level2.[level2:Key2]", "value2");
        assertThat(flattenMap).containsEntry("level1.level2.[level2*Key3]", "value3");
        assertThat(flattenMap).containsEntry("level1.level2.\\$level2Key4", "value4");
        assertThat(flattenMap).containsEntry("level1.level2.[#level2Key5]", "value5");
        assertThat(flattenMap).containsEntry("level1.level2.%level2Key6", "value6");
        assertThat(flattenMap).containsEntry("level1.level2.!level2Key7", "value7");
        assertThat(flattenMap).containsEntry("level1.level2.^level2Key8", "value8");
        assertThat(flattenMap).containsEntry("level1.level2.@level2Key9", "value9");
        assertThat(flattenMap).containsEntry("level1.level2.&level2Key10", "value10");
    }
}
