package miniJava.SyntacticAnalyzer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * These are all the Java keywords that we have to account for.
 */
public enum KeyChar {
    SEMICOLON(';'),
    DOT('.'),
    COMMA(','),
    FORWARD_SLASH('/'),
    ASTERISK('*'),
    PLUS('+'),
    MINUS('-'),
    GREATER_THAN('>'),
    LESS_THAN('<'),
    EQUALS('='),
    EXCLAMATION('!'),
    AND('&'),
    OR('|'),
    LEFT_PAREN('('),
    RIGHT_PAREN(')'),
    LEFT_BRACKET('['),
    RIGHT_BRACKET(']'),
    LEFT_CURLY('{'),
    RIGHT_CURLY('}'),
    WHITE_SPACE(' ');

    private final char character;

    private KeyChar(char character) {
        this.character = character;
    }

    public char getCharacter() {
        return this.character;
    }

    private static Set<KeyChar> binaryOps = Set.of(
    KeyChar.GREATER_THAN,
    KeyChar.LESS_THAN,
    KeyChar.EQUALS,
    KeyChar.EXCLAMATION, // b/c we have !=
    KeyChar.AND,
    KeyChar.OR,
    KeyChar.PLUS,
    KeyChar.MINUS,
    KeyChar.ASTERISK,
    KeyChar.FORWARD_SLASH
        );

    private static final Set<KeyChar> bracketsAndDots =
            Set.of(
    KeyChar.DOT,
    KeyChar.SEMICOLON,
    KeyChar.COMMA,
    KeyChar.RIGHT_BRACKET,
    KeyChar.LEFT_BRACKET,
    KeyChar.LEFT_PAREN,
    KeyChar.RIGHT_PAREN,
    KeyChar.LEFT_CURLY,
    KeyChar.RIGHT_CURLY,
    KeyChar.WHITE_SPACE
        );

    public static Set<KeyChar> getIdentifierTerminators() {
        return Stream.of(bracketsAndDots, binaryOps).flatMap(Collection::stream).collect(Collectors.toUnmodifiableSet());
    }

    public static boolean isBinaryOps(char val) {
       return binaryOps.stream().map(KeyChar::getCharacter).anyMatch(c -> c == val);
    }

    public static boolean isIdentifierTerminator(char val) {
        return getIdentifierTerminators().stream().map(i -> i.character).anyMatch(c -> c == val);
    }
}
