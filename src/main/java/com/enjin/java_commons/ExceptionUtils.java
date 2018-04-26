package com.enjin.java_commons;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>Operations on {@link Exception}.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will generally not be thrown for a {@code null} input.
 * Each method documents its behaviour in detail.</p>
 *
 * <p>#ThreadSafe#</p>
 *
 * @since 1.0
 */
public class ExceptionUtils {

    /**
     * <p>{@code ExceptionUtils} instances should NOT be constructed in standard programming.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public ExceptionUtils() {
        super();
    }

    /**
     * <p>Converts a throwable to a {@link String}.</p>
     *
     * @param throwable the throwable
     *
     * @return {@link String} representation of the throwable
     *
     * @since 1.0
     */
    public static String throwableToString(final Throwable throwable) {
        String stacktrace = null;
        if (throwable != null) {
            try (StringWriter sw = new StringWriter()) {
                try (PrintWriter pw = new PrintWriter(sw)) {
                    throwable.printStackTrace(pw);
                    stacktrace = sw.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stacktrace;
    }

}
