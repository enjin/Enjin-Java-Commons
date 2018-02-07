package com.enjin.java_commons;

import java.lang.reflect.Array;

/**
 * <p>Operations on arrays, primitive arrays (like {@code int[]}) and
 * primitive wrapper arrays (like {@code Integer[]}).</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will not be thrown for a {@code null}
 * array input. However, an Object array that contains a {@code null}
 * element may throw an exception. Each method documents its behaviour in detail.</p>
 *
 * <p>#ThreadSafe#</p>
 */
public class ArrayUtils {

    /**
     * <p>ArrayUtils instances should NOT be constructed in standard programming.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public ArrayUtils() {
        super();
    }

    /**
     * <p>Checks if an array of Objects is empty or {@code null}.</p>
     *
     * <pre>
     *     ArrayUtils.isEmpty(null)  = true
     *     ArrayUtils.isEmpty([])    = true
     *     ArrayUtils.isEmpty(["a"]) = false
     * </pre>
     *
     * @param array  the array to test
     * @return {@code true} if the array is empty or {@code null}.
     * @since 1.0.0
     */
    public static boolean isEmpty(final Object[] array) {
        return getLength(array) == 0;
    }

    /**
     * <p>Checks if an array of Objects is not empty.</p>
     *
     * <pre>
     *     ArrayUtils.isNotEmpty(null)  = false
     *     ArrayUtils.isNotEmpty([])    = false
     *     ArrayUtils.isNotEmpty(["a"]) = true
     * </pre>
     *
     * @param array  the array to test
     * @return {@code true} if the array is not empty.
     * @since 1.0.0
     */
    public static boolean isNotEmpty(final Object[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>Returns the length of the provided array.
     * This method can deal with {@code Object} arrays and with primitive arrays.</p>
     *
     * <p>If the input array is {@code null}, {@code 0} is returned.</p>
     *
     * <pre>
     *     ArrayUtils.getLength(null)            = 0
     *     ArrayUtils.getLength([])              = 0
     *     ArrayUtils.getLength([null])          = 1
     *     ArrayUtils.getLength([true, false])   = 2
     *     ArrayUtils.getLength([1, 2, 3])       = 3
     *     ArrayUtils.getLength(["a", "b", "c"]) = 3
     * </pre>
     *
     * @param array  the array to fetch the length from, may be null
     * @return The length of the array, or {@code 0} if the array is {@code null}
     * @throws IllegalArgumentException if the object argument is not an array.
     * @since 1.0.0
     */
    public static int getLength(final Object array) {
        return ObjectUtils.isNull(array) ? 0 : Array.getLength(array);
    }

}
