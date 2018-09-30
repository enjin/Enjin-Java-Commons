package com.enjin.java_commons.version;

import java.util.Arrays;

/**
 * The {@code MetadataVersion} class is used to represent
 * the pre-release version and the build metadata.
 */
public class MetadataVersion implements Comparable<MetadataVersion> {

    /**
     * Null metadata, the implementation of the Null Object design pattern.
     */
    public static final MetadataVersion NULL = new NullMetadataVersion();

    /**
     * The array containing the version's identifiers.
     */
    private final String[] identifiers;

    /**
     * Constructs a {@code MetadataVersion} instance with identifiers.
     *
     * @param identifiers the version's identifiers
     */
    public MetadataVersion(String[] identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * Increments the metadata version.
     *
     * @return a new instance of the {@code MetadataVersion} class
     */
    public MetadataVersion increment() {
        String[] ids    = identifiers;
        String   lastId = ids[ids.length - 1];

        if (isInt(lastId)) {
            int intId = Integer.parseInt(lastId);
            ids[ids.length - 1] = String.valueOf(++intId);
        } else {
            ids = Arrays.copyOf(ids, ids.length + 1);
            ids[ids.length - 1] = String.valueOf(1);
        }

        return new MetadataVersion(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof MetadataVersion)) return false;
        MetadataVersion that = (MetadataVersion) other;
        return compareTo(that) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(identifiers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < identifiers.length; i++) {
            sb.append(identifiers[i]);
            if (i < identifiers.length - 1) {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(MetadataVersion other) {
        if (other == MetadataVersion.NULL) return -1;
        int result = compareIdentifierArrays(other.identifiers);
        if (result == 0) result = identifiers.length - other.identifiers.length;
        return result;
    }

    /**
     * Compares two arrays of identifiers.
     *
     * @param otherIdentifiers the identifiers of the other version
     *
     * @return integer result of comparison compatible with
     * the {@code Comparable.compareTo} method
     */
    private int compareIdentifierArrays(String[] otherIdentifiers) {
        int result = 0;
        int length = getLeastCommonArrayLength(identifiers, otherIdentifiers);
        for (int i = 0; i < length; i++) {
            result = compareIdentifiers(identifiers[i], otherIdentifiers[i]);
            if (result != 0) break;
        }
        return result;
    }

    /**
     * Returns the size of the smallest array.
     *
     * @param array1 the first array
     * @param array2 the second array
     *
     * @return the size of the smallest array
     */
    private int getLeastCommonArrayLength(String[] array1, String[] array2) {
        return array1.length <= array2.length ? array1.length : array2.length;
    }

    /**
     * Compares two identifiers.
     *
     * @param identifier1 the first identifier
     * @param identifier2 the second identifier
     *
     * @return integer result of comparison compatible with
     * the {@code Comparable.compareTo} method
     */
    private int compareIdentifiers(String identifier1, String identifier2) {
        if (isInt(identifier1) && isInt(identifier2)) {
            return Integer.parseInt(identifier1) - Integer.parseInt(identifier2);
        } else {
            return identifier1.compareTo(identifier2);
        }
    }

    /**
     * Checks if the specified string is an integer.
     *
     * @param string the string to check
     *
     * @return {@code true} if the specified string is an integer
     * or {@code false} otherwise
     */
    private boolean isInt(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * The implementation of the Null Object design pattern.
     */
    private static class NullMetadataVersion extends MetadataVersion {

        /**
         * Constructs a {@code NullMetadataVersion} instance.
         */
        public NullMetadataVersion() {
            super(null);
        }

        /**
         * @throws NullPointerException as Null metadata cannot be incremented
         */
        @Override
        public MetadataVersion increment() {
            throw new NullPointerException("Metadata version is NULL.");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object other) {
            return other instanceof NullMetadataVersion;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(MetadataVersion other) {
            return equals(other) ? 0 : 1;
        }
    }

}
