import java.util.LinkedList;
import java.util.HashMap;

public class Lexer {
	private int line;
	private int charPos;
	private StringHandler strhdlr;
	private LinkedList<Token> tokenCollection = new LinkedList<>();
	private HashMap<String, Token.Tokens> keyWord = new HashMap<>();
	private HashMap<String, Token.Tokens> doubleSymbols = new HashMap<>();
	private HashMap<String, Token.Tokens> singleSymbols = new HashMap<>();
	Lexer(String input){
		strhdlr = new StringHandler(input);
		line = 0;
		charPos = 0;
		populator();
		}
	
	public void populator() {
		keyWord.put("while", Token.Tokens.WHILE);
		keyWord.put("if", Token.Tokens.IF);
		keyWord.put("do", Token.Tokens.DO);
		keyWord.put("for", Token.Tokens.FOR);
		keyWord.put("break", Token.Tokens.BREAK);
		keyWord.put("continue", Token.Tokens.CONTINUE);
		keyWord.put("else", Token.Tokens.ELSE);
		keyWord.put("return", Token.Tokens.RETURN);
		keyWord.put("BEGIN", Token.Tokens.BEGIN);
		keyWord.put("END", Token.Tokens.END);
		keyWord.put("print", Token.Tokens.PRINT);
		keyWord.put("printf", Token.Tokens.PRINTF);
		keyWord.put("next", Token.Tokens.NEXT);
		keyWord.put("in", Token.Tokens.IN);
		keyWord.put("delete", Token.Tokens.DELETE);
		keyWord.put("getline", Token.Tokens.GETLINE);
		keyWord.put("exit", Token.Tokens.EXIT);
		keyWord.put("nextfile", Token.Tokens.NEXTFILE);
		keyWord.put("function", Token.Tokens.FUNCTION);
		singleSymbols.put("{", Token.Tokens.OPENBRACE);
		singleSymbols.put("}", Token.Tokens.CLOSEBRACE);
		singleSymbols.put("[", Token.Tokens.OPENBRACKET);
		singleSymbols.put("]", Token.Tokens.CLOSEBRACKET);
		singleSymbols.put("(", Token.Tokens.OPENPARENTHSIS);
		singleSymbols.put(")", Token.Tokens.CLOSEPARENTHSIS);
		singleSymbols.put("$", Token.Tokens.DOLLARSIGN);
		singleSymbols.put("~", Token.Tokens.TILDE);
		singleSymbols.put("=", Token.Tokens.EQUAL);
		singleSymbols.put("<", Token.Tokens.LESSTHAN);
		singleSymbols.put(">", Token.Tokens.GREATERTHAN);
		singleSymbols.put("!", Token.Tokens.EXCLAMATION);
		singleSymbols.put("+", Token.Tokens.PLUS);
		singleSymbols.put("^", Token.Tokens.POWER);
		singleSymbols.put("-", Token.Tokens.MINUS);
		singleSymbols.put("?", Token.Tokens.QUESTIONMARK);
		singleSymbols.put(":", Token.Tokens.COLON);
		singleSymbols.put("*", Token.Tokens.STAR);
		singleSymbols.put("/", Token.Tokens.FORWARDSLASH);
		singleSymbols.put("%", Token.Tokens.MODULO);
		singleSymbols.put(";", Token.Tokens.SEPARATOR);
		singleSymbols.put("\n", Token.Tokens.SEPARATOR);
		singleSymbols.put("|", Token.Tokens.BAR);
		singleSymbols.put(",", Token.Tokens.COMMA);
		doubleSymbols.put(">=", Token.Tokens.GREATTHANEQUALTO);
		doubleSymbols.put("++", Token.Tokens.INCREMENT);
		doubleSymbols.put("--", Token.Tokens.DECREMENT);
		doubleSymbols.put("<=", Token.Tokens.LESSTHANEQUALTO);
		doubleSymbols.put("==", Token.Tokens.ISEQUALTO);
		doubleSymbols.put("!=", Token.Tokens.NOTEQUALTO);
		doubleSymbols.put("^=", Token.Tokens.RAISETO);
		doubleSymbols.put("%=", Token.Tokens.REMAINDERTO);
		doubleSymbols.put("*=", Token.Tokens.MULTIPLYTO);
		doubleSymbols.put("/=", Token.Tokens.DIVIDETO);
		doubleSymbols.put("+=", Token.Tokens.ADDTO);
		doubleSymbols.put("-=", Token.Tokens.SUBTRACTTO);
		doubleSymbols.put("!~", Token.Tokens.DOESNOTMATCH);
		doubleSymbols.put("&&", Token.Tokens.DOUBLEAND);
		doubleSymbols.put(">>", Token.Tokens.APPENDTO);
		doubleSymbols.put("||", Token.Tokens.DOUBLEOR);
	}
	public void Lex() throws Exception {
		while(!strhdlr.isDone()) {
			char current = strhdlr.peek(charPos);
			if(current == '#') {
				// loop to the end of the line
				while(strhdlr.peek(charPos) != '\n' || strhdlr.isDone()) {
					charPos++;
					strhdlr.swallow(1);
				}
				line++;
			}
			else if(current == '`') {
				tokenCollection.add(handlePatterns());
			}
			else if(current == '"') {
				tokenCollection.add(handleStringLiteral());
			}
			else if(current == ' ' || current == '\t') {
				charPos++;
				strhdlr.swallow(1);
			}
			else if(current == '\n') {
				tokenCollection.add(new Token(Token.Tokens.SEPARATOR, line, charPos));
				line++;
				charPos++;
				strhdlr.swallow(1);
			}
			else if(current == '\r') {
				charPos++;
				strhdlr.swallow(1);		
			}
			else if(Character.isLetter(current)){
				tokenCollection.add(processWord());
			}
			else if(Character.isDigit(current) || current == '.') {
				tokenCollection.add(processNumber());
			}
			else if(doubleSymbols.containsKey(strhdlr.peekString(2)) || singleSymbols.containsKey(strhdlr.peekString(1))) {
				Token symbol = processSymbol();
				if(symbol == null)
					throw new Exception("Unrecognized symbol");
				tokenCollection.add(symbol);
			}
			else {
				throw new Exception("Unexpected character");
			}
		}
		tokenCollection.add(new Token(Token.Tokens.SEPARATOR, line, charPos));
	}
	
