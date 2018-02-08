package com.enjin.java_commons;

import java.util.Random;

/**
 * <p>Operations involving {@link Random}.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will generally not be thrown for a {@code null} input.
 * Each method documents its behaviour in detail.</p>
 *
 * <p>#ThreadSafe#</p>
 *
 * @since 1.0
 */
public class RandomUtils {

    /**
     * <p>Random object used by random methods where a {@link Random} argument
     * is not present. Must not be local to the random methods to guarantee
     * randomness.</p>
     */
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * <p>{@code RandomUtils} instances should NOT be constructed in standard programming.
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public RandomUtils() {
        super();
    }

    /**
     * <p>Generates a random integer value using the given {@link Random} generator.</p>
     *
     * @return the random integer
     *
     * @since 1.0
     */
    public static int nextInt() {
        return nextInt(RANDOM);
    }

    /**
     * <p>Generates a random integer value using the given {@link Random} generator.</p>
     *
     * @return the random integer
     *
     * @throws NullPointerException if {@code generator} is null
     * @since 1.0
     */
    public static int nextInt(final Random generator) {
        if (generator == null)
            throw new NullPointerException("Random generator argument is null.");

        int number = generator.nextInt();

        return number;
    }

    /**
     * <p>Generates a random integer value within a range.</p>
     *
     * @param min the minimum bound
     * @param max the maximum bound
     *
     * @return the random integer
     *
     * @throws IllegalArgumentException if {@code max - min} is negative
     * @since 1.0
     */
    public static int nextIntInRange(final int min, final int max) {
        return nextIntInRange(RANDOM, min, max);
    }

    /**
     * <p>Generates a random integer value within a range using
     * the given {@link Random} generator.</p>
     *
     * @param min the minimum bound
     * @param max the maximum bound
     *
     * @return the random integer
     *
     * @throws NullPointerException     if {@code generator} is null
     * @throws IllegalArgumentException if {@code max - min} is negative
     * @since 1.0
     */
    public static int nextIntInRange(final Random generator, final int min, final int max) {
        if (generator == null)
            throw new NullPointerException("Random generator argument is null.");

        int number = generator.nextInt((max - min) + 1) + min;

        return number;
    }

}
