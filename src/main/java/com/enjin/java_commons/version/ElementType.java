package com.enjin.java_commons.version;

/**
 * The {@code ElementType} interface represents types of the elements
 * held by this stream and can be used for stream filtering.
 *
 * @param <E> type of elements held by this stream
 */
public interface ElementType<E> {

    /**
     * Checks if the specified element matches this type.
     *
     * @param element the element to be tested
     *
     * @return {@code true} if the element matches this type
     * or {@code false} otherwise
     */
    boolean isMatchedBy(E element);
}