	public Token processWord() {
		String word = "";
		while((Character.isLetter(strhdlr.peek(charPos)) || Character.isDigit(strhdlr.peek((charPos))) || strhdlr.peek(charPos) == '_')) {
			word += strhdlr.getChar();
			charPos++;
		}
		if(keyWord.containsKey(word)) {
			Token.Tokens ret = keyWord.get(word);
			return new Token(ret, line, charPos);
			}
		return new Token(Token.Tokens.WORD, word, line, charPos);
	}
	
	public Token processNumber() throws Exception {
		String number = "";
		boolean decimal = false;
		// boolean flag to keep track of multiple decimals
		while(((Character.isDigit(strhdlr.peek(charPos)) || (strhdlr.peek(charPos) == '.' && !decimal)))) {
			if(strhdlr.peek(charPos) == '.')
				decimal = !decimal;
			number += strhdlr.getChar();
			charPos++;
		}
		// will stop lexer from breaking an invalid number into two valid numbers with the 2nd starting from a decimal
		if(strhdlr.peek(charPos) == '.')
			throw new Exception("Unexpected character");
		return new Token(Token.Tokens.NUMBER, number, line, charPos);
	}
	
	public Token handleStringLiteral() throws Exception {
		String literal = "";
		strhdlr.swallow(1);
		charPos++;
		//get rid of start quote
		while(strhdlr.peek(charPos) != '"') {
			if(strhdlr.peek(charPos) == '\\') {
				strhdlr.swallow(1);
				charPos++;
				if(strhdlr.peek(charPos) != '"')
					throw new Exception("Missing quote");
				//get rid of start escape
				while(strhdlr.peek(charPos) != '\\') {
					literal+= strhdlr.getChar();
					charPos++;
				}
				strhdlr.swallow(1);
				charPos++;
				if(strhdlr.peek(charPos) != '"')
					throw new Exception("Missing quote");
				literal += strhdlr.getChar();
				charPos++;
				//get rid of end escape and add quote
			}
			else {
			literal+= strhdlr.getChar();
			charPos++;
			}
		}
		strhdlr.swallow(1);
		charPos++;
		return new Token(Token.Tokens.STRINGLITERAL, literal, line, charPos);
	}
	
	public Token handlePatterns() throws Exception {
		String pattern = "";
		strhdlr.swallow(1);
		charPos++;
		//get rid of start tick
		while(strhdlr.peek(charPos) != '`') {
			try {
			pattern+= strhdlr.getChar();}
			catch(Exception E) {
				throw new Exception("Missing end: `");
			}
			charPos++;
		}
		strhdlr.swallow(1);
		charPos++;
		//get rid of end tick
		return new Token(Token.Tokens.PATTERN, pattern, line, charPos);
	}
	
	public Token processSymbol() throws Exception {
		Token retToken;
		if(doubleSymbols.containsKey(strhdlr.peekString(2))) {
			Token.Tokens tokenTypeValue = doubleSymbols.get(strhdlr.peekString(2));
			retToken = new Token(tokenTypeValue, strhdlr.peekString(2), line, charPos);
			charPos+=2;
			strhdlr.swallow(2);
		}
		else if(singleSymbols.containsKey(strhdlr.peekString(1))) {
			Token.Tokens tokenTypeValue = singleSymbols.get(strhdlr.peekString(1));
			retToken = new Token(tokenTypeValue, strhdlr.peekString(1), line, charPos);
			charPos++;
			strhdlr.swallow(1);
		}
		else
			return null;
		return retToken;
	}
	
	public LinkedList<Token> getTokens(){
		return tokenCollection;
	}
	public String toString() {
		return tokenCollection.toString();
	}
}