package com.enjin.java_commons;

/**
 * <p>Operations on {@code Object}.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will generally not be thrown for a {@code null} input.
 * Each method documents its behaviour in detail.</p>
 *
 * <p>#ThreadSafe#</p>
 */
public class ObjectUtils {

    /**
     * <p>ObjectUtils instances should NOT be constructed in standard programming.
     *
     * </p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.
     */
    public ObjectUtils() {
        super();
    }

    /**
     * <p>Checks if an object {@code null}.</p>
     *
     * <pre>
     *     ArrayUtils.isNull(null) = true
     *     ArrayUtils.isNull([])   = false
     * </pre>
     *
     * @param object  the object to test
     * @return {@code true} if the object is {@code null}.
     * @since 1.0.0
     */
    public static boolean isNull(final Object object) {
        return object == null;
    }

}
