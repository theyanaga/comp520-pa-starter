package miniJava.SyntacticAnalyzer;

// TODO: Enumate the types of tokens we have.
//   Consider taking a look at the terminals in the Grammar.
//   What types of tokens do we want to be able to differentiate between?
//   E.g., I know "class" and "while" will result in different syntax, so
//   it makes sense for those reserved words to be their own token types.
//
// This may result in the question "what doesn't result in different syntax?"
//   By example, if binary operations are always "x binop y"
//   Then it makes sense for -,+,*,etc. to be one TokenType "operator" that can be accepted,
//      (E.g. compare accepting the stream: Expression Operator Expression Semicolon
//       compare against accepting stream: Expression (Plus|Minus|Multiply) Expression Semicolon.)
//   and then in a later assignment, we can peek at the Token's underlying text
//   to differentiate between them.
public enum TokenType {
    CLASS,
    IDENTIFIER,
    LEFT_CURLY, // {
    RIGHT_CURLY, // }
    LEFT_PAREN,  // (
    RIGHT_PAREN, // )
    LEFT_BRACKET,
    RIGHT_BRACKET,
    SEMICOLON,
    COMMA,
    DOT,
    UNDER_SCORE,
    ASSIGNMENT,
    UNARY_OPERATOR,
    BINARY_OPERATOR,
    ACCESS,
    VISIBILITY,
    INT_LITERAL,
    MINUS,
    IF,
    ELSE,
    WHILE,
    THIS,
    BOOL_LITERAL,
    INTEGER,
    BOOLEAN,
    VOID,
    NEW,
    RETURN,
    EOT; // This signals the end.
}
