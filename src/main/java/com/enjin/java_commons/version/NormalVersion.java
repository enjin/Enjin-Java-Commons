package com.enjin.java_commons.version;

import java.util.Objects;
import java.util.Optional;

/**
 * The {@code NormalVersion} class represents the version core.
 *
 * This class is immutable and hence thread-safe.
 */
public class NormalVersion implements Comparable<NormalVersion> {

    /**
     * The major version number.
     */
    private final Integer major;
    /**
     * The minor version number.
     */
    private final Optional<Integer> minor;
    /**
     * The patch version number.
     */
    private final Optional<Integer> patch;

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
    NormalVersion(Integer major, Integer minor, Integer patch) {
        if (major == null || major < 0 || (minor != null && minor < 0) || (patch != null && patch < 0)) {
            throw new IllegalArgumentException("Major, minor and patch version MUST be non-negative integers.");
        }

        this.major = major;
        this.minor = Optional.ofNullable(minor);
        this.patch = Optional.ofNullable(patch);
    }

    /**
     * Returns the major version number.
     *
     * @return the major version number
     */
    public Integer getMajor() {
        return major;
    }

    /**
     * Returns the minor version number.
     *
     * @return the minor version number
     */
    public Optional<Integer> getMinor() {
        return minor;
    }

    /**
     * Returns the patch version number.
     *
     * @return the patch version number
     */
    public Optional<Integer> getPatch() {
        return patch;
    }

    /**
     * Increments the major version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    public NormalVersion incrementMajor() {
        return new NormalVersion(major + 1, minor.orElse(null), patch.orElse(null));
    }

    /**
     * Increments the minor version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    public NormalVersion incrementMinor() {
        return new NormalVersion(major, minor.orElse(0) + 1, patch.orElse(null));
    }

    /**
     * Increments the patch version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    public NormalVersion incrementPatch() {
        return new NormalVersion(major, minor.get(), patch.orElse(0) + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(NormalVersion other) {
        int result = major - other.major;
        if (result == 0) {
            result = minor.orElse(0) - other.minor.orElse(0);
            if (result == 0) {
                result = patch.orElse(0) - other.patch.orElse(0);
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
                minor.orElse(0) == that.minor.orElse(0) &&
                patch.orElse(0) == that.patch.orElse(0);
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
        String version = String.valueOf(major);

        if (minor.isPresent()) {
            version = version.concat(".").concat(String.valueOf(minor.get()));
            if (patch.isPresent()) {
                version = version.concat(".").concat(String.valueOf(patch.get()));
            }
        }

        return version;
    }
}
