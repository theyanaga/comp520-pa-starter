package miniJava.SyntacticAnalyzer;

import java.security.Key;

/**
 * These are all the Java keywords that we have to account for.
 */
public enum Keyword {
    IF("if"),
    ELSE("else"),
    SEMICOLON(";"),
    FORWARD_SLASH("/"),
    ASTERISK("*"),
    NEW("new"),
    TRUE("true"),
    FALSE("false"),
    INT("int"),
    CLASS("class"),
    VOID("void"),
    STATIC("static"),
    PUBLIC("public"),
    PRIVATE("private"),
    PLUS("+"),
    MINUS("-"),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    EQUALS("="),
    EXCLAMATION("!"),
    AND("and"),
    OR("or");

    private final String text;

    private Keyword(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

}
