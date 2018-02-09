package com.enjin.java_commons;

import java.util.Map;

/**
 * <p>Operations on {@link Map}.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will generally not be thrown for a {@code null} input.
 * Each method documents its behaviour in detail.</p>
 *
 * @since 1.0
 */
public class MapUtils {

    /**
     * <p>{@code MapUtils} instances should NOT be constructed in standard programming.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public MapUtils() {
        super();
    }

    /**
     * <p>Checks if an {@link Map} is empty.</p>
     *
     * @param map the map to test
     *
     * @return - boolean {@code true} if the collection is empty or {@code null}
     *
     * @since 1.0
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return ObjectUtils.isNull(map) || map.size() == 0;
    }

    /**
     * <p>Returns the {@link String} representation of the value associated with the
     * specified key in the given {@link Map} or {@code null} if key does not
     * exist in the map.</p>
     *
     * @param map the map to fetch the value of the given key
     * @param key the key to search for its associated value
     *
     * @return {@code String} representation of the associate value of the key or {@code null}
     * if no key exists in the given map
     *
     * @since 1.0
     */
    public static String mapKeyValueToString(final Map<?, ?> map, final String key) {
        return ObjectUtils.toString(map.get(key));
    }

    /**
     * <p>Returns the {@link String} representation of the value of a given {@link Map.Entry}
     * or {@code null} if the entry or value are null.</p>
     *
     * @param entry the entry to generate the value {@link String} representation
     *
     * @return {@code String} representation of the value of the given entry
     *
     * @since 1.0
     */
    public static String entryToString(final Map.Entry<?, ?> entry) {
        String value = null;

        if (!ObjectUtils.isNull(entry))
            value = ObjectUtils.toString(entry.getValue());

        return value;
    }

}
