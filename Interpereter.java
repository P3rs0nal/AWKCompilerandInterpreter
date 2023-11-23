import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.*;

public class Interpereter {
	private HashMap<String, InterpreterDataType> globalVars = new HashMap<String, InterpreterDataType>();
	private HashMap<String, FunctionNode> functionCalls = new HashMap<String, FunctionNode>();
	private lineManager lineMNG;
	public ProgramNode program;
	
	public class lineManager{
		List<String> readInInputFile;
		
		public lineManager(List<String> readInInputFile) {
			this.readInInputFile = readInInputFile;
		}
		
		public boolean splitAndAssign() {
			String[] words = null;
			try {
				words = readInInputFile.get(Integer.valueOf(globalVars.get("NR").getType())).split(globalVars.get("FS").getType());
			}
			catch (Exception E) {
			}
			if(words == null)
				return false;
			if(words.length == 0)
				return false;
			int nr = Integer.valueOf(globalVars.get("NR").getType());// update NR
			nr++;
			globalVars.put("NR", new InterpreterDataType(Integer.toString(nr)));
			int nfr = Integer.valueOf(globalVars.get("NFR").getType()); // update NFR same way
			nfr++;
			globalVars.put("NFR", new InterpreterDataType(Integer.toString(nfr)));
			// loop & assign
			for(int index = 0; index < words.length; index++) {
				globalVars.put("$" + index , new InterpreterDataType(words[index]));
			}
			return true;
		}
	}
	
