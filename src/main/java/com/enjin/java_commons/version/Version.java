package com.enjin.java_commons.version;

import java.util.Comparator;
import java.util.Objects;

/**
 * The {@code Version} class is the main class of the Java SemVer library.
 *
 * This class implements the Facade design pattern.
 * It is also immutable, which makes the class thread-safe.
 */
public class Version implements Comparable<Version> {

    /**
     * A comparator that respects the build metadata when comparing versions.
     */
    public static final Comparator<Version> BUILD_AWARE_ORDER = new BuildAwareOrder();

    /**
     * A separator that separates the pre-release
     * version from the normal version.
     */
    private static final String PRE_RELEASE_PREFIX = "-";
    /**
     * A separator that separates the build metadata from
     * the normal version or the pre-release version.
     */
    private static final String BUILD_PREFIX       = "+";

    /**
     * The normal version.
     */
    private final NormalVersion   normal;
    /**
     * The pre-release version.
     */
    private final MetadataVersion preRelease;
    /**
     * The build metadata.
     */
    private final MetadataVersion build;

    /**
     * Constructs a {@code Version} instance with the normal version.
     *
     * @param normal the normal version
     */
    public Version(NormalVersion normal) {
        this(normal, MetadataVersion.NULL, MetadataVersion.NULL);
    }

    /**
     * Constructs a {@code Version} instance with the
     * normal version and the pre-release version.
     *
     * @param normal     the normal version
     * @param preRelease the pre-release version
     */
    public Version(NormalVersion normal, MetadataVersion preRelease) {
        this(normal, preRelease, MetadataVersion.NULL);
    }

    /**
     * Constructs a {@code Version} instance with the normal
     * version, the pre-release version and the build metadata.
     *
     * @param normal     the normal version
     * @param preRelease the pre-release version
     * @param build      the build metadata
     */
    public Version(NormalVersion normal, MetadataVersion preRelease, MetadataVersion build) {
        this.normal = normal;
        this.preRelease = preRelease;
        this.build = build;
    }

    /**
     * Increments the major version.
     *
     * @return a new instance of the {@code Version} class
     */
    public Version incrementMajorVersion() {
        return new Version(normal.incrementMajor());
    }

    /**
     * Increments the major version and appends the pre-release version.
     *
     * @param preRelease the pre-release version to append
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when invalid version string is provided
     * @throws UnexpectedCharacterException is a special case of {@code ParseException}
     */
    public Version incrementMajorVersion(String preRelease) {
        return new Version(normal.incrementMajor(), VersionParser.parsePreRelease(preRelease));
    }

    /**
     * Increments the minor version.
     *
     * @return a new instance of the {@code Version} class
     */
    public Version incrementMinorVersion() {
        return new Version(normal.incrementMinor());
    }

    /**
     * Increments the minor version and appends the pre-release version.
     *
     * @param preRelease the pre-release version to append
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when invalid version string is provided
     * @throws UnexpectedCharacterException is a special case of {@code ParseException}
     */
    public Version incrementMinorVersion(String preRelease) {
        return new Version(normal.incrementMinor(), VersionParser.parsePreRelease(preRelease)
        );
    }

    /**
     * Increments the patch version.
     *
     * @return a new instance of the {@code Version} class
     */
    public Version incrementPatchVersion() {
        return new Version(normal.incrementPatch());
    }

    /**
     * Increments the patch version and appends the pre-release version.
     *
     * @param preRelease the pre-release version to append
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when invalid version string is provided
     * @throws UnexpectedCharacterException is a special case of {@code ParseException}
     */
    public Version incrementPatchVersion(String preRelease) {
        return new Version(normal.incrementPatch(), VersionParser.parsePreRelease(preRelease)
        );
    }

    /**
     * Increments the pre-release version.
     *
     * @return a new instance of the {@code Version} class
     */
    public Version incrementPreReleaseVersion() {
        return new Version(normal, preRelease.increment());
    }

    /**
     * Increments the build metadata.
     *
     * @return a new instance of the {@code Version} class
     */
    public Version incrementBuildMetadata() {
        return new Version(normal, preRelease, build.increment());
    }

    /**
     * Sets the pre-release version.
     *
     * @param preRelease the pre-release version to set
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when invalid version string is provided
     * @throws UnexpectedCharacterException is a special case of {@code ParseException}
     */
    public Version setPreReleaseVersion(String preRelease) {
        return new Version(normal, VersionParser.parsePreRelease(preRelease));
    }

    /**
     * Sets the build metadata.
     *
     * @param build the build metadata to set
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when invalid version string is provided
     * @throws UnexpectedCharacterException is a special case of {@code ParseException}
     */
    public Version setBuildMetadata(String build) {
        return new Version(normal, preRelease, VersionParser.parseBuild(build));
    }

    /**
     * Returns the major version number.
     *
     * @return the major version number
     */
    public int getMajorVersion() {
        return normal.getMajor();
    }

    /**
     * Returns the minor version number.
     *
     * @return the minor version number
     */
    public int getMinorVersion() {
        return normal.getMinor();
    }

    /**
     * Returns the patch version number.
     *
     * @return the patch version number
     */
    public int getPatchVersion() {
        return normal.getPatch();
    }

    /**
     * Returns the string representation of the normal version.
     *
     * @return the string representation of the normal version
     */
    public String getNormalVersion() {
        return normal.toString();
    }

    /**
     * Returns the string representation of the pre-release version.
     *
     * @return the string representation of the pre-release version
     */
    public String getPreReleaseVersion() {
        return preRelease.toString();
    }

