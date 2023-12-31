import java.util.LinkedList;
import java.util.Optional;

public class Parser {
	private TokenManager tokenMNG;
	
	public Parser(LinkedList<Token> tokenStream) {
		tokenMNG = new TokenManager(tokenStream);
	}
	
	public Parser(Lexer lexer) {
		tokenMNG = new TokenManager(lexer.getTokens());
	}
	
	public boolean acceptSeperators() {
		boolean seperator = false;
		while(tokenMNG.matchAndRemove(Token.Tokens.SEPARATOR).isPresent()) {
			seperator = true;
		}
		return seperator;
	}
	
//	public boolean findMatch(LinkedList<Token> inp) {
//		
//		return true;
//	}
//	
//	public boolean matchers() throws Exception {
//		LinkedList<Token> copy = tokenMNG.getTokens();
//		int para = 0;
//		int bracket = 0;
//		int brace = 0;
//		int lineNumber = 0;
//		int startPos = 0;
//		for(int index = 0; index < copy.size(); index++) {
//			Token token = copy.get(index);
//			if(token.getTokenType().equals(Token.Tokens.OPENPARENTHSIS)) {
//				para++;
//				copy.subList(startPos, index).clear();
//				startPos = token.getPosition();
//				lineNumber = token.getLineNumber();
//				if(!findMatch(copy)) {
//					throw new Exception("Exception at line: " + lineNumber + ", " + startPos);
//				}
//			}//
//			if(token.getTokenType().equals(Token.Tokens.OPENBRACKET)) {
//				bracket++;
//				copy.subList(startPos, index).clear();
//				startPos = token.getPosition();
//				lineNumber = token.getLineNumber();
//				if(!findMatch(copy)) {
//					throw new Exception("Exception at line: " + lineNumber + ", " + startPos);
//				}
//			}
//			if(token.getTokenType().equals(Token.Tokens.OPENBRACE)) {
//				brace++;
//				copy.subList(startPos, index).clear();
//				startPos = token.getPosition();
//				lineNumber = token.getLineNumber();
//				if(!findMatch(copy)) {
//					throw new Exception("Exception at line: " + lineNumber + ", " + startPos);
//				}
//			}
//		}
//		return true;
//	}
//	
//	public boolean matchDelimeters() {
//		LinkedList<Token> tokenCopy = tokenMNG.getTokens();
//		int openPara = 0;
//		int openBracket = 0;
//		int openBrace = 0;
//		int closePara = 0;
//		int closeBracket = 0;
//		int closeBrace = 0;
//		for(Token token : tokenCopy) {
//			if(token.getTokenType().equals(Token.Tokens.OPENPARENTHSIS))
//				openPara++;
//			if(token.getTokenType().equals(Token.Tokens.OPENBRACKET))
//				openBracket++;
//			if(token.getTokenType().equals(Token.Tokens.OPENBRACE))
//				openBrace++;
//			if(token.getTokenType().equals(Token.Tokens.CLOSEPARENTHSIS))
//				closePara++;
//			if(token.getTokenType().equals(Token.Tokens.CLOSEBRACKET))
//				closeBracket++;
//			if(token.getTokenType().equals(Token.Tokens.CLOSEBRACE))
//				closeBrace++;
//		}
//		if(openPara != closePara || openBracket != closeBracket || openBrace != closeBrace)
//			return false;
//		return true;
//	}
	
	public ProgramNode parse() throws Exception {

		ProgramNode program = new ProgramNode(new LinkedList<FunctionNode>(), new LinkedList<BlockNode>(), new LinkedList<BlockNode>(), new LinkedList<BlockNode>());
		// check if there are any unmatched delimeters prior to running
//		if(!matchDelimeters())
//			throw new Exception("Unmatched delimeter");
		while(tokenMNG.moreTokens()) {
			if(!parseFunction(program)) {
				if(!parseAction(program)) {
					throw new Exception("Error.");
					}
				}
		}
		return program;
	}
	
