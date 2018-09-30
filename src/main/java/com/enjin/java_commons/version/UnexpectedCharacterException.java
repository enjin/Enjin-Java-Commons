package com.enjin.java_commons.version;

import com.enjin.java_commons.version.VersionParser.CharType;
import com.enjin.java_commons.version.util.UnexpectedElementException;

import java.util.Arrays;

/**
 * Thrown when attempting to consume a character of unexpected types.
 *
 * This exception is a wrapper exception extending {@code ParseException}.
 */
public class UnexpectedCharacterException extends ParseException {

    /**
     * The unexpected character.
     */
    private final Character  unexpected;
    /**
     * The position of the unexpected character.
     */
    private final int        position;
    /**
     * The array of expected character types.
     */
    private final CharType[] expected;

    /**
     * Constructs a {@code UnexpectedCharacterException} instance with
     * the wrapped {@code UnexpectedElementException} exception.
     *
     * @param cause the wrapped exception
     */
    public UnexpectedCharacterException(UnexpectedElementException cause) {
        this.position = cause.getPosition();
        this.unexpected = (Character) cause.getUnexpectedElement();
        this.expected = (CharType[]) cause.getExpectedElementTypes();
    }

    /**
     * Constructs a {@code UnexpectedCharacterException} instance
     * with the unexpected character, its position and the expected types.
     *
     * @param unexpected the unexpected character
     * @param position   the position of the unexpected character
     * @param expected   an array of the expected character types
     */
    public UnexpectedCharacterException(Character unexpected, int position, CharType... expected) {
        this.unexpected = unexpected;
        this.position = position;
        this.expected = expected;
    }

    /**
     * Gets the unexpected character.
     *
     * @return the unexpected character
     */
    Character getUnexpectedCharacter() {
        return unexpected;
    }

    /**
     * Gets the position of the unexpected character.
     *
     * @return the position of the unexpected character
     */
    int getPosition() {
        return position;
    }

    /**
     * Gets the expected character types.
     *
     * @return an array of expected character types
     */
    CharType[] getExpectedCharTypes() {
        return expected;
    }

    /**
     * Returns the string representation of this exception
     * containing the information about the unexpected
     * element and, if available, about the expected types.
     *
     * @return the string representation of this exception
     */
    @Override
    public String toString() {
        String msg = "Unexpected character '".concat(String.valueOf(CharType.forCharacter(unexpected)))
                                             .concat("(")
                                             .concat(String.valueOf(unexpected))
                                             .concat(")' at position '")
                                             .concat(String.valueOf(position))
                                             .concat("'");

        if (expected.length > 0) {
            msg.concat(", expecting '").concat(Arrays.toString(expected)).concat("'");
        }

        return msg;
    }
}
