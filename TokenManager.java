import java.util.LinkedList;
import java.util.Optional;

public class TokenManager {
	private LinkedList<Token> tokenStream;
	
	public TokenManager(LinkedList<Token> inputStream) {
		tokenStream = inputStream;
	}
	
	public Optional<Token> peek(int i) {
		if(i < tokenStream.size() && i >= 0)
			return Optional.of(tokenStream.get(i));
		return null;
	}
	
	public boolean moreTokens() {
		return !tokenStream.isEmpty();
	}
	
	public Optional<Token> matchAndRemove(Token.Tokens tokenType) {
		if(!tokenStream.isEmpty() && tokenStream.getFirst().getTokenType().equals(tokenType)) {
			Token ret = tokenStream.getFirst();
			tokenStream.removeFirst();
			return Optional.of(ret);
		}
		return Optional.empty();
	}
	
	public LinkedList<Token> getTokens(){
		return tokenStream;
	}
	
	public String toString() {
		return tokenStream.toString();
	}
}
