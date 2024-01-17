package miniJava.SyntacticAnalyzer;

import java.io.IOException;
import java.io.InputStream;

import miniJava.ErrorReporter;

import static java.lang.Character.isDigit;

public class Scanner {
    private InputStream _in;
    private ErrorReporter _errors;
    private StringBuilder _currentText;
    private char _currentChar;
    private boolean eot;

    public Scanner(InputStream in, ErrorReporter errors) {
        this._in = in;
        this._errors = errors;
        this._currentText = new StringBuilder();

        nextChar();
    }

    /**
     * This function will be called to retrieve a single token.
     *
     * @return {@link Token} that represents the current token that was just scanned.
     */
    public Token scan() {
        // TODO: This function should check the current char to determine what the token could be.
        // TODO: Consider what happens if the current char is whitespace
        // TODO: Consider what happens if there is a comment (// or /* */)
        skipWhiteSpaceAndComments(); // Could potentially inline the function here.

        // TODO: What happens if there are no more tokens?
        TokenType tokenType = scanTokenType();

        // TODO: Determine what the token is. For example, if it is a number
        //  keep calling takeIt() until _currentChar is not a number. Then
        //  create the token via makeToken(TokenType.IntegerLiteral) and return it.
        return makeToken(tokenType);
    }

    private void takeIt() {
        _currentText.append(_currentChar);
        nextChar();
    }

    private void skipIt() {
        nextChar();
    }

    /**
     * This function simply sets the {@link #_currentChar} field in the class.
     */
    private void nextChar() {
        try {
            int c = _in.read();
            // TODO: What happens if c == -1?
            // c == -1 means that the end of the input stream has been reached.
            // TODO: This is supposed to terminate scanner, how can we do that? Send `eot`?
            if (c == -1) {
                eot = true;
                return;
            }
            // TODO: What happens if c is not a regular ASCII character?
            // ASCII characters only go up to 127, therefore, 128 and up are not ASCII characters
            if (c > 127) {
                throw new IOException("Non-ASCII character was read in.");
            }
            _currentChar = (char) c;
        } catch (IOException e) {
            // TODO: Report an error here
            _errors.reportError(e.getMessage());
            eot = true;
        }
    }

    private Token makeToken(TokenType toktype) {
        String text = this._currentText.toString();
        this._currentText = new StringBuilder(); // Reset the StringBuilder after creating the string.
        return new Token(toktype, text);
    }

    public TokenType scanTokenType() {
        // Keep iterating, until the end, and check if it's a keyword (perhaps store a lit of keywords).

        if (eot) {
            return TokenType.EOT;
        }

        switch(_currentChar) {
            case '*':
                takeIt();
                return TokenType.BINARY_OPERATOR;
            case '/':
                takeIt();
                return TokenType.BINARY_OPERATOR;
            case '+':
                takeIt();
                return TokenType.BINARY_OPERATOR;
            case '-':
                takeIt();
                return TokenType.MINUS;
            case '.':
                takeIt();
                return TokenType.DOT;
            case ';':
                takeIt();
                return TokenType.SEMICOLON;
            case '(':
                takeIt();
                return TokenType.LEFT_PAREN;
            case ')':
                takeIt();
                return TokenType.RIGHT_PAREN;
            case '{':
                takeIt();
                return TokenType.LEFT_CURLY;
            case '}':
                takeIt();
                return TokenType.RIGHT_CURLY;
            case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                while(isDigit(_currentChar)) {
                    takeIt();
                }
                return TokenType.INT_LITERAL;
            default:
                return parseMultiCharacterToken();
        }
    }

    private TokenType parseMultiCharacterToken() {
        // Tokens to parse here are && || >= <= == != true false identifiers if else

        return TokenType.DOT;
    }

    /*
     * The invariant in this function is that when either {@link #skipWhitespace()} or {@link #skipComment()} returns,
     * {@link #_currentChar} is the set to a non-whitespace/comment character.
     */
    private void skipWhiteSpaceAndComments() {
        while (!eot && (_currentChar == ' ' || _currentChar == '/') ) {
            if (_currentChar == ' ') {
                skipWhitespace();
            }
            else {
                takeIt();
                skipComment();
            }
        }
    }

    private void skipWhitespace() {
        while (!eot && _currentChar == ' ') {
            skipIt();
        }
    }

    /**
     * Going into this function, you know that the last character was a `/`.
     */
    private void skipComment() {
        nextChar();
        if (_currentChar == '/') {
            _currentText = new StringBuilder(); // You have to clear the clear, b/c you have to take the initial `/`, since it might be a division symbol.
            skipInlineComment();
        } else if (_currentChar == '*') {
            _currentText = new StringBuilder(); // You have to clear the clear, b/c you have to take the initial `/`, since it might be a division symbol.
            skipBlockComment();
        }
        // The else block is not necessary, if the next character is not `/` or `*`, then you have to be an assignment operator.
    }

    private void skipInlineComment() {
        while (!eot && _currentChar != '\n') {
            skipIt();
        }
    }

    private void skipBlockComment() {
        findEndOfBlockComment();
        skipIt();
    }

    private void findEndOfBlockComment() {
        while (!eot && _currentChar != '*') {
            skipIt();
        }
        nextChar(); // Get the character after '*'.
        if (_currentChar != '/') {
            _errors.reportError("Invalid comment syntax. Expected a \\/ after a \\*, but got " + _currentChar + ".");
            eot = true;
        }
        else {
            findEndOfBlockComment();
        }
    }
}
