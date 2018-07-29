package io.github.piszmog.cloudconfigclient.autoconfig.env.support


import spock.lang.Specification

/**
 * Created by Piszmog on 7/29/2018
 */
class MapFlattenerSpec extends Specification
{
    // ============================================================
    // Tests:
    // ============================================================

    def "flatten a simple map"()
    {
        given: "a map"
        Map topLevelMap = new HashMap()
        topLevelMap.put( "key1", "value1" )
        topLevelMap.put( "key2", "value2" )

        when: "the map is flattened"
        def flattenedMap = MapFlattener.flatten( topLevelMap )

        then: "the map is flattened"
        flattenedMap.get( "key1" ) == "value1"
        flattenedMap.get( "key2" ) == "value2"
    }

    def "flatten a nested map"()
    {
        given: "a map"
        Map level2Map = new HashMap()
        level2Map.put( "level2Key1", "value1" )
        level2Map.put( "level2Key2", "value2" )
        Map level1Map = new HashMap()
        level1Map.put( "level2", level2Map )
        Map topLevelMap = new HashMap()
        topLevelMap.put( "level1", level1Map )

        when: "the map is flattened"
        def flattenMap = MapFlattener.flatten( topLevelMap )

        then: "the map is flattened"
        flattenMap.get( "level1.level2.level2Key1" ) == "value1"
        flattenMap.get( "level1.level2.level2Key2" ) == "value2"
    }

    def "flatten a nested map with a list"()
    {
        given: "a map"
        List list = new LinkedList()
        list.add( "value1" )
        list.add( "value2" )
        Map level1Map = new HashMap()
        level1Map.put( "list", list )
        Map topLevelMap = new HashMap()
        topLevelMap.put( "level1", level1Map )

        when: "the map is flattened"
        def flattenMap = MapFlattener.flatten( topLevelMap )

        then: "the map is flattened"
        flattenMap.get( "level1.list[0]" ) == "value1"
        flattenMap.get( "level1.list[1]" ) == "value2"
    }

    def "flatten a nested map with special characters"()
    {
        given: "a map"
        Map level2Map = new HashMap()
        level2Map.put( "level2.Key1", "value1" )
        level2Map.put( "level2:Key2", "value2" )
        level2Map.put( "level2*Key3", "value3" )
        level2Map.put( "\$level2Key4", "value4" )
        level2Map.put( "#level2Key5", "value5" )
        level2Map.put( "%level2Key6", "value6" )
        level2Map.put( "!level2Key7", "value7" )
        level2Map.put( "^level2Key8", "value8" )
        level2Map.put( "@level2Key9", "value9" )
        level2Map.put( "&level2Key10", "value10" )
        Map level1Map = new HashMap()
        level1Map.put( "level2", level2Map )
        Map topLevelMap = new HashMap()
        topLevelMap.put( "level1", level1Map )

        when: "the map is flattened"
        def flattenMap = MapFlattener.flatten( topLevelMap )

        then: "the map is flattened"
        flattenMap.get( "level1.level2.[level2.Key1]" ) == "value1"
        flattenMap.get( "level1.level2.[level2:Key2]" ) == "value2"
        flattenMap.get( "level1.level2.[level2*Key3]" ) == "value3"
        flattenMap.get( "level1.level2.[\$level2Key4]" ) == "value4"
        flattenMap.get( "level1.level2.[#level2Key5]" ) == "value5"
        flattenMap.get( "level1.level2.%level2Key6" ) == "value6"
        flattenMap.get( "level1.level2.!level2Key7" ) == "value7"
        flattenMap.get( "level1.level2.^level2Key8" ) == "value8"
        flattenMap.get( "level1.level2.@level2Key9" ) == "value9"
        flattenMap.get( "level1.level2.&level2Key10" ) == "value10"
    }
}
