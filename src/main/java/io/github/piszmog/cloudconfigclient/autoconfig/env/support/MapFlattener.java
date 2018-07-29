package io.github.piszmog.cloudconfigclient.autoconfig.env.support;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Flattens maps into a Spring readable properties map. See https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-Configuration-Binding
 * for more information on Spring key names.
 * <p>
 * Copied from {@link org.springframework.boot.env.SpringApplicationJsonEnvironmentPostProcessor}.
 * <p>
 * Created by Piszmog on 7/25/2018
 */
public class MapFlattener
{
    // ============================================================
    // Static Methods:
    // ============================================================

    /**
     * Flattens the provided map.
     *
     * @param map the map to flatten
     * @return The flatten map with the keys in the Spring properties format.
     */
    public static Map<String, Object> flatten( Map<String, Object> map )
    {
        Map<String, Object> result = new LinkedHashMap<>();
        flatten( null, result, map );
        return result;
    }

    // ============================================================
    // Private Methods:
    // ============================================================

    private static void flatten( String prefix,
                                 Map<String, Object> result,
                                 Map<String, Object> map )
    {
        String namePrefix = ( prefix != null ? prefix + "." : "" );
        map.forEach( ( key, value ) -> {
            String finalKey = key;
            //
            // If key has certain characters in it, we want to wrap key in '[' and ']'.
            //
            if ( StringUtils.contains( key, ":" )
                    || StringUtils.contains( key, "." )
                    || StringUtils.contains( key, "*" )
                    || StringUtils.startsWith( key, "$" )
                    || StringUtils.startsWith( key, "#" ) )
            {
                finalKey = "[" + key + "]";
            }
            extract( namePrefix + finalKey, result, value );
        } );
    }

    @SuppressWarnings( "unchecked" )
    private static void extract( String name, Map<String, Object> result, Object value )
    {
        if ( value instanceof Map )
        {
            flatten( name, result, (Map<String, Object>) value );
        }
        else if ( value instanceof Collection )
        {
            int index = 0;
            for ( Object object : (Collection<Object>) value )
            {
                extract( name + "[" + index + "]", result, object );
                index++;
            }
        }
        else
        {
            result.put( name, value );
        }
    }
}