	public Interpereter(ProgramNode program, Path filePath) throws IOException {
		this.program = program;
		globalVars.put("NR", new InterpreterDataType("0"));
		globalVars.put("NFR", new InterpreterDataType("0"));
		globalVars.put("OMFT", new InterpreterDataType("%.6g"));
		globalVars.put("OFS", new InterpreterDataType(" "));
		globalVars.put("ORS", new InterpreterDataType("\n"));
		globalVars.put("FS", new InterpreterDataType(" "));
		if(filePath == null) {
			lineMNG = new lineManager(null);
		}
		else {
			lineMNG = new lineManager(Files.readAllLines(filePath));
			lineMNG.splitAndAssign();
			globalVars.put(filePath.toString(), null);
		}
		if(program.getFunctions() != null)
			for(FunctionNode function : program.getFunctions()) {
				functionCalls.put(function.getName(), function);
			}
		
		functionCalls.put("print", new BuiltInFunctionNode(input -> {
			InterpreterArrayDataType ret1 = (InterpreterArrayDataType) input.get("print");
			for(InterpreterDataType value : ret1.getVariables().values()) {
				if(value == null)
					break;
				else
					System.out.println(value); 
			}
			return "0";
			}, true));
		
		functionCalls.put("printf", new BuiltInFunctionNode(input -> {
			InterpreterDataType ret1 = input.get("printf");
			for(InterpreterDataType value :  ((InterpreterArrayDataType) ret1).getVariables().values()) {
				if(value == null)
					break;
				else
					System.out.println(value); 
			}
			return "0";
			}, true));
		
		functionCalls.put("getline", new BuiltInFunctionNode(input -> {
			lineMNG.splitAndAssign();
			return "0";
			}, true));
		
		functionCalls.put("next", new BuiltInFunctionNode(input -> {
			lineMNG.splitAndAssign(); 
			return "0";
			}, true));
		
		functionCalls.put("gsub", new BuiltInFunctionNode(input -> {
			InterpreterDataType regExp = input.get("gsubRegExp");
			InterpreterDataType target = input.get("gsubTarget");
			InterpreterDataType replacement = input.get("gsubReplacement");
			Pattern pattern = Pattern.compile(regExp.getType());
			Matcher matcher = pattern.matcher(target.getType());
			int matches = 0;
			if(matcher.find()) {
				matches = (int) matcher.results().count();
				matcher.replaceAll(replacement.getType());
			}
			return String.valueOf(matches);
			}, true));
		
		functionCalls.put("index", new BuiltInFunctionNode(input -> {
			InterpreterDataType myString = input.get("index");
			InterpreterDataType searchingFor = input.get("searchFunction");
			String ret = "";
			String search = "";
			if(globalVars.containsKey("index"))
				ret = globalVars.get("index").getType();
			else
				ret = myString.getType();
			if(globalVars.containsKey("searchFunction"))
				search = globalVars.get("searchFunction").getType();
			else
				search = searchingFor.getType();
			return String.valueOf(ret.indexOf(search));
			}, true));
		
		functionCalls.put("length", new BuiltInFunctionNode(input -> {
			InterpreterArrayDataType val = (InterpreterArrayDataType) input.get("length");
			return String.valueOf(val.getVariables().get("1").getType().length());
			}, true));

		functionCalls.put("match", new BuiltInFunctionNode(input -> {
			InterpreterDataType regExp = input.get("regExp");
			InterpreterDataType string = input.get("string");
			InterpreterArrayDataType arr = (InterpreterArrayDataType) input.get("optionalArray");
			String reg = regExp.getType();
			String str = "";
			HashMap<String, InterpreterDataType> optionalArr = new HashMap<String, InterpreterDataType>();
			try {
				optionalArr = arr.getVariables();}
			catch(Exception E) {
				
			}
			if(globalVars.containsKey("string"))
				str = globalVars.get("string").getType();
			else
				str = string.getType();
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(str);
			boolean ret = matcher.find();
			optionalArr.clear();
			try {
			optionalArr.put("0", new InterpreterDataType(matcher.group()));
			globalVars.put(arr.getType(), new InterpreterDataType(matcher.group()));
			}
			catch(Exception E) {
			}
			if(ret)
				return String.valueOf(matcher.start());
			return "0";
			}, true));
		
		functionCalls.put("split", new BuiltInFunctionNode(input -> {
			String string = input.get("targetString").getType();
			String separator = input.get("seperator").getType();
			HashMap<String, InterpreterDataType> array = ((InterpreterArrayDataType) input.get("arrayIADT")).getVariables();
			if(globalVars.containsKey("targetString"))
				string = globalVars.get("targetString").getType();
			if(separator == null)
				separator = globalVars.get("FS").getType();
			String[] splitArray = string.split(separator);
			for(String sub : splitArray) {
				array.put(sub, new InterpreterDataType(sub));
			}
			return "0";
			}, true));
		
		functionCalls.put("sub", new BuiltInFunctionNode(input -> {
			InterpreterArrayDataType myString = (InterpreterArrayDataType) input.get("sub");
			InterpreterDataType firstIndex = myString.getVariables().get("2");
			InterpreterDataType secondIndex = null;
			try {
			secondIndex = myString.getVariables().get("3");
			}
			catch(Exception E) {
				secondIndex = new InterpreterDataType("$0");
			}
			String ret = myString.getVariables().get("1").getType();
			if(secondIndex == null) {
				return String.valueOf(ret.substring(Integer.parseInt(firstIndex.getType())));
			}
			else {
				if(Integer.parseInt(firstIndex.getType())+Integer.parseInt(secondIndex.getType()) < ret.length()) {
					return String.valueOf(ret.substring(Integer.parseInt(firstIndex.getType()), Integer.parseInt(secondIndex.getType())));
				}
				else
					return null;
			}
			}, true));
		
		functionCalls.put("substr", new BuiltInFunctionNode(input -> {
			InterpreterDataType myString = input.get("subString");
			InterpreterDataType firstIndex = input.get("indexOne");
			InterpreterDataType secondIndex = input.get("indexTwo");
			String ret = "";
			String indexOne = "";
			String indexTwo = "";
			if(globalVars.containsKey("subString"))
				ret = globalVars.get("subString").getType();
			else
				ret = myString.getType();
			if(globalVars.containsKey("indexOne"))
				indexOne = globalVars.get("indexOne").getType();
			else
				indexOne = firstIndex.getType();
			if(globalVars.containsKey("indexTwo"))
				indexTwo = globalVars.get("indexTwo").getType();
			else
				indexTwo = secondIndex.getType();
			if(indexTwo == "")
				return String.valueOf(ret.substring(Integer.parseInt(indexOne)));
			return String.valueOf(ret.substring(Integer.parseInt(indexOne), Integer.parseInt(indexTwo)));
			}, true));
		
		functionCalls.put("tolower", new BuiltInFunctionNode(input -> {
			InterpreterDataType val = input.get("tolower");
			String ret = "";
			if(globalVars.containsKey("tolower"))
				ret = globalVars.get("tolower").getType();
			else
				ret = val.getType();
			return String.valueOf(ret.toLowerCase());
			}, true));
		
		functionCalls.put("toupper", new BuiltInFunctionNode(input -> {
			InterpreterDataType val = input.get("toupper");
			String ret = "";
			if(globalVars.containsKey("toupper"))
				ret = globalVars.get("toupper").getType();
			else
				ret = val.getType();
			return String.valueOf(ret.toUpperCase());
			}, true));
	}
	