    /**
     * Returns the string representation of the build metadata.
     *
     * @return the string representation of the build metadata
     */
    public String getBuildMetadata() {
        return build.toString();
    }

    /**
     * Checks if this version is greater than the other version.
     *
     * @param other the other version to compare to
     *
     * @return {@code true} if this version is greater than the other version
     * or {@code false} otherwise
     *
     * @see #compareTo(Version other)
     */
    public boolean greaterThan(Version other) {
        return compareTo(other) > 0;
    }

    /**
     * Checks if this version is greater than or equal to the other version.
     *
     * @param other the other version to compare to
     *
     * @return {@code true} if this version is greater than or equal
     * to the other version or {@code false} otherwise
     *
     * @see #compareTo(Version other)
     */
    public boolean greaterThanOrEqualTo(Version other) {
        return compareTo(other) >= 0;
    }

    /**
     * Checks if this version is less than the other version.
     *
     * @param other the other version to compare to
     *
     * @return {@code true} if this version is less than the other version
     * or {@code false} otherwise
     *
     * @see #compareTo(Version other)
     */
    public boolean lessThan(Version other) {
        return compareTo(other) < 0;
    }

    /**
     * Checks if this version is less than or equal to the other version.
     *
     * @param other the other version to compare to
     *
     * @return {@code true} if this version is less than or equal
     * to the other version or {@code false} otherwise
     *
     * @see #compareTo(Version other)
     */
    public boolean lessThanOrEqualTo(Version other) {
        return compareTo(other) <= 0;
    }

    /**
     * Compare this version to the other version
     * taking into account the build metadata.
     *
     * The method makes use of the {@code Version.BUILD_AWARE_ORDER} comparator.
     *
     * @param other the other version to compare to
     *
     * @return integer result of comparison compatible with
     * that of the {@code Comparable.compareTo} method
     *
     * @see #BUILD_AWARE_ORDER
     */
    public int compareWithBuildsTo(Version other) {
        return BUILD_AWARE_ORDER.compare(this, other);
    }

    /**
     * Checks if this version equals the other version.
     *
     * The comparison is done by the {@code Version.compareTo} method.
     *
     * @param other the other version to compare to
     * @return {@code true} if this version equals the other version
     *         or {@code false} otherwise
     * @see #compareTo(Version other)
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof Version)) return false;
        Version version = (Version) other;
        return compareTo(version) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(normal, preRelease);
    }

    /**
     * Compares this version to the other version.
     *
     * This method does not take into account the versions' build
     * metadata. If you want to compare the versions' build metadata
     * use the {@code Version.compareWithBuildsTo} method or the
     * {@code Version.BUILD_AWARE_ORDER} comparator.
     *
     * @param other the other version to compare to
     *
     * @return a negative integer, zero or a positive integer if this version
     * is less than, equal to or greater the the specified version
     *
     * @see #BUILD_AWARE_ORDER
     * @see #compareWithBuildsTo(Version other)
     */
    @Override
    public int compareTo(Version other) {
        int result = normal.compareTo(other.normal);
        if (result == 0) {
            result = preRelease.compareTo(other.preRelease);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getNormalVersion());

        if (!getPreReleaseVersion().isEmpty()) {
            sb.append(PRE_RELEASE_PREFIX).append(getPreReleaseVersion());
        }

        if (!getBuildMetadata().isEmpty()) {
            sb.append(BUILD_PREFIX).append(getBuildMetadata());
        }

        return sb.toString();
    }

    /**
     * Creates a new instance of {@code Version} as a
     * result of parsing the specified version string.
     *
     * @param version the version string to parse
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when invalid version string is provided
     * @throws UnexpectedCharacterException is a special case of {@code ParseException}
     */
    public static Version valueOf(String version) {
        return VersionParser.parseValidSemVer(version);
    }

    /**
     * Creates a new instance of {@code Version}
     * for the specified version numbers.
     *
     * @param major the major version number
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException if a negative integer is passed
     */
    public static Version forIntegers(int major) {
        return new Version(new NormalVersion(major, 0, 0));
    }

    /**
     * Creates a new instance of {@code Version}
     * for the specified version numbers.
     *
     * @param major the major version number
     * @param minor the minor version number
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException if a negative integer is passed
     */
    public static Version forIntegers(int major, int minor) {
        return new Version(new NormalVersion(major, minor, 0));
    }

    /**
     * Creates a new instance of {@code Version}
     * for the specified version numbers.
     *
     * @param major the major version number
     * @param minor the minor version number
     * @param patch the patch version number
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException if a negative integer is passed
     */
    public static Version forIntegers(int major, int minor, int patch) {
        return new Version(new NormalVersion(major, minor, patch));
    }

    /**
     * A build-aware comparator.
     */
    private static class BuildAwareOrder implements Comparator<Version> {

        /**
         * Compares two {@code Version} instances taking
         * into account their build metadata.
         *
         * When compared build metadata is divided into identifiers. The
         * numeric identifiers are compared numerically, and the alphanumeric
         * identifiers are compared in the ASCII sort order.
         *
         * If one of the compared versions has no defined build
         * metadata, this version is considered to have a lower
         * precedence than that of the other.
         *
         * @return {@inheritDoc}
         */
        @Override
        public int compare(Version version1, Version version2) {
            int result = version1.compareTo(version2);
            if (result == 0) {
                result = version1.build.compareTo(version2.build);
                if (version1.build == MetadataVersion.NULL || version2.build == MetadataVersion.NULL) {
                    result = -1 * result;
                }
            }
            return result;
        }
    }

}
