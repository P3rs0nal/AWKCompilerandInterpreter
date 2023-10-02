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
	
	public boolean parseFunction(ProgramNode inp) throws Exception {
		String name;
		LinkedList<String> parameters = new LinkedList<String>();
		LinkedList<StatementNode> statements = new LinkedList<StatementNode>(); 
		
		if(tokenMNG.matchAndRemove(Token.Tokens.FUNCTION).isPresent()) {
			if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WORD)) {
				name = tokenMNG.matchAndRemove(Token.Tokens.WORD).get().getTokenValue();
				if(tokenMNG.matchAndRemove(Token.Tokens.OPENPARENTHSIS).isPresent()) {
					//collect parameters
					while(!tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.CLOSEPARENTHSIS)) {
						if(tokenMNG.matchAndRemove(Token.Tokens.COMMA).isPresent())
							if(parameters.isEmpty())
								throw new Exception("Invalid parameter.");
						acceptSeperators();
						if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WORD)) {
							parameters.add(tokenMNG.matchAndRemove(Token.Tokens.WORD).get().getTokenValue());
							acceptSeperators();
							if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.COMMA)) {
								//dual comma exception
								tokenMNG.matchAndRemove(Token.Tokens.COMMA);
								if(tokenMNG.matchAndRemove(Token.Tokens.COMMA).isPresent())
									throw new Exception("Invalid parameter.");
								acceptSeperators();
							}
							else if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WORD)) {
								// missing comma exception
								throw new Exception("Expected comma.");
							}
						}
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

	public boolean parseAction(ProgramNode inp) throws Exception {
		if(tokenMNG.matchAndRemove(Token.Tokens.BEGIN).isPresent()) {
			inp.setStartBlocks(parseBlock());
			return true;
		}
		else if(tokenMNG.matchAndRemove(Token.Tokens.END).isPresent()) {
			inp.setEndBlocks(parseBlock());
			return true;
		}
		else {
			Optional<Node> operation = parseOperation();
			BlockNode block = parseBlock();
			inp.setBlocks(new BlockNode(block.getStatements(), operation));
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
	
	public Optional<Node> parseBottomLevel() throws Exception{
		while(!tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.SEPARATOR)) {
			if(tokenMNG.peek(0).get().getTokenType().equals((Token.Tokens.STRINGLITERAL)))
				return Optional.of(new ConstantNode(tokenMNG.matchAndRemove(Token.Tokens.STRINGLITERAL).get().getTokenValue()));
			//escape for closing delimiters
			else if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.CLOSEBRACKET) || tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.CLOSEPARENTHSIS))
				break;
			else if(tokenMNG.peek(0).get().getTokenType().equals((Token.Tokens.NUMBER))) 
				return Optional.of(new ConstantNode(tokenMNG.matchAndRemove(Token.Tokens.NUMBER).get().getTokenValue()));
			else if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.PATTERN))
				return Optional.of(new PatternNode(tokenMNG.matchAndRemove(Token.Tokens.PATTERN).get().getTokenValue()));
			else if(tokenMNG.matchAndRemove(Token.Tokens.OPENPARENTHSIS).isPresent())
				return parseOperation();
			else if(tokenMNG.matchAndRemove(Token.Tokens.EXCLAMATION).isPresent()) 
				return Optional.of(new OperationNode(parseOperation(),OperationNode.possibleOperations.NOT));
			else if(tokenMNG.matchAndRemove(Token.Tokens.MINUS).isPresent()) 
				return Optional.of(new OperationNode(parseOperation(),OperationNode.possibleOperations.SUBTRACT));
			else if(tokenMNG.matchAndRemove(Token.Tokens.PLUS).isPresent()) 
				return Optional.of(new OperationNode(parseOperation(),OperationNode.possibleOperations.ADD));
			else if(tokenMNG.matchAndRemove(Token.Tokens.INCREMENT).isPresent()) 
				return Optional.of(new OperationNode(parseOperation(),OperationNode.possibleOperations.PREINCREMENT));
			else if(tokenMNG.matchAndRemove(Token.Tokens.DECREMENT).isPresent()) 
				return Optional.of(new OperationNode(parseOperation(),OperationNode.possibleOperations.PREDECREMENT));
			else
				return parseLValue();
		}
		return Optional.empty();
	}

	public Optional<Node> parseLValue() throws Exception{
		String varName;
		if(tokenMNG.matchAndRemove(Token.Tokens.DOLLARSIGN).isPresent()) {
			return Optional.of(new OperationNode(parseBottomLevel(), OperationNode.possibleOperations.DOLLAR));
		}
		if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WORD)) {
			if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WORD)) {
				varName = tokenMNG.matchAndRemove(Token.Tokens.WORD).get().getTokenValue();
				if(tokenMNG.matchAndRemove(Token.Tokens.OPENBRACKET).isPresent()) {
					VariableReferenceNode retNode = new VariableReferenceNode(varName, parseOperation());
					if(tokenMNG.matchAndRemove(Token.Tokens.CLOSEBRACKET).isPresent()) {
						return Optional.of(retNode);
					}
					else {
						throw new Exception("Missing end bracket");
					}
				}
				else
					return Optional.of(new VariableReferenceNode(varName, parseOperation()));
			}
		}
		return parseBottomLevel();
	}
	
	public Optional<Node> parseOperation() throws Exception{
		return parseBottomLevel();
	}
	
	public LinkedList<Token> getTokens(){
		return tokenMNG.getTokens();
	}
	public String toString() {
		return tokenMNG.toString();
	}
}
