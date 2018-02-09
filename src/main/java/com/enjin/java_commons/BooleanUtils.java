package com.enjin.java_commons;

/**
 * <p>Operations on {@code boolean} and the {@link Boolean} wrapper.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will generally not be thrown for a {@code null} input.
 * Each method documents its behaviour in detail.</p>
 *
 * <p>#ThreadSafe#</p>
 *
 * @since 1.0
 */
public class BooleanUtils {

    /**
     * <p>{@code BooleanUtils} instances should NOT be constructed in standard programming.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public BooleanUtils() {
        super();
    }

    /**
     * <p>Checks if the given {@link Boolean} condition is {@code true}.</p>
     *
     * @param bool the condition to check
     *
     * @return {@code true} if {@code bool} is not null and is true.
     *
     * @since 1.0
     */
    public static boolean isTrue(final Boolean bool) {
        return !ObjectUtils.isNull(bool) && bool;
    }

    /**
     * <p>Checks if the given {@link Boolean} condition is {@code false}.</p>
     *
     * @param bool the condition to check
     *
     * @return {@code true} if {@code bool} is null or false.
     *
     * @since 1.0
     */
    public static boolean isFalse(final Boolean bool) {
        return !isTrue(bool);
    }

}
