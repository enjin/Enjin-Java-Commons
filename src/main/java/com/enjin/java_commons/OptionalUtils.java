package com.enjin.java_commons;

import java.util.Optional;

/**
 * <p>Operations on {@link Optional}.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will generally not be thrown for a {@code null} input.
 * Each method documents its behaviour in detail.</p>
 *
 * <p>#ThreadSafe#</p>
 *
 * @since 1.0
 */
public class OptionalUtils {

    /**
     * <p>{@code OptionalUtils} instances should NOT be constructed in standard programming.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public OptionalUtils() {
        super();
    }

    /**
     * <p>Checks if a value is present in an {@link Optional}.</p>
     *
     * @param optional the optional to test
     *
     * @return - boolean {@code true} if optional is not {@code null} and a value is present.
     *
     * @since 1.0
     */
    public static boolean isPresent(final Optional<?> optional) {
        return !ObjectUtils.isNull(optional) && optional.isPresent();
    }

}
