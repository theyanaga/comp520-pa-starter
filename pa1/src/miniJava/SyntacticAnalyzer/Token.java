package miniJava.SyntacticAnalyzer;

public class Token {
	private TokenType _type;
	private String _text;
	
	public Token(TokenType type, String text) {
		this._type = type;
		this._text = text;
	}
	
	public TokenType getTokenType() {
		return this._type;
	}
	
	public String getTokenText() {
		return this._text;
	}
}
