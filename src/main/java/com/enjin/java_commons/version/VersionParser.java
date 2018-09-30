package com.enjin.java_commons.version;

import static com.enjin.java_commons.version.VersionParser.CharType.DIGIT;
import static com.enjin.java_commons.version.VersionParser.CharType.DOT;
import static com.enjin.java_commons.version.VersionParser.CharType.EOI;
import static com.enjin.java_commons.version.VersionParser.CharType.HYPHEN;
import static com.enjin.java_commons.version.VersionParser.CharType.LETTER;
import static com.enjin.java_commons.version.VersionParser.CharType.PLUS;
import com.enjin.java_commons.version.util.Stream;
import com.enjin.java_commons.version.util.Stream.ElementType;
import com.enjin.java_commons.version.util.UnexpectedElementException;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * A parser for the semantic Version.
 */
public class VersionParser implements Parser<Version> {

    /**
     * The stream of characters.
     */
    private final Stream<Character> characters;

    /**
     * Constructs a {@code VersionParser} instance
     * with the input string to parse.
     *
     * @param input the input string to parse
     *
     * @throws IllegalArgumentException if the input string is {@code NULL} or empty
     */
    public VersionParser(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input string is NULL or empty");
        }

        Character[] elements = new Character[input.length()];
        for (int i = 0; i < input.length(); i++) {
            elements[i] = input.charAt(i);
        }