	public HashMap<String, FunctionNode> getFunctions(){
		return functionCalls;
	}
	
	public HashMap<String, InterpreterDataType> getVariables(){
		return globalVars;
	}
	
	public InterpreterDataType getIDT(Node inputNode, HashMap<String, InterpreterDataType> localVars) throws Exception {
		if(inputNode instanceof AssignmentNode) {
			var name = ((AssignmentNode) inputNode).target;
			var exp = ((AssignmentNode) inputNode).expression;
			if(name instanceof VariableReferenceNode) {
				var expEval = getIDT(exp, localVars);
				globalVars.put(String.valueOf(name), expEval);
				return expEval;
			}
			else if(name instanceof OperationNode && ((OperationNode) name).operator.equals(OperationNode.possibleOperations.DOLLAR)){
					var assignment = getIDT(exp, localVars);
					globalVars.put("$" + name, assignment);
					return assignment;
			}
		}
		else if(inputNode instanceof VariableReferenceNode) {
			var target = ((VariableReferenceNode) inputNode).getName();
			var index = ((VariableReferenceNode) inputNode).getExpression();
			if(globalVars.containsKey(target)) {
				if(globalVars.get(target) instanceof InterpreterArrayDataType) {
					if(!index.equals(Optional.empty()))// return value at index
						return new InterpreterDataType(((InterpreterArrayDataType) globalVars.get(target)).getVariables().get((getIDT(index.get(), localVars)).getType()).getType());
					return globalVars.get(target); // return array reference
				}
				else {
					return new InterpreterDataType(globalVars.get(target).getType());
				}
			}
			else if(index == null) { // not an array
				if(globalVars.containsKey(target)) // var is initialized
					return new InterpreterDataType(globalVars.get(target).getType());
				else //initialize var to empty
					globalVars.put(target, new InterpreterDataType(""));
				return new InterpreterDataType(globalVars.get(target).getType());
			}
		}
		else if(inputNode instanceof ConstantNode) {
			return new InterpreterDataType(((ConstantNode) inputNode).getName());
		}
		else if(inputNode instanceof ParseFunctionCallNode) {
			//System.out.println(inputNode + " ehere");
			return new InterpreterDataType(runFunctionCall((ParseFunctionCallNode)inputNode, localVars));
		}
		else if(inputNode instanceof PatternNode) {
			throw new Exception("Cannot pass pattern to a function");
		}
		else if(inputNode instanceof TernaryNode) {
			Node left = ((TernaryNode) inputNode).left;
			Node center = ((TernaryNode) inputNode).center;
			Node right = ((TernaryNode) inputNode).right;
			if(getIDT(left,localVars).getType().compareTo("true") == 0  || getIDT(left,localVars).getType().compareTo("0") == 0) { //true
				return new InterpreterDataType(getIDT(center,localVars).getType());
				}
			else
				return new InterpreterDataType(getIDT(right,localVars).getType());
		}
		else if(inputNode instanceof OperationNode) {
			InterpreterDataType left = null;
			InterpreterDataType right = null;
			try {
				left = getIDT(((OperationNode) inputNode).getLeft().get(), localVars);
				right = getIDT(((OperationNode) inputNode).getRight().get(), localVars);
			}
			catch(Exception E) {
				left = getIDT(((OperationNode) inputNode).getLeft().get(), localVars);
				try {
					if(((OperationNode) inputNode).getRight().get() instanceof PatternNode) {
						right = new InterpreterDataType(((PatternNode)((OperationNode) inputNode).getRight().get()).getPattern());
					}
					else
						right = null;
					}
				catch(Exception B) { //case right doesn't exist
					left = getIDT(((OperationNode) inputNode).getLeft().get(), localVars);
					right = null;
				}
			}
			OperationNode.possibleOperations operator = ((OperationNode) inputNode).operator;
			if(right != null){
				try { //case where both values are numeric
					float lefthandValue = Float.parseFloat(left.getType());
					float righthandValue = Float.parseFloat(right.getType());
					switch(operator) {
					case AND:
						if(lefthandValue == 0 || righthandValue == 0)
							return new InterpreterDataType("0");
						return new InterpreterDataType("1");
					case OR:
						if(lefthandValue == 0 && righthandValue == 0)
							return new InterpreterDataType("0");
						return new InterpreterDataType("1");
					case ADD:
						return new InterpreterDataType(String.valueOf(lefthandValue + righthandValue));
					case DIVIDE:
						if(right.getType().compareTo("0") == 0)
							throw new Exception("Division by 0");
						return new InterpreterDataType(String.valueOf(lefthandValue / righthandValue));
					case EQUAL:
						return new InterpreterDataType(String.valueOf(lefthandValue == righthandValue));
					case EXPONENT:
						return new InterpreterDataType(String.valueOf(Math.pow(lefthandValue, righthandValue)));
					case GREATERTHAN:
						return new InterpreterDataType(String.valueOf(lefthandValue > righthandValue));
					case GREATERTHANEQUAL:
						return new InterpreterDataType(String.valueOf(lefthandValue >= righthandValue));
					case LESSTHAN:
						return new InterpreterDataType(String.valueOf(lefthandValue < righthandValue));
					case LESSTHANEQUAL:
						return new InterpreterDataType(String.valueOf(lefthandValue <= righthandValue));
					case MODULO:
						if(right.getType().compareTo("0") == 0)
							throw new Exception("Division by 0");
						return new InterpreterDataType(String.valueOf(lefthandValue % righthandValue));
					case MULTIPLY:
						return new InterpreterDataType(String.valueOf(lefthandValue * righthandValue));
					case NOTEQUAL:
						return new InterpreterDataType(String.valueOf(lefthandValue != righthandValue));
					case SUBTRACT:
						return new InterpreterDataType(String.valueOf(lefthandValue - righthandValue));
					default:
						break;
					}
				}
				catch(Exception E) { //case where either 1 or both values are not numeric
					String lefthandValue = left.getType();
					String righthandValue = right.getType();
					switch(operator) {
					case AND:
						if(lefthandValue.compareTo("false")==0 || righthandValue.compareTo("false")==0 )
							return new InterpreterDataType("0");
						return new InterpreterDataType("1");
					case OR:
						if(lefthandValue.compareTo("false")==0 && righthandValue.compareTo("false")==0 )
							return new InterpreterDataType("0");
						return new InterpreterDataType("1");
					case CONCATENATION:
						return new InterpreterDataType(String.valueOf(lefthandValue + righthandValue));
					case EQUAL:
						return new InterpreterDataType(String.valueOf(lefthandValue.compareTo(righthandValue)));
					case GREATERTHAN:
						return new InterpreterDataType(String.valueOf(lefthandValue.compareTo(righthandValue)>0));
					case GREATERTHANEQUAL:
						return new InterpreterDataType(String.valueOf(lefthandValue.compareTo(righthandValue)>=0));
					case IN:
						if(((OperationNode) inputNode).getRight().get() instanceof VariableReferenceNode) {
							if(right instanceof InterpreterArrayDataType)
								return new InterpreterDataType(String.valueOf(((InterpreterArrayDataType) right).getVariables().containsKey(lefthandValue)));
						}
						else
							throw new Exception("Invalid reference");
					case LESSTHAN:
						return new InterpreterDataType(String.valueOf(lefthandValue.compareTo(righthandValue)<0));
					case LESSTHANEQUAL:
						return new InterpreterDataType(String.valueOf(lefthandValue.compareTo(righthandValue)<=0));
					case MATCH:
						Pattern matchPattern = Pattern.compile((((OperationNode)inputNode).getRight().get()).toString());
						Matcher retVal = matchPattern.matcher(lefthandValue);
						return new InterpreterDataType(String.valueOf(retVal.find()));
					case NOTEQUAL:
						return new InterpreterDataType(String.valueOf(lefthandValue.compareTo(righthandValue)!=0));
					case NOTMATCH:
						Pattern notMatchPattern = Pattern.compile((((OperationNode)inputNode).getRight().get()).toString());
						Matcher retNotVal = notMatchPattern.matcher(lefthandValue);
						return new InterpreterDataType(String.valueOf(!retNotVal.find()));
					default:
						break;
					}
				}
			}
			else { //right isn't provided
				try { //value is numeric
					float lefthandValue = Float.parseFloat(left.getType());
					//System.out.println("HERERERERERE " + lefthandValue);
					switch(operator) {
					case DOLLAR:
						if(globalVars.containsKey("$" + (String.valueOf((lefthandValue)).substring(String.valueOf(lefthandValue).length()-1,String.valueOf(lefthandValue).length())))) {
							return new InterpreterDataType(globalVars.get("$" + (String.valueOf((lefthandValue)).substring(String.valueOf(lefthandValue).length()-1,String.valueOf(lefthandValue).length()))).getType());
						}
						else{
							globalVars.put("$" + (String.valueOf((lefthandValue)).substring(String.valueOf(lefthandValue).length()-1,String.valueOf(lefthandValue).length())), new InterpreterDataType(""));
							return new InterpreterDataType(globalVars.get("$" + (String.valueOf((lefthandValue)).substring(String.valueOf(lefthandValue).length()-1,String.valueOf(lefthandValue).length()))).getType());
						}
					case POSTDECREMENT:
						return new InterpreterDataType(String.valueOf(lefthandValue--));
					case POSTINCREMENT:
						return new InterpreterDataType(String.valueOf(lefthandValue++));
					case PREDECREMENT:
						return new InterpreterDataType(String.valueOf(--lefthandValue));
					case PREINCREMENT:
						return new InterpreterDataType(String.valueOf(++lefthandValue));
					case UNARYNEGATIVE:
						return new InterpreterDataType(String.valueOf(Math.abs(lefthandValue)*(-1)));
					case UNARYPOSITIVE:
						return new InterpreterDataType(String.valueOf(Math.abs(lefthandValue)));
					default:
						break;
					}
				}
				catch(Exception E){ //non numeric single operation value
					String lefthandValue = left.getType();
					switch(operator) {
					case NOT:
						if(lefthandValue.compareTo("true")==0)
							return new InterpreterDataType("false");
						else
							return new InterpreterDataType("true");
					default:
						break;
					}
				}
			}
		}
		else
			return null;
		return null;
	}
	
