package miniJava.SyntacticAnalyzer;

import miniJava.ErrorReporter;

import java.util.Arrays;
import java.util.Queue;
import java.util.Stack;

public class Parser {
    private Scanner _scanner;
    private ErrorReporter _errors;
    private Token _currentToken;

    private int test;


    public Parser(Scanner scanner, ErrorReporter errors) {
        this._scanner = scanner;
        this._errors = errors;
        this._currentToken = this._scanner.scan();
    }

    class SyntaxError extends Error {
        private static final long serialVersionUID = -6461942006097999362L;

        public SyntaxError(String message) {
            super(message);
        }
    }

    public void parse() {
        try {
            // The first thing we need to parse is the Program symbol
            parseProgram();
        } catch (SyntaxError e) {
            _errors.reportError(e.getMessage());
        }
    }

    // Program ::= (ClassDeclaration)* eot
    private void parseProgram() throws SyntaxError {
        // TODO: Keep parsing class declarations until eot
        if (_currentToken.getTokenType() == TokenType.EOT) {
            accept(TokenType.EOT);
        } else {
            while (_currentToken.getTokenType() != TokenType.EOT) {
                parseClassDeclaration();
            }
            accept(TokenType.EOT);
        }
    }

    // ClassDeclaration ::= class identifier { (FieldDeclaration|MethodDeclaration)* }
    private void parseClassDeclaration() throws SyntaxError {
        // TODO: Take in a "class" token (check by the TokenType)
        //  What should be done if the first token isn't "class"?
        accept(TokenType.CLASS);
        accept(TokenType.IDENTIFIER);
        accept(TokenType.LEFT_CURLY);
        // Parse field or method declaration.
        while (_currentToken.getTokenType() != TokenType.RIGHT_CURLY) {
            parseFieldOrMethodDeclaration();
        }
        accept(TokenType.RIGHT_CURLY);
    }

    private void parseFieldOrMethodDeclaration() {
        if (_currentToken.getTokenType() == TokenType.VISIBILITY) {
            parseVisibility();
        }
        if (_currentToken.getTokenType() == TokenType.ACCESS) {
            parseAccess();
        }
        if (_currentToken.getTokenType() == TokenType.VOID) { // Then it has to be method
            accept(TokenType.VOID);
            parseMethodDeclaration();
        } else {
            parseType();
            accept(TokenType.IDENTIFIER);
            if (_currentToken.getTokenType() == TokenType.SEMICOLON) {
                accept(TokenType.SEMICOLON);
            } else {
                accept(TokenType.LEFT_PAREN);
                if (_currentToken.getTokenType() != TokenType.RIGHT_PAREN) {
                    parseParameterList();
                }
                accept(TokenType.RIGHT_PAREN);
                accept(TokenType.LEFT_CURLY);
                do {
                    parseStatement();
                }
                while (_currentToken.getTokenType() != TokenType.RIGHT_CURLY);
                accept(TokenType.RIGHT_CURLY);
            }
        }
    }

    private void parseMethodDeclaration() {
        accept(TokenType.IDENTIFIER);
        accept(TokenType.LEFT_PAREN);
        if (_currentToken.getTokenType() == TokenType.RIGHT_PAREN) {
            accept(TokenType.RIGHT_PAREN);
        } else {
            parseParameterList();
            accept(TokenType.RIGHT_PAREN);
        }
        accept(TokenType.LEFT_CURLY);
        while (_currentToken.getTokenType() != TokenType.RIGHT_CURLY) {
            parseStatement();
        }
        accept(TokenType.RIGHT_CURLY);
    }

    public void parseVisibility() {
        accept(TokenType.VISIBILITY);
    }

    public void parseAccess() {
        accept(TokenType.ACCESS);
    }

    public void parseType() {
        if (_currentToken.getTokenType() == TokenType.BOOLEAN) {
            accept(TokenType.BOOLEAN);
        } else {
            accept(TokenType.INTEGER, TokenType.IDENTIFIER);
            if (_currentToken.getTokenType() == TokenType.LEFT_BRACKET) {
                accept(TokenType.LEFT_BRACKET);
                accept(TokenType.RIGHT_BRACKET);
            }
        }
    }

