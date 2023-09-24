import java.util.LinkedList;
import java.util.Optional;

public class Parser {
	private TokenManager tokenMNG;
	
	public Parser(LinkedList<Token> tokenStream) {
		tokenMNG = new TokenManager(tokenStream);
	}
	
	public boolean acceptSeperators() {
		boolean seperator = false;
		while(tokenMNG.matchAndRemove(Token.Tokens.SEPARATOR).isPresent()) {
			seperator = true;
		}
		return seperator;
	}
	
	public ProgramNode parse() throws Exception {
		ProgramNode program = new ProgramNode(new LinkedList<FunctionNode>(), new LinkedList<BlockNode>(), new LinkedList<BlockNode>(), new LinkedList<BlockNode>());
		while(tokenMNG.moreTokens()) { 
			if(!parseFunction(program))
				if(!parseAction(program))
					throw new Exception("Error.");
		}
		return program;
	}
	
	public boolean parseFunction(ProgramNode inp) {
		String name;
		LinkedList<String> parameters = new LinkedList<String>();
		LinkedList<StatementNode> statements = new LinkedList<StatementNode>(); 
		
		if(tokenMNG.matchAndRemove(Token.Tokens.FUNCTION).isPresent()) {
			if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WORD)) {
				name = tokenMNG.matchAndRemove(Token.Tokens.WORD).get().getTokenValue();
				if(tokenMNG.matchAndRemove(Token.Tokens.OPENPARENTHSIS).isPresent()) {
					//collect parameters
					while(!tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.CLOSEPARENTHSIS)) {
						tokenMNG.matchAndRemove(Token.Tokens.COMMA).isPresent();
						parameters.add(tokenMNG.matchAndRemove(Token.Tokens.WORD).get().getTokenValue());
					}
					tokenMNG.matchAndRemove(Token.Tokens.CLOSEPARENTHSIS);
					statements.addAll(parseBlock().getStatements());
					FunctionNode function = new FunctionNode(name, parameters, statements);
					inp.setFunctions(function);
					return true;
				}
			}
		}
		return false;
	}

	public boolean parseAction(ProgramNode inp) {
		if(tokenMNG.matchAndRemove(Token.Tokens.BEGIN).isPresent()) {
			inp.setStartBlocks(parseBlock());
			return true;
		}
		else if(tokenMNG.matchAndRemove(Token.Tokens.END).isPresent()) {
			inp.setEndBlocks(parseBlock());
			return true;
		}
		else {
			inp.setBlocks(new BlockNode(new LinkedList<StatementNode>(), parseOperation()));
			return true;
		}
	}
	
	public BlockNode parseBlock() {
		LinkedList<StatementNode> statements = new LinkedList<StatementNode>(); 
		if(tokenMNG.matchAndRemove(Token.Tokens.OPENBRACE).isPresent()) {
			/*given that we currently do not process statements, this would infinitely loop
			 *logically, we would process the statements in the given block and use the end
			 *brace as a conditional to stop
			 */
			//while(!tokenMNG.matchAndRemove(Token.Tokens.CLOSEBRACE).isPresent()) {
				//some function that parses
				statements.add(null);
			//}
		}
		tokenMNG.matchAndRemove(Token.Tokens.CLOSEBRACE);
		acceptSeperators();
		return new BlockNode(statements,null);
	}
	
	public Optional<Node> parseOperation(){
		return Optional.empty();
	}
	
	public LinkedList<Token> getTokens(){
		return tokenMNG.getTokens();
	}
	public String toString() {
		return tokenMNG.toString();
	}
}
