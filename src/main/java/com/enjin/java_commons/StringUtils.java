package com.enjin.java_commons;

/**
 * <p>Operations on {@link String}.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will generally not be thrown for a {@code null} input.
 * Each method documents its behaviour in detail.</p>
 *
 * <p>#ThreadSafe#</p>
 *
 * @since 1.0
 */
public class StringUtils {

    /**
     * <p>{@code StringUtils} instances should NOT be constructed in standard programming.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public StringUtils() {
        super();
    }

    /**
     * <p>Checks if a string is empty or {@code null}.</p>
     *
     * @param str - str to check
     *
     * @return - boolean result
     *
     * @since 1.0
     */
    public static boolean isEmpty(final String str) {
        return ObjectUtils.isNull(str) || str.isEmpty();
    }

}