	public ReturnType processStatement(HashMap<String, InterpreterDataType> localVars, StatementNode statement) throws Exception {
		if(statement instanceof AssignmentNode) {
			var left = ((AssignmentNode) statement).target;
			var right = ((AssignmentNode) statement).expression;
			var leftRes = getIDT(left, localVars);
			var rightRes = getIDT(right, localVars);
			//System.out.println("IN POS Left: " + left + " right: " + rightRes);
			globalVars.put(left.toString(), rightRes);
			return new ReturnType(ReturnType.type.Normal, rightRes.getType());
		}
		else if(statement instanceof ParseBreakNode) {
			return new ReturnType(ReturnType.type.Break);
		}
		else if(statement instanceof ParseContinueNode) {
			return new ReturnType(ReturnType.type.Continue);
		}
		else if(statement instanceof ParseDeleteNode) {
			var delArray = ((ParseDeleteNode) statement).arrayName.get();
			var keyName = ((VariableReferenceNode) delArray).getName();
			String index;
			try {
				index = getIDT(((VariableReferenceNode) delArray).getExpression().get(), localVars).getType();
			}
			catch(Exception E) {
				index = null;
			}
			// check if exp is valid
			try {
				if(index != null) {
					Float.valueOf(index);
					String.valueOf(index);
				}
			}
			catch(Exception E) {
				throw new Exception("Invalid Index");
			}
			InterpreterArrayDataType arrayReference;
			//get array variable
			if(localVars.containsKey(keyName)) {
				arrayReference = (InterpreterArrayDataType) localVars.get(keyName);
			}
			else if (globalVars.containsKey(keyName)){
				arrayReference = (InterpreterArrayDataType) globalVars.get(keyName);
			}
			else
				throw new Exception("Array not declared");
			if(index == null) //no index declared
				arrayReference.getVariables().clear();
			else //remove index
				if(arrayReference.getVariables().containsKey(String.valueOf(index)))
					arrayReference.getVariables().remove(String.valueOf(index));
				else
					throw new Exception("Invalid index");
			return new ReturnType(ReturnType.type.Normal);
		}	
		else if(statement instanceof ParseDoWhileNode) {
			var condition = ((ParseDoWhileNode) statement).condition.get();
			var statements = ((ParseDoWhileNode) statement).body.get();
			var statementsList = ((BlockNode)statements).getStatements();
			do {
				var retType = interpretListOfStatements(statementsList, localVars);
				if (retType.typeOfRet.equals(ReturnType.type.Break)) {
					break;
					//return processStatment();
				}
				if (retType.typeOfRet.equals(ReturnType.type.Return)) {
					return processStatement(localVars, statement);
				}
			}
			while(getIDT(condition,localVars).getType().compareTo("true") == 0 || getIDT(condition,localVars).getType().compareTo("1") == 0);
			return new ReturnType(ReturnType.type.Normal);
		}
		else if(statement instanceof ParseForNode) {
			var body = ((ParseForNode) statement).body.get();
			var statements = ((BlockNode) body).getStatements();
			var condition = ((ParseForNode) statement).condition.get();
			var inc = ((ParseForNode) statement).delimiter.get();
			var variable = ((ParseForNode) statement).variable.get();
			System.out.println(variable);
			//var retVar = processStatement(localVars, (StatementNode) variable);
			while(getIDT(condition,localVars).getType().compareTo("true") == 0 || getIDT(condition,localVars).getType().compareTo("1") == 0) {
				var retType = interpretListOfStatements(statements, localVars);
				var increment = processStatement(localVars, (StatementNode) inc);
				if (retType.typeOfRet.equals(ReturnType.type.Break)) {
					break;
				}
				if (retType.typeOfRet.equals(ReturnType.type.Return)) {
					return processStatement(localVars, statement);
				}
			}
			//return retVar;
		}
		else if(statement instanceof ParseForEachNode) {
			var condition = ((ParseDoWhileNode) statement).condition.get();
			var statements = ((ParseDoWhileNode) statement).body.get();
			var statementsList = ((BlockNode) statements).getStatements();
			InterpreterArrayDataType arr;
			if(localVars.containsKey(condition.toString())) {
				arr = (InterpreterArrayDataType) localVars.get(condition.toString());
			}
			else if(globalVars.containsKey(condition.toString())) {
				arr = (InterpreterArrayDataType) globalVars.get(condition.toString());
			}
			else
				throw new Exception("Array doesn't exist");
			for(String index : arr.getVariables().keySet()) {
				var variable = index;
				var retType = interpretListOfStatements(statementsList, localVars);
				if (retType.typeOfRet.equals(ReturnType.type.Break)) {
					break;
				}
				if (retType.typeOfRet.equals(ReturnType.type.Return)) {
					return processStatement(localVars, statement);
				}
			}
			return new ReturnType(ReturnType.type.Normal); 
		}
		else if(statement instanceof ParseIfNode) {
			var condition = ((ParseIfNode) statement).condition;
			var operation = ((ParseIfNode) statement).operation;
			var statements = ((BlockNode) operation).getStatements();
			var next = ((ParseIfNode) statement).next;
			//check first instance of if
			if(condition == null || getIDT(condition, localVars).getType().compareTo("true") == 0 || getIDT(condition, localVars).getType().compareTo("1") == 0) {
				var retType = interpretListOfStatements(statements, localVars);
				if(retType != null) {
					if (!retType.typeOfRet.equals(ReturnType.type.Normal)) {
						return retType;
					}
				}
			}
			while(next != null) {
				next = next.next;
				condition = next.condition;
				operation = next.operation;
				statements = ((BlockNode) operation).getStatements();
				if(condition == null || getIDT(condition, localVars).getType().compareTo("true") == 0 || getIDT(condition, localVars).getType().compareTo("1") == 0) {
					var retType = interpretListOfStatements(statements, localVars);
					if (!retType.typeOfRet.equals(ReturnType.type.Normal)) {
						return retType;
					}
				}
			}
		}
		else if(statement instanceof ParseReturnNode) {
			var val = ((ParseReturnNode) statement).returnValue.get();
			//evaluate and return
			if(val != null)
				return new ReturnType(ReturnType.type.Return, val.toString());
		}
		else if(statement instanceof ParseWhileNode) {
			var condition = ((ParseWhileNode) statement).condition.get();
			var statements = ((ParseWhileNode) statement).body.get();
			var statementsList = ((BlockNode)statements).getStatements();
			
			while(getIDT(condition,localVars).getType().compareTo("true") == 0 || getIDT(condition,localVars).getType().compareTo("1") == 0) {
				var retType = interpretListOfStatements(statementsList, localVars);
				if(retType != null) {
					if (retType.typeOfRet.equals(ReturnType.type.Break)) {
						break;
					}
					if (retType.typeOfRet.equals(ReturnType.type.Return)) {
						return processStatement(localVars, statement);
					}
				}
			}
			return new ReturnType(ReturnType.type.Normal);
		}
		else {
//			System.out.println(statement + " hrer");
			var getIDTVar = getIDT(statement,localVars);
			if(getIDTVar == null)
				throw new Exception("Invalid statement");
			return new ReturnType(ReturnType.type.Normal, getIDTVar.getType());
		}
		return null;
	}
	
