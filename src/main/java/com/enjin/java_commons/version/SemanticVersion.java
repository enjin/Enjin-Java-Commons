package com.enjin.java_commons.version;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SemanticVersion implements Comparable<SemanticVersion> {

    private static final Pattern CHARACTER_VALIDATION_PATTERN = Pattern.compile("[0-9A-Za-z]+");

    protected final int      major;
    protected final int      minor;
    protected final int      patch;
    protected final String[] preRelease;
    protected final String[] buildMeta;

    private int[]        vParts;
    private List<String> preParts;
    private List<String> metaParts;
    private int          errPos;
    private char[]       input;

    /**
     * Construct a new plain version object
     *
     * @param major major version number. Must not be negative
     * @param minor minor version number. Must not be negative
     * @param patch patchlevel. Must not be negative.
     */
    public SemanticVersion(int major, int minor, int patch) {
        this(major, minor, patch, new String[0], new String[0]);
    }

    /**
     * Construct a fully featured version object with all bells and whistles.
     *
     * @param major      major version number (must not be negative)
     * @param minor      minor version number (must not be negative)
     * @param patch      patch level (must not be negative).
     * @param preRelease pre release identifiers. Must not be null, all parts must match
     *                   "[0-9A-Za-z-]+".
     * @param buildMeta  build meta identifiers. Must not be null, all parts must match
     *                   "[0-9A-Za-z-]+".
     */
    public SemanticVersion(int major, int minor, int patch, String[] preRelease, String[] buildMeta) {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Semantic version numbers must be positive!");
        }

        this.preRelease = new String[preRelease.length];
        this.buildMeta = new String[buildMeta.length];

        for (int i = 0; i < preRelease.length; i++) {
            if (preRelease[i] == null || !CHARACTER_VALIDATION_PATTERN.matcher(preRelease[i]).matches()) {
                throw new IllegalArgumentException("Invalid pre release tag with index ".concat(String.valueOf(i)));
            }

            this.preRelease[i] = preRelease[i];
        }

        for (int i = 0; i < buildMeta.length; i++) {
            if (buildMeta[i] == null || !CHARACTER_VALIDATION_PATTERN.matcher(buildMeta[i]).matches()) {
                throw new IllegalArgumentException("Invalid build meta tag with index ".concat(String.valueOf(i)));
            }

            this.buildMeta[i] = buildMeta[i];
        }

        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Convenience constructor for creating a Version object from the
     * "Implementation-Version:" property of the Manifest file.
     *
     * @param clazz a class in the JAR file (or that otherwise has its
     *              implementationVersion attribute set).
     *
     * @throws ParseException if the versionstring does not conform to the semver specs.
     */
    public SemanticVersion(Class<?> clazz) throws ParseException {
        this(clazz.getPackage().getImplementationVersion());
    }

    /**
     * Construct a version object by parsing a string.
     *
     * @param version version in flat string format
     *
     * @throws ParseException if the version string does not conform to the semver specs.
     */
    public SemanticVersion(String version) throws ParseException {
        vParts = new int[3];
        preParts = new ArrayList<>(5);
        metaParts = new ArrayList<>(5);
        input = version.toCharArray();

        if (!stateMajor()) {
            throw new ParseException(version, errPos);
        }

        major = vParts[0];
        minor = vParts[1];
        patch = vParts[2];
        preRelease = preParts.toArray(new String[preParts.size()]);
        buildMeta = metaParts.toArray(new String[metaParts.size()]);
    }

    /**
     * Returns the major version.
     *
     * @return an integer greater than or equal to 0
     */
    public int getMajor() {
        return major;
    }

    /**
     * Returns the minor version.
     *
     * @return an integer greater than or equal to 0
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Returns the patch version.
     *
     * @return an integer greater than or equal to 0
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Returns an array of pre-release tags.
     *
     * @return an array of strings
     */
    public String[] getPreRelease() {
        return preRelease;
    }

    /**
     * Returns an array of build-meta tags.
     *
     * @return an array of strings
     */
    public String[] getBuildMeta() {
        return buildMeta;
    }

    /**
     * Check if this version has a given build Meta tags.
     *
     * @param tag the tag to check for.
     *
     * @return true if the tag is found in {@link SemanticVersion#buildMeta}.
     */
    public boolean hasPreRelease(String tag) {
        for (String s : preRelease) {
            if (s.equals(tag)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if this version has a given pre release tag.
     *
     * @param tag the tag to check for
     *
     * @return true if the tag is found in {@link SemanticVersion#preRelease}.
     */
    public boolean hasBuildMeta(String tag) {
        for (String s : buildMeta) {
            if (s.equals(tag)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Convenience method to check if this version is an update.
     *
     * @param other the other version object
     *
     * @return true if this version is newer than the other one.
     */
    public boolean isUpdateFor(SemanticVersion other) {
        return compareTo(other) > 0;
    }

    /**
     * Convenience method to check if this version is a compatible update.
     *
     * @param other the other version object.
     *
     * @return true if this version is newer and both have the same major version.
     */
    public boolean isCompatibleUpdateFor(SemanticVersion other) {
        return major == other.major && isUpdateFor(other);
    }

    /**
     * Convenience method to check if this is a stable version.
     *
     * @return true if the major version number is greater than zero and there are
     * no pre release tags.
     */
    public boolean isStable() {
        return major > 0 && preRelease.length == 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append(major)
                                                   .append('.')
                                                   .append(minor)
                                                   .append('.')
                                                   .append(patch);

        if (preRelease.length > 0) {
            builder.append('-');
            for (int i = 0; i < preRelease.length; i++) {
                builder.append(preRelease[i]);
                if (i < preRelease.length - 1) {
                    builder.append('.');
                }
            }
        }

        if (buildMeta.length > 0) {
            builder.append('+');
            for (int i = 0; i < buildMeta.length; i++) {
                builder.append(buildMeta[i]);
                if (i < buildMeta.length - 1) {
                    builder.append('.');
                }
            }
        }

        return builder.toString();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SemanticVersion)) {
            return false;
        }

        SemanticVersion other = (SemanticVersion) o;
        if (major != other.major || minor != other.minor || patch != other.patch) {
            return false;
        }

        if (preRelease.length != other.preRelease.length) {
            return false;
        }

        for (int i = 0; i < preRelease.length; i++) {
            if (!preRelease[i].equals(other.preRelease[i])) {
                return false;
            }
        }

        if (buildMeta.length != other.buildMeta.length) {
            return false;
        }

        for (int i = 0; i < buildMeta.length; i++) {
            if (!buildMeta[i].equals(other.buildMeta[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int compareTo(SemanticVersion other) {
        int result = major - other.major;
        if (result == 0) {
            result = minor - other.minor;
            if (result == 0) {
                result = patch - other.patch;
                if (result == 0) {
                    if (preRelease.length == 0 && other.preRelease.length > 0) {
                        result = 1;
                    }

                    if (other.preRelease.length == 0 && preRelease.length > 0) {
                        result = -1;
                    }

                    if (preRelease.length > 0 && other.preRelease.length > 0) {
                        int len   = Math.min(preRelease.length, other.preRelease.length);
                        int count = 0;

                        for (count = 0; count < len; count++) {
                            result = comparePreReleaseTag(count, other);
                            if (result != 0) {
                                break;
                            }
                        }

                        if (result == 0 && count == len) {
                            result = preRelease.length - other.preRelease.length;
                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean stateMajor() {
        int pos = 0;
        while (pos < input.length && input[pos] >= '0' && input[pos] <= '9') {
            pos++;
        }

        if (pos == 0) {
            return false;
        }

        vParts[0] = Integer.parseInt(new String(input, 0, pos), 10);

        if (input[pos] == '.') {
            return stateMinor(pos + 1);
        }

        return false;
    }

    private boolean stateMinor(int index) {
        int pos = index;
        while (pos < input.length && input[pos] >= '0' && input[pos] <= '9') {
            pos++;
        }

        if (pos == index) {
            errPos = pos;
            return false;
        }

        vParts[1] = Integer.parseInt(new String(input, index, pos - index), 10);

        if (input[pos] == '.') {
            return statePatch(pos + 1);
        }

        errPos = pos;
        return false;
    }

    private boolean statePatch(int index) {
        int pos = index;
        while (pos < input.length && input[pos] >= '0' && input[pos] <= '9') {
            pos++;
        }

        if (pos == index) {
            errPos = pos;
            return false;
        }

        vParts[2] = Integer.parseInt(new String(input, index, pos - index), 10);

        if (pos == input.length) {
            return true;
        }

        if (input[pos] == '-') {
            return stateRelease(pos + 1);
        }

        if (input[pos] == '+') {
            return stateMeta(pos + 1);
        }

        errPos = pos;
        return false;
    }

    private boolean stateRelease(int index) {
        int pos = index;
        while ((pos < input.length)
                && ((input[pos] >= '0' && input[pos] <= '9')
                || (input[pos] >= 'a' && input[pos] <= 'z')
                || (input[pos] >= 'A' && input[pos] <= 'Z'))
                || input[pos] == '-') {
            pos++;
        }

        if (pos == index) {
            errPos = pos;
            return false;
        }

        preParts.add(new String(input, index, pos - index));

        if (pos == input.length) {
            return true;
        }

        if (input[pos] == '.') {
            return stateRelease(pos + 1);
        }

        if (input[pos] == '+') {
            return stateMeta(pos + 1);
        }

        errPos = pos;
        return false;
    }

    private boolean stateMeta(int index) {
        int pos = index;
        while ((pos < input.length)
                && ((input[pos] >= '0' && input[pos] <= '9')
                || (input[pos] >= 'a' && input[pos] <= 'z')
                || (input[pos] >= 'A' && input[pos] <= 'Z'))
                || input[pos] == '-') {
            pos++;
        }

        if (pos == index) {
            errPos = pos;
            return false;
        }

        metaParts.add(new String(input, index, pos - index));

        if (pos == input.length) {
            return true;
        }

        if (input[pos] == '.') {
            return stateMeta(pos + 1);
        }

        errPos = pos;
        return false;
    }

    private int comparePreReleaseTag(int pos, SemanticVersion other) {
        Integer here  = null;
        Integer there = null;

        try {
            here = Integer.parseInt(preRelease[pos], 10);
        } catch (NumberFormatException e) { }

        try {
            there = Integer.parseInt(other.preRelease[pos], 10);
        } catch (NumberFormatException e) { }

        if (here != null && there != null) {
            return here.compareTo(there);
        } else if (here != null) {
            return -1;
        } else if (there != null) {
            return 1;
        } else {
            return preRelease[pos].compareTo(other.preRelease[pos]);
        }
    }
}