	public boolean parseFunction(ProgramNode inp) throws Exception {
		String name;
		LinkedList<String> parameters = new LinkedList<String>();
		LinkedList<StatementNode> statements = new LinkedList<StatementNode>();
		tokenMNG.matchAndRemove(Token.Tokens.OPENBRACE);
		acceptSeperators();
		if(tokenMNG.matchAndRemove(Token.Tokens.FUNCTION).isPresent()) {
			if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WORD)) {
				name = tokenMNG.matchAndRemove(Token.Tokens.WORD).get().getTokenValue();
				if(tokenMNG.matchAndRemove(Token.Tokens.OPENPARENTHSIS).isPresent()) {
					//collect parameters
					while(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WORD)) {
						acceptSeperators();
						if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WORD)) {
							// case 1 parameter
							if(tokenMNG.peek(1).get().getTokenType().equals(Token.Tokens.CLOSEPARENTHSIS)) {
								parameters.add(tokenMNG.matchAndRemove(Token.Tokens.WORD).get().getTokenValue());
							}
							// case n parameter
							else if(tokenMNG.peek(1).get().getTokenType().equals(Token.Tokens.COMMA)) {
								if(tokenMNG.peek(2).get().getTokenType().equals(Token.Tokens.WORD)) {
									parameters.add(tokenMNG.matchAndRemove(Token.Tokens.WORD).get().getTokenValue());
									tokenMNG.matchAndRemove(Token.Tokens.COMMA);
								}
							}
							else
								throw new Exception("Unexpected character");
						}
					}
					acceptSeperators();
					if(tokenMNG.matchAndRemove(Token.Tokens.CLOSEPARENTHSIS).isEmpty())
						throw new Exception("Expected )");
					acceptSeperators();
					if(tokenMNG.matchAndRemove(Token.Tokens.OPENBRACE).isEmpty())
						throw new Exception("Expected {");
					acceptSeperators();
					while(tokenMNG.matchAndRemove(Token.Tokens.CLOSEBRACE).isEmpty()) {
						statements.addAll(parseBlock().getStatements());
					}
					acceptSeperators();
					FunctionNode function = new FunctionNode(name, parameters, statements);
					inp.setFunctions(function);
					tokenMNG.matchAndRemove(Token.Tokens.CLOSEBRACE);
					return true;
				}
				else 
					throw new Exception("Expected (");
			}
			else
				throw new Exception("Missing function name");
		}
		
		return false;
	}

	public boolean parseAction(ProgramNode inp) throws Exception {
		//System.out.println(tokenMNG.getTokens());
		if(tokenMNG.matchAndRemove(Token.Tokens.BEGIN).isPresent()) {
			tokenMNG.matchAndRemove(Token.Tokens.OPENBRACE);
			acceptSeperators();
			inp.setStartBlocks(parseBlock());
			return true;
		}
		else if(tokenMNG.matchAndRemove(Token.Tokens.END).isPresent()) {
			tokenMNG.matchAndRemove(Token.Tokens.OPENBRACE);
			acceptSeperators();
			inp.setEndBlocks(parseBlock());
			acceptSeperators();
			return true;
		}
		else {
			BlockNode block = parseBlock();
			Optional<Node> operation = parseOperation();
			tokenMNG.matchAndRemove(Token.Tokens.CLOSEBRACE);	
			acceptSeperators();
			inp.setBlocks(new BlockNode(block.getStatements(), operation));
			return true;
		}
	}
	
	public BlockNode parseBlock() throws Exception {
		LinkedList<StatementNode> statements = new LinkedList<StatementNode>();
		// multiline block
		acceptSeperators();
		//System.out.println(tokenMNG.getTokens());
		if(tokenMNG.matchAndRemove(Token.Tokens.OPENBRACE).isPresent()) {
			acceptSeperators();
			//System.out.println(tokenMNG.getTokens());

			while(!tokenMNG.matchAndRemove(Token.Tokens.CLOSEBRACE).isPresent()) {
				//System.out.println(tokenMNG.getTokens());

				acceptSeperators();
				var statement = parseStatement();
//				System.out.println(statement);
//				System.out.println(tokenMNG.getTokens());
				if(statement == null) {
					if(tokenMNG.matchAndRemove(Token.Tokens.CLOSEBRACE).isEmpty())
						throw new Exception("Missing block }");
					}
				statements.add(statement);
				acceptSeperators();
			}
			acceptSeperators();
		}
		// singleline block
		else {
//			System.out.println(tokenMNG.getTokens());
			tokenMNG.matchAndRemove(Token.Tokens.CLOSEBRACE);
			acceptSeperators();
			tokenMNG.matchAndRemove(Token.Tokens.CLOSEBRACE);
			acceptSeperators();
			if(tokenMNG.moreTokens()) {
				acceptSeperators();
				tokenMNG.matchAndRemove(Token.Tokens.OPENBRACE);
				var statement = parseStatement();
				if(statement == null)
					throw new Exception("Missing statement");
				statements.add(statement);
				acceptSeperators();
			}
		}
		return new BlockNode(statements,null);
	}
	
	public StatementNode parseStatement() throws Exception{
		acceptSeperators();
		if(tokenMNG.peek(0) == null) {
			return null;
		}
		if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.IF)) {
			return parseIf();
		}
		else if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.CONTINUE)) {
			return parseContinue();
		}
		else if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.BREAK)) {
			return parseBreak();
		}
		else if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.FOR)) {
			 return parseFor();
		}
		else if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.DELETE)) {
			return parseDelete();
			}
		else if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WHILE)) {
			return parseWhile();
		}
		else if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.DO)) {
			return parseDoWhile();
		}
		else if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.RETURN)) {
			return parseReturn();
		}
		else {
			acceptSeperators();
			Optional<Node> parseOperationResult = parseOperation();
			if(parseOperationResult.isEmpty()) {
				return null;
			}
			if(parseOperationResult.get() instanceof AssignmentNode) {
				return (StatementNode) parseOperationResult.get();
			}
			else if(parseOperationResult.get() instanceof ParseFunctionCallNode) {
				return (StatementNode) parseOperationResult.get();
			}
			else if(parseOperationResult.get() instanceof OperationNode) {
				return (StatementNode) parseOperationResult.get();
			}
		}
		acceptSeperators();
		return null;
	}
	
	public StatementNode parseFunctionCall() throws Exception {
		LinkedList<Node> parameters = new LinkedList<Node>();
		if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.WORD) && tokenMNG.peek(1).get().getTokenType().equals(Token.Tokens.OPENPARENTHSIS)) {
			String functionName = tokenMNG.matchAndRemove(Token.Tokens.WORD).get().getTokenValue();
			if(tokenMNG.matchAndRemove(Token.Tokens.OPENPARENTHSIS).isPresent()) {
				while(!tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.CLOSEPARENTHSIS)) {
					parameters.add(parseOperation().get());
					if(!tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.COMMA)) {
						if(!tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.CLOSEPARENTHSIS)) {
							throw new Exception("Expecting )");
						}
					}
					else
						tokenMNG.matchAndRemove(Token.Tokens.COMMA);
				}
				tokenMNG.matchAndRemove(Token.Tokens.CLOSEPARENTHSIS);
				return new ParseFunctionCallNode(functionName, parameters);
			}
			else 
				throw new Exception("Expected (");
		}
		if(tokenMNG.matchAndRemove(Token.Tokens.GETLINE).isPresent()) {
			Optional<Node> parseOpResult = parseOperation();
			if(parseOpResult.isPresent())
				parameters.add(parseOpResult.get());
			return new ParseFunctionCallNode("getline", parameters);
		}
		if(tokenMNG.matchAndRemove(Token.Tokens.PRINT).isPresent()) {
			Optional<Node> parseOperationResult = parseOperation();
			while(parseOperationResult.isPresent()) {
				parameters.add(parseOperationResult.get());
				if(tokenMNG.matchAndRemove(Token.Tokens.COMMA).isEmpty())
					return new ParseFunctionCallNode("print", parameters);
				parseOperationResult = parseOperation();
			}
			return new ParseFunctionCallNode("print", parameters);
		}
		if(tokenMNG.matchAndRemove(Token.Tokens.PRINTF).isPresent()) {
			Optional<Node> parseOperationResult = parseOperation();
			while(parseOperationResult.isPresent()) {
				parameters.add(parseOperationResult.get());
				if(tokenMNG.matchAndRemove(Token.Tokens.COMMA).isEmpty())
					return new ParseFunctionCallNode("printf", parameters);
				parseOperationResult = parseOperation();
			}
			return new ParseFunctionCallNode("printf", parameters);
		}
		if(tokenMNG.matchAndRemove(Token.Tokens.EXIT).isPresent()) {
			Optional<Node> parseOperationResult = parseOperation();
			if(parseOperationResult.isPresent())
				parameters.add(parseOperationResult.get());
			return new ParseFunctionCallNode("exit", parameters);
		}
		if(tokenMNG.matchAndRemove(Token.Tokens.NEXTFILE).isPresent()) {
			return new ParseFunctionCallNode("nextfile", null);
		}
		if(tokenMNG.matchAndRemove(Token.Tokens.NEXT).isPresent()) {
			return new ParseFunctionCallNode("next", null);
		}
		return null;
	}
	
	public StatementNode parseBreak() throws Exception{
		tokenMNG.matchAndRemove(Token.Tokens.BREAK);
		return new ParseBreakNode();
	}
	
	public StatementNode parseIf() throws Exception{
		if(tokenMNG.matchAndRemove(Token.Tokens.IF).isPresent()) {
			if(tokenMNG.matchAndRemove(Token.Tokens.OPENPARENTHSIS).isPresent()) {
				Optional<Node> ifCondition = parseOperation();
				if(ifCondition.isEmpty())
					throw new Exception("Expected condition");
				if(tokenMNG.matchAndRemove(Token.Tokens.CLOSEPARENTHSIS).isPresent()) {
					Node statements = parseBlock();
					if(tokenMNG.matchAndRemove(Token.Tokens.ELSE).isPresent()) {
						if(tokenMNG.peek(0).get().getTokenType().equals(Token.Tokens.IF)) {
							ParseIfNode nestedIfNode = (ParseIfNode) parseIf();
							return new ParseIfNode(ifCondition.get(),statements,nestedIfNode);
						}
						BlockNode elseBlockStatements = parseBlock();
						ParseIfNode elseNoIfNode = new ParseIfNode(null,elseBlockStatements);
						return new ParseIfNode(ifCondition.get(),statements, elseNoIfNode);
					}
					else
						return new ParseIfNode(ifCondition.get(),statements);
				}
				else
					throw new Exception("Missing righthand )");
			}
			else
				throw new Exception("Expected (");
		}
		return null;
	}
	
	public StatementNode parseFor() throws Exception{
		if(tokenMNG.matchAndRemove(Token.Tokens.FOR).isPresent()) {
			if(tokenMNG.matchAndRemove(Token.Tokens.OPENPARENTHSIS).isPresent()) {
				//check if forInLoop
				if(tokenMNG.peek(1).get().getTokenType().equals(Token.Tokens.IN)) {
					Optional<Node> forInCondition = parseOperation();
					if(tokenMNG.matchAndRemove(Token.Tokens.CLOSEPARENTHSIS).isPresent()) {
						Optional<Node> body = Optional.of(parseBlock());
						return new ParseForEachNode(forInCondition.get(), body.get());
					}
					else
						throw new Exception("Missing )");
				}
				//regular for loop
				else {
					Optional<Node> forCondition = parseOperation();
					acceptSeperators();
					Optional<Node> secondForCondition = parseOperation();
					acceptSeperators();
					Optional<Node> thirdForCondition = parseOperation();
					if(forCondition.isEmpty() || secondForCondition.isEmpty() || thirdForCondition.isEmpty())
						throw new Exception("Missing condition");
				
					if(tokenMNG.matchAndRemove(Token.Tokens.CLOSEPARENTHSIS).isPresent()) {
						Optional<Node> body = Optional.of(parseBlock());
						return new ParseForNode(forCondition, secondForCondition, thirdForCondition, body);
					}
					else
						throw new Exception("Missing righthand )");
				}
			}
			else
				throw new Exception("Expected (");
		}
		return null;	
	}
	
	public StatementNode parseDelete() throws Exception{
		if(tokenMNG.matchAndRemove(Token.Tokens.DELETE).isPresent()) {
			Optional<Node> deletionContent = parseLValue();
			if(deletionContent.isEmpty())
				throw new Exception("Expected condition");
			return new ParseDeleteNode(deletionContent);
		}
		return null;	
	}
	
	public StatementNode parseWhile() throws Exception{
		if(tokenMNG.matchAndRemove(Token.Tokens.WHILE).isPresent()) {
			if(tokenMNG.matchAndRemove(Token.Tokens.OPENPARENTHSIS).isPresent()) {
				Optional<Node> whileCondition = parseOperation();
				if(whileCondition.isEmpty())
					throw new Exception("Expected condition");
				if(tokenMNG.matchAndRemove(Token.Tokens.CLOSEPARENTHSIS).isPresent()) {
					Optional<Node> body = Optional.of(parseBlock());
					return new ParseWhileNode(whileCondition,body);
				}
				else
					throw new Exception("Missing righthand )");
			}
			else
				throw new Exception("Expected (");
		}
		return null;
	}
	
	public StatementNode parseDoWhile() throws Exception{
		if(tokenMNG.matchAndRemove(Token.Tokens.DO).isPresent()) {
				Optional<Node> body = Optional.of(parseBlock());
				if(tokenMNG.matchAndRemove(Token.Tokens.WHILE).isPresent()) {
					if(tokenMNG.matchAndRemove(Token.Tokens.OPENPARENTHSIS).isPresent()) {
						Optional<Node> whileCondition = parseOperation();
						if(whileCondition.isEmpty())
							throw new Exception("Missing condition");
						if(!tokenMNG.matchAndRemove(Token.Tokens.CLOSEPARENTHSIS).isPresent())
							throw new Exception("Missing )");
						return new ParseDoWhileNode(whileCondition, body);
					}
					else
						throw new Exception("Missing (");
				}
		}
		return null;
	}
	
	public StatementNode parseReturn() throws Exception{
		if(tokenMNG.matchAndRemove(Token.Tokens.RETURN).isPresent()) {
			ParseReturnNode returnNode = new ParseReturnNode(parseOperation());
			return returnNode;
		}
		return null;
	}
	
	public StatementNode parseContinue(){
		if(tokenMNG.matchAndRemove(Token.Tokens.CONTINUE).isPresent())
			return new ParseContinueNode();
		return null;
	}
		
	public Optional<Node> parseBottomLevel() throws Exception{
		if(tokenMNG.peek(0) != null) {
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
			else {
				Node functionCallResult = parseFunctionCall();
				if(functionCallResult != null)
					return Optional.of(functionCallResult);
				return parseLValue();
				}
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
		Optional<Node> middle = parseOperation(); //accounts for a nested ternary expression
		if(tokenMNG.matchAndRemove(Token.Tokens.COLON).isEmpty())
			throw new Exception("Expected Colon");
		Optional<Node> right = parseOperation(); //accounts for a nested ternary expression
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