	public ReturnType interpretListOfStatements(LinkedList<StatementNode> statements, HashMap<String, InterpreterDataType> localVars) throws Exception {
		//System.out.println(statements);
		ReturnType retType = null;
		for(StatementNode statement : statements) {
			retType = processStatement(localVars,statement);
		}
		return retType;
	}
	
	public String runFunctionCall(ParseFunctionCallNode inputNode, HashMap<String,InterpreterDataType> localVars) throws Exception {
//		System.out.println("node: " + inputNode + " with name: " + inputNode.getFunction() + " and params: " + inputNode.getStatements());
		HashMap<String, InterpreterDataType> lambdaFunctionArray = new HashMap<String, InterpreterDataType>();
		HashMap<String, InterpreterDataType> arrayOfStatements = new HashMap<String, InterpreterDataType>();
		char index = '1';
		if(inputNode.getFunction() != null) {
			if(functionCalls.containsKey(((ParseFunctionCallNode)inputNode).getFunction())) {
				var functionDef = functionCalls.get(((ParseFunctionCallNode)inputNode).getFunction());
				if(functionDef instanceof BuiltInFunctionNode){
					if(((BuiltInFunctionNode) functionDef).variadic) {
							index = '1';
							for(Node parameter : inputNode.getStatements()) {
								arrayOfStatements.put(String.valueOf(index++), getIDT(parameter, localVars));
							}
							lambdaFunctionArray.put(inputNode.getFunction(), new InterpreterArrayDataType(arrayOfStatements));
							if(functionDef instanceof BuiltInFunctionNode) {
								return ((BuiltInFunctionNode) functionDef).execute.apply(lambdaFunctionArray);
							}
					}
				}
				else { // user defined function
//					if(functionCalls.get(inputNode.getFunction()).getParameters().size() != 1) {
//						
//					}
					for(Node parameter: inputNode.getStatements()) {
						//System.out.println("para" + parameter);
						arrayOfStatements.put(String.valueOf(index++), getIDT(parameter, localVars));
					}
					//System.out.println(arrayOfStatements);
					lambdaFunctionArray.put(inputNode.getFunction(), new InterpreterArrayDataType(arrayOfStatements));
					//System.out.println(lambdaFunctionArray);
					return interpretListOfStatements(functionDef.getStatements(), lambdaFunctionArray).returnValue;
				}
			}
		}
		return "";
	}
	
	public void interpretProgram(ProgramNode program) throws Exception {
		lineMNG.splitAndAssign();
		//System.out.println(program.getBlocks() + " ----------------");
		for(BlockNode startBlock: program.getStartBlocks()) {
			interpretBlock(startBlock);
			lineMNG.splitAndAssign();
		}
		for(BlockNode endBlock: program.getEndBlocks()) {
			interpretBlock(endBlock);
		}
		for(BlockNode block: program.getBlocks()) {
			interpretBlock(block);
		}
	}
	
	public void interpretBlock(BlockNode block) throws Exception {
		try {
			var condition = block.getCondition().get();
			if(String.valueOf(getIDT(condition, new HashMap<String, InterpreterDataType>()).getType()).compareTo("1") == 0 || String.valueOf(getIDT(condition, new HashMap<String, InterpreterDataType>()).getType()).compareTo("true") == 0) {
				for(StatementNode statement : block.getStatements()) {
					processStatement(new HashMap<String, InterpreterDataType>(),statement);
				}
			}
		}
		catch(Exception E) {
			for(StatementNode statement : block.getStatements()) {
				processStatement(new HashMap<String, InterpreterDataType>(),statement);
			}
		}
	}
}