        characters = new Stream<>(elements);
    }

    /**
     * Parses the input string.
     *
     * @param input the input string to parse
     *
     * @return a valid version object
     *
     * @throws ParseException               when there is a grammar error
     * @throws UnexpectedCharacterException when encounters an unexpected character type
     */
    @Override
    public Version parse(String input) {
        return parseValidSemVer();
    }

    /**
     * Parses the {@literal <valid semver>} non-terminal.
     *
     * <pre>
     * {@literal
     * <valid semver> ::= <version core>
     *                  | <version core> "-" <pre-release>
     *                  | <version core> "+" <build>
     *                  | <version core> "-" <pre-release> "+" <build>
     * }
     * </pre>
     *
     * @return a valid version object
     */
    private Version parseValidSemVer() {
        NormalVersion   normal     = parseVersionCore();
        MetadataVersion preRelease = MetadataVersion.NULL;
        MetadataVersion build      = MetadataVersion.NULL;

        Character next = consumeNextCharacter(HYPHEN, PLUS, EOI);
        if (HYPHEN.isMatchedBy(next)) {
            preRelease = parsePreRelease();
            next = consumeNextCharacter(PLUS, EOI);
            if (PLUS.isMatchedBy(next)) {
                build = parseBuild();
            }
        } else if (PLUS.isMatchedBy(next)) {
            build = parseBuild();
        }

        consumeNextCharacter(EOI);
        return new Version(normal, preRelease, build);
    }

    /**
     * Parses the {@literal <version core>} non-terminal.
     *
     * <pre>
     * {@literal
     * <version core> ::= <major> "." <minor> "." <patch>
     * }
     * </pre>
     *
     * @return a valid normal version object
     */
    private NormalVersion parseVersionCore() {
        int major = Integer.parseInt(numericIdentifier());
        consumeNextCharacter(DOT);
        int minor = Integer.parseInt(numericIdentifier());
        consumeNextCharacter(DOT);
        int patch = Integer.parseInt(numericIdentifier());
        return new NormalVersion(major, minor, patch);
    }

    /**
     * Parses the {@literal <pre-release>} non-terminal.
     *
     * <pre>
     * {@literal
     * <pre-release> ::= <dot-separated pre-release identifiers>
     *
     * <dot-separated pre-release identifiers> ::= <pre-release identifier>
     *    | <pre-release identifier> "." <dot-separated pre-release identifiers>
     * }
     * </pre>
     *
     * @return a valid pre-release version object
     */
    private MetadataVersion parsePreRelease() {
        ensureValidLookAhead(DIGIT, LETTER, HYPHEN);
        List<String> identifiers = new ArrayList<>();

        do {
            identifiers.add(preReleaseIdentifier());
            if (characters.positiveLookAhead(DOT)) {
                consumeNextCharacter(DOT);
                continue;
            }
            break;
        } while (true);

        return new MetadataVersion(identifiers.toArray(new String[0]));
    }

    /**
     * Parses the {@literal <pre-release identifier>} non-terminal.
     *
     * <pre>
     * {@literal
     * <pre-release identifier> ::= <alphanumeric identifier>
     *                            | <numeric identifier>
     * }
     * </pre>
     *
     * @return a single pre-release identifier
     */
    private String preReleaseIdentifier() {
        checkForEmptyIdentifier();
        CharType boundary = nearestCharType(DOT, PLUS, EOI);
        if (characters.positiveLookAheadBefore(boundary, LETTER, HYPHEN)) {
            return alphanumericIdentifier();
        } else {
            return numericIdentifier();
        }
    }

    /**
     * Parses the {@literal <build>} non-terminal.
     *
     * <pre>
     * {@literal
     * <build> ::= <dot-separated build identifiers>
     *
     * <dot-separated build identifiers> ::= <build identifier>
     *                | <build identifier> "." <dot-separated build identifiers>
     * }
     * </pre>
     *
     * @return a valid build metadata object
     */
    private MetadataVersion parseBuild() {
        ensureValidLookAhead(DIGIT, LETTER, HYPHEN);
        List<String> identifiers = new ArrayList<>();

        do {
            identifiers.add(buildIdentifier());
            if (characters.positiveLookAhead(DOT)) {
                consumeNextCharacter(DOT);
                continue;
            }
            break;
        } while (true);

        return new MetadataVersion(identifiers.toArray(new String[0]));
    }

    /**
     * Parses the {@literal <build identifier>} non-terminal.
     *
     * <pre>
     * {@literal
     * <build identifier> ::= <alphanumeric identifier>
     *                      | <digits>
     * }
     * </pre>
     *
     * @return a single build identifier
     */
    private String buildIdentifier() {
        checkForEmptyIdentifier();
        CharType boundary = nearestCharType(DOT, EOI);
        if (characters.positiveLookAheadBefore(boundary, LETTER, HYPHEN)) {
            return alphanumericIdentifier();
        } else {
            return digits();
        }
    }

    /**
     * Parses the {@literal <numeric identifier>} non-terminal.
     *
     * <pre>
     * {@literal
     * <numeric identifier> ::= "0"
     *                        | <positive digit>
     *                        | <positive digit> <digits>
     * }
     * </pre>
     *
     * @return a string representing the numeric identifier
     */
    private String numericIdentifier() {
        checkForLeadingZeroes();
        return digits();
    }

    /**
     * Parses the {@literal <alphanumeric identifier>} non-terminal.
     *
     * <pre>
     * {@literal
     * <alphanumeric identifier> ::= <non-digit>
     *             | <non-digit> <identifier characters>
     *             | <identifier characters> <non-digit>
     *             | <identifier characters> <non-digit> <identifier characters>
     * }
     * </pre>
     *
     * @return a string representing the alphanumeric identifier
     */
    private String alphanumericIdentifier() {
        StringBuilder sb = new StringBuilder();

        do {
            sb.append(consumeNextCharacter(DIGIT, LETTER, HYPHEN));
        } while (characters.positiveLookAhead(DIGIT, LETTER, HYPHEN));

        return sb.toString();
    }

    /**
     * Parses the {@literal <digits>} non-terminal.
     *
     * <pre>
     * {@literal
     * <digits> ::= <digit>
     *            | <digit> <digits>
     * }
     * </pre>
     *
     * @return a string representing the digits
     */
    private String digits() {
        StringBuilder sb = new StringBuilder();

        do {
            sb.append(consumeNextCharacter(DIGIT));
        } while (characters.positiveLookAhead(DIGIT));

        return sb.toString();
    }

    /**
     * Finds the nearest character type.
     *
     * @param types the character types to choose from
     *
     * @return the nearest character type or {@code EOI}
     */
    private CharType nearestCharType(CharType... types) {
        for (Character character : characters) {
            for (CharType type : types) {
                if (type.isMatchedBy(character)) {
                    return type;
                }
            }
        }
        return EOI;
    }

    /**
     * Checks for leading zeroes in the numeric identifiers.
     *
     * @throws ParseException if a numeric identifier has leading zero(es)
     */
    private void checkForLeadingZeroes() {
        Character la1 = characters.lookAhead();
        Character la2 = characters.lookAhead(2);
        if (la1 != null & la1 == '0' && DIGIT.isMatchedBy(la2)) {
            throw new ParseException("Numeric identifier MUST NOT contain leading zeroes");
        }
    }

    /**
     * Checks for empty identifiers in the pre-release version or build metadata.
     *
     * @throws ParseException if the pre-release version or build
     *                        metadata have empty identifier(s)
     */
    private void checkForEmptyIdentifier() {
        Character la = characters.lookAhead();
        if (DOT.isMatchedBy(la) || PLUS.isMatchedBy(la) || EOI.isMatchedBy(la)) {
            throw new ParseException("Identifiers MUST NOT be empty",
                                     new UnexpectedCharacterException(la,
                                                                      characters.getCurrentOffset(),
                                                                      DIGIT,
                                                                      LETTER,
                                                                      HYPHEN));
        }
    }

    /**
     * Tries to consume the next character in the stream.
     *
     * @param expected the expected types of the next character
     *
     * @return the next character in the stream
     *
     * @throws UnexpectedCharacterException when encounters an unexpected character type
     */
    private Character consumeNextCharacter(CharType... expected) {
        try {
            return characters.consume(expected);
        } catch (UnexpectedElementException e) {
            throw new UnexpectedCharacterException(e);
        }
    }

    /**
     * Checks if the next character in the stream is valid.
     *
     * @param expected the expected types of the next character
     *
     * @throws UnexpectedCharacterException if the next character is not valid
     */
    private void ensureValidLookAhead(CharType... expected) {
        if (!characters.positiveLookAhead(expected)) {
            throw new UnexpectedCharacterException(characters.lookAhead(), characters.getCurrentOffset(), expected);
        }
    }

    /**
     * Parses the whole version including pre-release version and build metadata.
     *
     * @param version the version string to parse
     *
     * @return a valid version object
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when there is a grammar error
     * @throws UnexpectedCharacterException when encounters an unexpected character type
     */
    public static Version parseValidSemVer(String version) {
        VersionParser parser = new VersionParser(version);
        return parser.parseValidSemVer();
    }

    /**
     * Parses the version core.
     *
     * @param versionCore the version core string to parse
     *
     * @return a valid normal version object
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when there is a grammar error
     * @throws UnexpectedCharacterException when encounters an unexpected character type
     */
    public static NormalVersion parseVersionCore(String versionCore) {
        VersionParser parser = new VersionParser(versionCore);
        return parser.parseVersionCore();
    }

    /**
     * Parses the pre-release version.
     *
     * @param preRelease the pre-release version string to parse
     *
     * @return a valid pre-release version object
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when there is a grammar error
     * @throws UnexpectedCharacterException when encounters an unexpected character type
     */
    public static MetadataVersion parsePreRelease(String preRelease) {
        VersionParser parser = new VersionParser(preRelease);
        return parser.parsePreRelease();
    }

    /**
     * Parses the build metadata.
     *
     * @param build the build metadata string to parse
     *
     * @return a valid build metadata object
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when there is a grammar error
     * @throws UnexpectedCharacterException when encounters an unexpected character type
     */
    public static MetadataVersion parseBuild(String build) {
        VersionParser parser = new VersionParser(build);
        return parser.parseBuild();
    }

    /**
     * Valid character types.
     */
    public static enum CharType implements ElementType<Character> {

        DIGIT {
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isMatchedBy(Character element) {
                return element != null && element >= '0' && element <= '9';
            }
        },
        LETTER {
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isMatchedBy(Character element) {
                return element != null && ((element >= 'a' && element <= 'z') || (element >= 'A' && element <= 'Z'));
            }
        },
        DOT {
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isMatchedBy(Character element) {
                return element != null && element == '.';
            }
        },
        HYPHEN {
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isMatchedBy(Character element) {
                return element != null && element == '-';
            }
        },
        PLUS {
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isMatchedBy(Character element) {
                return element != null && element == '+';
            }
        },
        EOI {
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isMatchedBy(Character element) {
                return element == null;
            }
        },
        ILLEGAL {
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isMatchedBy(Character element) {
                EnumSet<CharType> itself = EnumSet.of(ILLEGAL);
                for (CharType type : EnumSet.complementOf(itself)) {
                    if (type.isMatchedBy(element)) {
                        return false;
                    }
                }
                return true;
            }
        };

        /**
         * Gets the type for a given character.
         *
         * @param character the character to get the type for
         *
         * @return the type of the specified character
         */
        public static CharType forCharacter(Character character) {
            for (CharType type : values()) {
                if (type.isMatchedBy(character)) {
                    return type;
                }
            }
            return null;
        }
    }
}