    public void parseParameterList() {
        parseType();
        accept(TokenType.IDENTIFIER);
        while (_currentToken.getTokenType() == TokenType.COMMA) {
            accept(TokenType.COMMA);
            parseType();
            accept(TokenType.IDENTIFIER);
        }
    }

    public void parseArgumentList() {
        parseExpression();
        while (_currentToken.getTokenType() == TokenType.COMMA) {
            accept(TokenType.COMMA);
            parseExpression();
        }
    }

    public void parseReference() {
        accept(TokenType.THIS, TokenType.IDENTIFIER);
        while (_currentToken.getTokenType() == TokenType.DOT) {
            accept(TokenType.THIS, TokenType.IDENTIFIER);
        }
    }

    public void parseExpression() {
        TokenType currentTokenType = _currentToken.getTokenType();
        switch (currentTokenType) {
            case NEW:
                accept(TokenType.NEW);
                parseObjectOrArrayCreation();
                break;
            case INT_LITERAL:
                accept(TokenType.INT_LITERAL);
                break;
            case BOOL_LITERAL:
                accept(TokenType.BOOL_LITERAL);
                break;
            case MINUS:
                accept(TokenType.MINUS);
                parseExpression();
                break;
            case UNARY_OPERATOR:
                accept(TokenType.UNARY_OPERATOR);
                parseExpression();
                break;
            case LEFT_PAREN:
                accept(TokenType.LEFT_PAREN);
                parseExpression();
                accept(TokenType.RIGHT_PAREN);
                break;
            case IDENTIFIER:
            case THIS:
                parseReference();
                if (_currentToken.getTokenType() == TokenType.LEFT_PAREN) {
                    accept(TokenType.LEFT_PAREN);
                    parseExpression();
                    accept(TokenType.RIGHT_PAREN);
                } else if (_currentToken.getTokenType() == TokenType.LEFT_BRACKET) {
                    accept(TokenType.LEFT_BRACKET);
                    parseArgumentList();
                    accept(TokenType.RIGHT_BRACKET);
                }
                break;
            default:
                accept(TokenType.THIS, TokenType.IDENTIFIER, TokenType.LEFT_PAREN, TokenType.UNARY_OPERATOR, TokenType.MINUS, TokenType.BOOL_LITERAL, TokenType.INT_LITERAL, TokenType.NEW);
                break;
        }
        if (_currentToken.getTokenType() == TokenType.BINARY_OPERATOR || _currentToken.getTokenType() == TokenType.MINUS) {
            accept(TokenType.BINARY_OPERATOR, TokenType.MINUS);
            parseExpression();
        }
    }

    public void parseObjectOrArrayCreation() {
        accept(TokenType.LEFT_PAREN);
        if (_currentToken.getTokenType() == TokenType.INTEGER) {
            accept(TokenType.INTEGER);
            accept(TokenType.LEFT_BRACKET);
            parseExpression();
            accept(TokenType.RIGHT_BRACKET);
        } else {
            accept(TokenType.IDENTIFIER);
            if (_currentToken.getTokenType() == TokenType.LEFT_PAREN) {
                accept(TokenType.LEFT_PAREN);
                accept(TokenType.RIGHT_PAREN);
            } else {
                accept(TokenType.LEFT_BRACKET);
                parseExpression();
                accept(TokenType.RIGHT_BRACKET);
            }
        }
    }

