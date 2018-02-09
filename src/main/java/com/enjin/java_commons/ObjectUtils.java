package com.enjin.java_commons;

/**
 * <p>Operations on {@link Object}.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will generally not be thrown for a {@code null} input.
 * Each method documents its behaviour in detail.</p>
 *
 * <p>#ThreadSafe#</p>
 *
 * @since 1.0
 */
public class ObjectUtils {

    /**
     * <p>{@code ObjectUtils} instances should NOT be constructed in standard programming.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public ObjectUtils() {
        super();
    }

    /**
     * <p>Checks if an object is {@code null}.</p>
     *
     * <pre>
     *     ArrayUtils.isNull(null) = true
     *     ArrayUtils.isNull([])   = false
     * </pre>
     *
     * @param object the object to test
     *
     * @return {@code true} if the object is {@code null}
     *
     * @since 1.0
     */
    public static boolean isNull(final Object object) {
        return object == null;
    }

    /**
     * <p>Returns the {@link String} representation of the provided object or {@code null}
     * if the object is null</p>
     *
     * @param object the object to construct string representation of
     *
     * @return {@link String} representation of the object or {@code null} if the object is null.
     *
     * @since 1.0
     */
    public static String toString(final Object object) {
        return isNull(object) ? null : object.toString();
    }

}
