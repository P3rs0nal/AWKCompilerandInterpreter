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
			if(tokenMNG.peek(0).isPresent()) {
				if(tokenMNG.peek(0).get().getTokenType().equals((Token.Tokens.STRINGLITERAL)))
					return Optional.of(new ConstantNode(tokenMNG.matchAndRemove(Token.Tokens.STRINGLITERAL).get().getTokenValue()));
				else if(tokenMNG.peek(0).get().getTokenType().equals((Token.Tokens.NUMBER))) 
					return Optional.of(new ConstantNode(tokenMNG.matchAndRemove(Token.Tokens.NUMBER).get().getTokenValue()));
				else if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.PATTERN))
					return Optional.of(new PatternNode(tokenMNG.matchAndRemove(Token.Tokens.PATTERN).get().getTokenValue()));
				else if(tokenMNG.matchAndRemove(Token.Tokens.OPENPARENTHSIS).isPresent()){
					Optional<Node> parseOperationRes = parseOperation();
					if(tokenMNG.matchAndRemove(Token.Tokens.CLOSEPARENTHSIS).isPresent())
						return parseOperationRes;
					else
						throw new Exception("Missing close )");
				}
				else if(tokenMNG.matchAndRemove(Token.Tokens.EXCLAMATION).isPresent()) 
					return Optional.of(new OperationNode(parseOperation(),OperationNode.possibleOperations.NOT));
				else if(tokenMNG.matchAndRemove(Token.Tokens.MINUS).isPresent()) 
					return Optional.of(new OperationNode(parseOperation(),OperationNode.possibleOperations.UNARYNEGATIVE));
				else if(tokenMNG.matchAndRemove(Token.Tokens.PLUS).isPresent()) 
					return Optional.of(new OperationNode(parseOperation(),OperationNode.possibleOperations.UNARYPOSITIVE));
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
		if(tokenMNG.peek(0).isPresent()) {
			if(tokenMNG.matchAndRemove(Token.Tokens.DOLLARSIGN).isPresent()) {
				Optional<Node> temp = parseBottomLevel();
				if(temp.isPresent())
					return Optional.of(new OperationNode(temp, OperationNode.possibleOperations.DOLLAR));
				else
					return Optional.empty();
			}
			if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WORD)) {
				varName = tokenMNG.matchAndRemove(Token.Tokens.WORD).get().getTokenValue();
				if(tokenMNG.matchAndRemove(Token.Tokens.OPENBRACKET).isPresent()) {
					Optional<Node> parseOperationRes = parseOperation();
					if(tokenMNG.matchAndRemove(Token.Tokens.CLOSEBRACKET).isPresent()) {
						if(parseOperationRes.isPresent())
							return Optional.of(new VariableReferenceNode(varName, parseOperationRes));
						else
							return Optional.of(new VariableReferenceNode(varName));
					}
					else
						throw new Exception("Missing end bracket");
				}
				return Optional.of(new VariableReferenceNode(varName));
			}
		}
		return Optional.empty();
	}
	
	public Optional<Node> parseOperation() throws Exception{
		return parseAssignment();
	}
	
	//right associative
	public Optional<Node> parseAssignment() throws Exception{
		Optional<Node> leftVal = parseTernary();
		Optional<Node> rightVal;
		if(leftVal.isPresent()) {
			if(tokenMNG.matchAndRemove(Token.Tokens.EQUAL).isPresent()) {
				rightVal = parseTernary();
				if(rightVal.isPresent()) {
					return Optional.of(new AssignmentNode(leftVal.get(), rightVal.get()));
				}
				else
					throw new Exception("Missing righthand expression");
			}		
			else if(tokenMNG.matchAndRemove(Token.Tokens.SUBTRACTTO).isPresent()){
				rightVal = parseTernary();
				if(rightVal.isPresent()) {
					return Optional.of(new AssignmentNode(leftVal.get(), new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.SUBTRACT)));
				}
				else
					throw new Exception("Missing righthand expression");
			}
			else if(tokenMNG.matchAndRemove(Token.Tokens.ADDTO).isPresent()){
				rightVal = parseTernary();
				if(rightVal.isPresent()) {
					return Optional.of(new AssignmentNode(leftVal.get(), new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.ADD)));
				}
				else
					throw new Exception("Missing righthand expression");
			}
			else if(tokenMNG.matchAndRemove(Token.Tokens.DIVIDETO).isPresent()){
				rightVal = parseTernary();
				if(rightVal.isPresent()) {
					return Optional.of(new AssignmentNode(leftVal.get(), new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.DIVIDE)));
				}
				else
					throw new Exception("Missing righthand expression");
			}
			else if(tokenMNG.matchAndRemove(Token.Tokens.MULTIPLYTO).isPresent()){
				rightVal = parseTernary();
				if(rightVal.isPresent()) {
					return Optional.of(new AssignmentNode(leftVal.get(), new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.MULTIPLY)));
				}
				else
					throw new Exception("Missing righthand expression");
			}
			else if(tokenMNG.matchAndRemove(Token.Tokens.REMAINDERTO).isPresent()){
				rightVal = parseTernary();
				if(rightVal.isPresent()) {
					return Optional.of(new AssignmentNode(leftVal.get(), new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.MODULO)));
				}
				else
					throw new Exception("Missing righthand expression");
			}
			else if(tokenMNG.matchAndRemove(Token.Tokens.RAISETO).isPresent()){
				rightVal = parseTernary();
				if(rightVal.isPresent()) {
					return Optional.of(new AssignmentNode(leftVal.get(), new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.EXPONENT)));
				}
				else
					throw new Exception("Missing righthand expression");	
			}	
		}
		return leftVal;
	}
	//right associative
	public Optional<Node> parseTernary() throws Exception{
		Optional<Node> left = parseOr();
		if(tokenMNG.matchAndRemove(Token.Tokens.QUESTIONMARK).isEmpty())
			return left;
		Optional<Node> middle = parseTernary(); //accounts for a nested ternary expression
		if(tokenMNG.matchAndRemove(Token.Tokens.COLON).isEmpty())
			throw new Exception("Expected Colon");
		Optional<Node> right = parseTernary(); //accounts for a nested ternary expression
		if(middle.isEmpty() || right.isEmpty()) {
			throw new Exception("Expected Expression");}
		return Optional.of(new TernaryNode(left.get(),middle.get(),right.get()));
	}
	
	//left associative
	public Optional<Node> parseOr() throws Exception{
		Optional<Node> leftVal = parseAnd();
		Optional<Node> rightVal;
		while(true) { // loop for associativity
			if(leftVal.isPresent()) {
				if(tokenMNG.matchAndRemove(Token.Tokens.DOUBLEOR).isPresent()) {
					rightVal = parseOperation();
					if(rightVal.isPresent()) {
						OperationNode op = new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.OR);
						leftVal = Optional.of(op);
					}
					else
						throw new Exception("Missing righthand expression");
				}
			}
			return leftVal;
		}
	}	
	
	//left associative
	public Optional<Node> parseAnd() throws Exception{
		Optional<Node> leftVal = parseArray();
		Optional<Node> rightVal;
		while(true) {// loop for associativity
			if(leftVal.isPresent()) {
				if(tokenMNG.matchAndRemove(Token.Tokens.DOUBLEAND).isPresent()) {
					rightVal = parseOperation();
					if(rightVal.isPresent()) {
						OperationNode op = new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.AND);
						leftVal = Optional.of(op);
					}
					else 
						throw new Exception("Missing righthand expression");
				}
			}
			return leftVal;
		}
	}
	
	//left associative
	public Optional<Node> parseArray() throws Exception {
		Optional<Node> var = parseMatch();
		Optional<Node> exp;
		while(true) {// loop for associativity
			if(var.isPresent()) {
				if(tokenMNG.matchAndRemove(Token.Tokens.IN).isPresent()) {
					exp = parseOperation();
					if(exp.isPresent()) {
						OperationNode op = new OperationNode(var, exp, OperationNode.possibleOperations.IN);
						var = Optional.of(op);
					}
					else
						throw new Exception("Missing righthand expression");	
				}
			}
			return var;
		}
	}
	
	public Optional<Node> parseMatch() throws Exception{
		Optional<Node> leftVal = parseComparison();
		Optional<Node> rightVal;
		if(leftVal.isPresent()) {
			if(tokenMNG.matchAndRemove(Token.Tokens.TILDE).isPresent()) {
				rightVal = parseComparison();
				if(rightVal.isPresent())
					return Optional.of(new OperationNode(leftVal,rightVal, OperationNode.possibleOperations.MATCH));
				else
					throw new Exception("Missing righthand expression");
			}
			if(tokenMNG.matchAndRemove(Token.Tokens.DOESNOTMATCH).isPresent()) {
				rightVal = parseComparison();
				if(rightVal.isPresent())
					return Optional.of(new OperationNode(leftVal,rightVal, OperationNode.possibleOperations.NOTMATCH));
				else
					throw new Exception("Missing righthand expression");
			}
		}
		return leftVal;
	}
	
	public Optional<Node> parseComparison() throws Exception{
		Optional<Node> leftVal = parseConcat();
		Optional<Node> rightVal;
		if(leftVal.isPresent()) {
			if(tokenMNG.matchAndRemove(Token.Tokens.GREATTHANEQUALTO).isPresent()) {
				rightVal = parseConcat();
				if(rightVal.isPresent()) {
					return Optional.of(new OperationNode(leftVal,rightVal,OperationNode.possibleOperations.GREATERTHANEQUAL));
				}
				else
					throw new Exception("Missing righthand expression");
			}
			else if(tokenMNG.matchAndRemove(Token.Tokens.GREATERTHAN).isPresent()) {
				rightVal = parseConcat();
				if(rightVal.isPresent()) {
					return Optional.of(new OperationNode(leftVal,rightVal,OperationNode.possibleOperations.GREATERTHAN));
				}
				else
					throw new Exception("Missing righthand expression");
			}
			else if(tokenMNG.matchAndRemove(Token.Tokens.ISEQUALTO).isPresent()) {
				rightVal = parseConcat();
				if(rightVal.isPresent()) {
					return Optional.of(new OperationNode(leftVal,rightVal,OperationNode.possibleOperations.EQUAL));
				}
				else
					throw new Exception("Missing righthand expression");
			}
			else if(tokenMNG.matchAndRemove(Token.Tokens.NOTEQUALTO).isPresent()) {
				rightVal = parseConcat();
				if(rightVal.isPresent()) {
					return Optional.of(new OperationNode(leftVal,rightVal,OperationNode.possibleOperations.NOTEQUAL));
				}
				else
					throw new Exception("Missing righthand expression");
			}
			else if(tokenMNG.matchAndRemove(Token.Tokens.LESSTHANEQUALTO).isPresent()) {
				rightVal = parseConcat();
				if(rightVal.isPresent()) {
					return Optional.of(new OperationNode(leftVal,rightVal,OperationNode.possibleOperations.LESSTHANEQUAL));
				}
				else
					throw new Exception("Missing righthand expression");
			}
			else if(tokenMNG.matchAndRemove(Token.Tokens.LESSTHAN).isPresent()) {
				rightVal = parseConcat();
				if(rightVal.isPresent()) {
					return Optional.of(new OperationNode(leftVal,rightVal,OperationNode.possibleOperations.LESSTHAN));
				}
				else
					throw new Exception("Missing righthand expression");
			}
		}
		return leftVal;
	}
	
	//left associative
	public Optional<Node> parseConcat() throws Exception{
		Optional<Node> leftExp = parseAddSubtract();
		while(true) {// loop for associativity
			if(leftExp.isPresent()) {
				Optional<Node> rightExp = parseAddSubtract();
				if(rightExp.isPresent()) {
					OperationNode op = new OperationNode(leftExp, rightExp, OperationNode.possibleOperations.CONCATENATION);
					leftExp = Optional.of(op);
				}
				else
					return leftExp;
			}
			else return leftExp;
		}
	}
	
	//left associative
	public Optional<Node> parseAddSubtract() throws Exception{
		Optional<Node> leftVal = parseMulDivMod();
		Optional<Node> rightVal;
		// loop for associativity
		while(true) {
			if(leftVal.isPresent()) {
				if(tokenMNG.matchAndRemove(Token.Tokens.PLUS).isPresent()) {
					rightVal = parseMulDivMod();
					if(rightVal.isPresent()) {
						OperationNode op = new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.ADD);
						leftVal = Optional.of(op);
					}
					else
						throw new Exception("Missing righthand expression");
				}
				else if(tokenMNG.matchAndRemove(Token.Tokens.MINUS).isPresent()) {
					rightVal = parseMulDivMod();
					if(rightVal.isPresent()) {
						OperationNode op = new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.SUBTRACT);
						leftVal = Optional.of(op);
					}
					else
						throw new Exception("Missing righthand expression");
				}
				else
					return leftVal;
			}
			else
				return leftVal;
		}
	}
	
	//left associative
	public Optional<Node> parseMulDivMod() throws Exception{
		Optional<Node> leftVal = parseExponent();
		Optional<Node> rightVal;
		while(true) {// loop for associativity
			if(leftVal.isPresent()) {
				if(tokenMNG.matchAndRemove(Token.Tokens.STAR).isPresent()) {
					rightVal = parseExponent();
					if(rightVal.isPresent()) {
						OperationNode op = new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.MULTIPLY);
						leftVal = Optional.of(op);
					}
					else
						throw new Exception("Missing righthand expression");
				}
				else if(tokenMNG.matchAndRemove(Token.Tokens.FORWARDSLASH).isPresent()) {
					rightVal = parseExponent();
					if(rightVal.isPresent()) {
						OperationNode op = new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.DIVIDE);
						leftVal = Optional.of(op);
					}
					else
						throw new Exception("Missing righthand expression");
				}
				else if(tokenMNG.matchAndRemove(Token.Tokens.MODULO).isPresent()) {
					rightVal = parseExponent();
					if(rightVal.isPresent()) {
						OperationNode op = new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.MODULO);
						leftVal = Optional.of(op);
					}
					else
						throw new Exception("Missing righthand expression");
				}
				else return leftVal;
			}
			else
				return leftVal;
		}
	}
	//right associative
	public Optional<Node> parseExponent() throws Exception{
		Optional<Node> leftVal = parsePost();
		Optional<Node> rightVal;
		if(leftVal.isPresent()) {
			if(tokenMNG.matchAndRemove(Token.Tokens.POWER).isPresent()) {
				rightVal = parseExponent(); //associative call and accounts for nested exponents
				if(rightVal.isPresent())
					return Optional.of(new OperationNode(leftVal, rightVal, OperationNode.possibleOperations.EXPONENT));
				else
					throw new Exception("Missing righthand expression");
			}
		}
		return leftVal;
	}
	
	public Optional<Node> parsePost() throws Exception{
		Optional<Node> temp = parseBottomLevel();
		if(temp.isPresent()) {
			if(tokenMNG.matchAndRemove(Token.Tokens.INCREMENT).isPresent())
				return Optional.of(new OperationNode(temp, OperationNode.possibleOperations.POSTINCREMENT));
			else if(tokenMNG.matchAndRemove(Token.Tokens.DECREMENT).isPresent())
				return Optional.of(new OperationNode(temp, OperationNode.possibleOperations.POSTDECREMENT));
		}
		return temp;
	}
	
	public LinkedList<Token> getTokens(){
		return tokenMNG.getTokens();
	}
	public String toString() {
		return tokenMNG.toString();
	}
}