    public void parseStatement() {
        TokenType currentTokenType = _currentToken.getTokenType();
        switch (currentTokenType) {
            case LEFT_CURLY:
                accept(TokenType.LEFT_CURLY);
                if (_currentToken.getTokenType() != TokenType.RIGHT_CURLY) {
                    while (_currentToken.getTokenType() != TokenType.RIGHT_CURLY) {
                        parseStatement();
                    }
                }
                accept(TokenType.RIGHT_CURLY);
                return;
            case RETURN:
                accept(TokenType.RETURN);
                if (_currentToken.getTokenType() != TokenType.SEMICOLON) {
                    parseExpression();
                }
                accept(TokenType.SEMICOLON);
                return;
            case IF:
                parseIfStatement();
                return;
            case WHILE:
                parseWhileLoop();
                return;
            case INTEGER:
            case BOOLEAN:
                parseType();
                accept(TokenType.IDENTIFIER);
                accept(TokenType.ASSIGNMENT);
                parseExpression();
                accept(TokenType.SEMICOLON);
                return;
            case THIS:
                parseReference();
            case IDENTIFIER:
                accept(TokenType.IDENTIFIER);
                if (_currentToken.getTokenType() == TokenType.LEFT_BRACKET) { // array type
                    accept(TokenType.LEFT_BRACKET);
                    accept(TokenType.RIGHT_BRACKET);
                    accept(TokenType.IDENTIFIER);
                    accept(TokenType.ASSIGNMENT);
                    parseExpression();
                    accept(TokenType.SEMICOLON);
                } else if (_currentToken.getTokenType() == TokenType.DOT) { // reference
                    accept(TokenType.DOT);
                    parseReference();
                    if (_currentToken.getTokenType() == TokenType.LEFT_BRACKET) {
                        accept(TokenType.LEFT_BRACKET);
                        parseExpression();
                        accept(TokenType.RIGHT_BRACKET);
                        accept(TokenType.SEMICOLON);
                    } else if (_currentToken.getTokenType() == TokenType.LEFT_PAREN) {
                        accept(TokenType.LEFT_PAREN);
                        parseArgumentList();
                        accept(TokenType.RIGHT_PAREN);
                        accept(TokenType.SEMICOLON);

                    }
                } else if (_currentToken.getTokenType() == TokenType.IDENTIFIER) { // type
                    accept(TokenType.IDENTIFIER);
                    accept(TokenType.ASSIGNMENT);
                    parseExpression();
                    accept(TokenType.SEMICOLON);
                } else if (_currentToken.getTokenType() == TokenType.ASSIGNMENT) {
                    accept(TokenType.ASSIGNMENT);
                    parseExpression();
                    accept(TokenType.SEMICOLON);
                }
                return;
            default:
                accept(TokenType.LEFT_CURLY, TokenType.RETURN, TokenType.IF, TokenType.WHILE, TokenType.INTEGER, TokenType.BOOLEAN, TokenType.THIS, TokenType.IDENTIFIER);
        }

    }

    private void parseWhileLoop() {
        accept(TokenType.WHILE);
        accept(TokenType.LEFT_PAREN);
        parseExpression();
        accept(TokenType.RIGHT_PAREN);
        parseStatement();
    }

    private void parseIfStatement() {
        accept(TokenType.IF);
        accept(TokenType.LEFT_PAREN);
        parseExpression();
        accept(TokenType.RIGHT_PAREN);
        parseStatement();
        while (_currentToken.getTokenType() == TokenType.ELSE) {
            accept(TokenType.ELSE);
            parseStatement();
        }
        return;
    }


    // This method will accept the token and retrieve the next token.
    //  Can be useful if you want to error check and accept all-in-one.
    private void accept(TokenType expectedType) throws SyntaxError {
        if (_currentToken.getTokenType() == expectedType) {
            _currentToken = _scanner.scan();
            return;
        }

        // TODO: Report an error here.
        //  "Expected token X, but got Y"
        throw new SyntaxError(makeErrorReportString(expectedType, _currentToken.getTokenType()));
    }

    private void accept(TokenType... expectedTypes) throws SyntaxError {
        boolean hasMatched = Arrays.stream(expectedTypes).anyMatch(t -> t == _currentToken.getTokenType());

        if (!hasMatched) {
            throw new SyntaxError(makeErrorReportString(expectedTypes, _currentToken.getTokenType()));
        } else {
            this._currentToken = this._scanner.scan();
        }

    }

    private static String makeErrorReportString(TokenType expected, TokenType actual) {
        return String.format("Expected %s, but was %s.", expected.toString(), actual.toString());
    }

    private static String makeErrorReportString(TokenType[] expected, TokenType actual) {
        return String.format("Expected any of %s, but was %s.", Arrays.toString(expected), actual.toString());
    }

}
