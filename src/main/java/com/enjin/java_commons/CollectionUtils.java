package com.enjin.java_commons;

import java.util.Collection;

/**
 * <p>Operations on {@link Collection}.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will generally not be thrown for a {@code null} input.
 * Each method documents its behaviour in detail.</p>
 *
 * @since 1.0
 */
public class CollectionUtils {

    /**
     * <p>{@code CollectionUtils} instances should NOT be constructed in standard programming.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public CollectionUtils() {
        super();
    }

    /**
     * <p>Checks if an {@link Collection} is empty.</p>
     *
     * @param collection the collection to test
     *
     * @return - boolean {@code true} if the collection is empty or {@code null}
     *
     * @since 1.0
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return ObjectUtils.isNull(collection) || collection.size() == 0;
    }

}
