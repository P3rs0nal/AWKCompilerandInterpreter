
public class Token {
	enum Tokens{
		WORD, NUMBER, SEPARATOR, 
		WHILE, IF, DO, FOR, BREAK, 
		CONTINUE, ELSE, RETURN, BEGIN, 
		END, PRINT, PRINTF, NEXT, IN, 
		DELETE, GETLINE, EXIT, NEXTFILE, 
		FUNCTION, STRINGLITERAL, PATTERN, 
		GREATTHANEQUALTO, INCREMENT, DECREMENT, 
		LESSTHANEQUALTO, ISEQUALTO, NOTEQUALTO, 
		RAISETO, REMAINDERTO, MULTIPLYTO, DIVIDETO, 
		ADDTO, SUBTRACTTO, DOESNOTMATCH, DOUBLEAND, 
		APPENDTO, DOUBLEOR, OPENBRACE, CLOSEBRACE, 
		OPENBRACKET, CLOSEBRACKET, OPENPARENTHSIS, CLOSEPARENTHSIS,
		DOLLARSIGN, TILDE, EQUAL, LESSTHAN, GREATERTHAN, EXCLAMATION,
		PLUS, POWER, MINUS, QUESTIONMARK, COLON, STAR, FORWARDSLASH,
		MODULO, BAR, COMMA
	}
	private Tokens tokenType;
	private String value;
	private int lineNumber;
	private int startPosition;
	
	// value constructor
	Token(Tokens type, String value, int lineNum, int pos){
		tokenType = type;
		this.value = value;
		lineNum = lineNumber;
		startPosition = pos;
	}
	// non-value constructor
	Token(Tokens type, int lineNum, int pos){
		tokenType = type;
		lineNum = lineNumber;
		startPosition = pos;
	}
	
	public Tokens getTokenType() {
		return tokenType;
	}
	
	public String getTokenValue() {
		return value;
	}
	public String toString() {
		if(tokenType == Tokens.SEPARATOR)
			return "SEPARATOR";
		else if(tokenType == Tokens.NUMBER)
			return "NUMBER (" + value + ")";
		else if(tokenType == Tokens.WORD)
			return "WORD (" + value + ")";
		else if(tokenType == Tokens.STRINGLITERAL)
			return "STRINGLITERAL (" + value + ")";
		else if(tokenType == Tokens.PATTERN)
			return "PATTERN (" + value + ")";
		else
			return tokenType.toString();
	}
}