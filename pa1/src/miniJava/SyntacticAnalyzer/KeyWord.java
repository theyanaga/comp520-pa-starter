package miniJava.SyntacticAnalyzer;

import java.util.Arrays;
import java.util.Optional;

public enum KeyWord {
    IF("if", TokenType.IF),
    ELSE("else", TokenType.ELSE),
    WHILE("while", TokenType.WHILE),
    NEW("new", TokenType.NEW),
    TRUE("true", TokenType.BOOL_LITERAL),
    FALSE("false", TokenType.BOOL_LITERAL),
    INT("int", TokenType.INTEGER),
    BOOLEAN("boolean", TokenType.BOOLEAN),
    THIS("this", TokenType.THIS),
    CLASS("class", TokenType.CLASS),
    VOID("void", TokenType.VOID),
    STATIC("static", TokenType.ACCESS),
    PUBLIC("public", TokenType.VISIBILITY),
    PRIVATE("private", TokenType.VISIBILITY),
    RETURN("return", TokenType.RETURN);

    private final String text;

    private final TokenType tokenType;

    public String getText() {
        return text;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    private KeyWord(String text, TokenType tokenType) {
        this.text = text;
        this.tokenType = tokenType;
    }

    public static TokenType determineTokenType(String val) {
        Optional<KeyWord> optKeyWord = Arrays.stream(values()).filter(v -> v.getText().equals(val)).findFirst();
        return optKeyWord.get().getTokenType(); // Could be refactored.
    }

    public static boolean isKeyWord(String val) {
        return Arrays.stream(values()).map(KeyWord::getText).anyMatch(s -> s.equals(val));
    }
}
