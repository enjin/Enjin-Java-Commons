package com.enjin.java_commons.version;

import java.util.Objects;

/**
 * The {@code NormalVersion} class represents the version core.
 *
 * This class is immutable and hence thread-safe.
 */
public class NormalVersion implements Comparable<NormalVersion> {

    /**
     * The major version number.
     */
    private final int major;
    /**
     * The minor version number.
     */
    private final int minor;
    /**
     * The patch version number.
     */
    private final int patch;

    /**
     * Constructs a {@code NormalVersion} with the
     * major, minor and patch version numbers.
     *
     * @param major the major version number
     * @param minor the minor version number
     * @param patch the patch version number
     *
     * @throws IllegalArgumentException if one of the version numbers is a negative integer
     */
    NormalVersion(int major, int minor, int patch) {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Major, minor and patch version MUST be non-negative integers.");
        }

        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Returns the major version number.
     *
     * @return the major version number
     */
    public int getMajor() {
        return major;
    }

    /**
     * Returns the minor version number.
     *
     * @return the minor version number
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Returns the patch version number.
     *
     * @return the patch version number
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Increments the major version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    public NormalVersion incrementMajor() {
        return new NormalVersion(major + 1, 0, 0);
    }

    /**
     * Increments the minor version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    public NormalVersion incrementMinor() {
        return new NormalVersion(major, minor + 1, 0);
    }

    /**
     * Increments the patch version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    public NormalVersion incrementPatch() {
        return new NormalVersion(major, minor, patch + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(NormalVersion other) {
        int result = major - other.major;
        if (result == 0) {
            result = minor - other.minor;
            if (result == 0) {
                result = patch - other.patch;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof NormalVersion)) return false;
        NormalVersion that = (NormalVersion) other;
        return major == that.major &&
                minor == that.minor &&
                patch == that.patch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }

    /**
     * Returns the string representation of this normal version.
     *
     * A normal version number MUST take the form X.Y.Z where X, Y, and Z are
     * non-negative integers. X is the major version, Y is the minor version,
     * and Z is the patch version. (SemVer p.2)
     *
     * @return the string representation of this normal version
     */
    @Override
    public String toString() {
        return String.valueOf(major)
                     .concat(".")
                     .concat(String.valueOf(minor))
                     .concat(".")
                     .concat(String.valueOf(patch));
    }
}
