import java.util.HashMap;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

public class InterpreterTesting {
	
	@Test
    public void testlength() throws Exception {
        Interpereter lengthTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        functions.put("lengthFunction", new InterpreterDataType("iiii"));
        BuiltInFunctionNode lengthFunction = (BuiltInFunctionNode) lengthTestItpr.getFunctions().get("length");
        Assert.assertEquals(lengthFunction.execute.apply(functions), "4");
    }
	
	//@Test
    public void testprintf() throws Exception {
        Interpereter printfTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        var IDT = new InterpreterDataType("printthis!");
        var IDT2 = new InterpreterDataType("printthistoo!");
        HashMap<String, InterpreterDataType> mymap = new HashMap<String, InterpreterDataType>();
        mymap.put("0", IDT);
        mymap.put("1", IDT2);
        functions.put("printfFunction", new InterpreterArrayDataType(mymap));
        BuiltInFunctionNode printfFunction = (BuiltInFunctionNode) printfTestItpr.getFunctions().get("printf");
        Assert.assertEquals(printfFunction.execute.apply(functions), "0");
    }

	@Test
    public void testgetline() throws Exception {
        Interpereter getlineTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        functions.put("getlineFunction", new InterpreterDataType("iiii"));
        BuiltInFunctionNode getlineFunction = (BuiltInFunctionNode) getlineTestItpr.getFunctions().get("getline");
        Assert.assertEquals(getlineFunction.execute.apply(functions), "0");
    }

	@Test
    public void testnext() throws Exception {
        Interpereter nextTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        functions.put("nextFunction", new InterpreterDataType("iiii"));
        BuiltInFunctionNode nextFunction = (BuiltInFunctionNode) nextTestItpr.getFunctions().get("next");
        Assert.assertEquals(nextFunction.execute.apply(functions), "0");
    }

	@Test
    public void testgsub() throws Exception {
        Interpereter nextTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        functions.put("gsubRegex", new InterpreterDataType("l"));
        functions.put("gsubReplacement", new InterpreterDataType("g"));
        functions.put("gsubTarget", new InterpreterDataType("hello world"));
        BuiltInFunctionNode nextFunction = (BuiltInFunctionNode) nextTestItpr.getFunctions().get("gsub");
        Assert.assertEquals(nextFunction.execute.apply(functions), "3");
    }

	@Test
    public void testindex() throws Exception {
        Interpereter indexTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        functions.put("indexFunction", new InterpreterDataType("hello"));
        functions.put("searchFunction", new InterpreterDataType("ll"));
        BuiltInFunctionNode indexFunction = (BuiltInFunctionNode) indexTestItpr.getFunctions().get("index");
        Assert.assertEquals(indexFunction.execute.apply(functions), "2");
    }

	//@Test
    public void testprint() throws Exception {
		Interpereter printfTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        var IDT = new InterpreterDataType("printmystring!");
        HashMap<String, InterpreterDataType> mymap = new HashMap<String, InterpreterDataType>();
        mymap.put("", IDT);
        functions.put("printFunction", new InterpreterArrayDataType(mymap));
        BuiltInFunctionNode printFunction = (BuiltInFunctionNode) printfTestItpr.getFunctions().get("print");
        Assert.assertEquals(printFunction.execute.apply(functions), "0");
    }

	@Test
    public void testmatch() throws Exception {
        Interpereter matchTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        functions.put("regExp", new InterpreterDataType("h+"));
        functions.put("string", new InterpreterDataType("world hello"));
        BuiltInFunctionNode matchFunction = (BuiltInFunctionNode) matchTestItpr.getFunctions().get("match");
        matchFunction.execute.apply(functions);
        Assert.assertEquals(matchFunction.execute.apply(functions), "6");
    }

	@Test
    public void testsplit() throws Exception {
        Interpereter nextTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        functions.put("regExp", new InterpreterDataType("h+"));
        functions.put("string", new InterpreterDataType("world hello"));
        BuiltInFunctionNode nextFunction = (BuiltInFunctionNode) nextTestItpr.getFunctions().get("match");
        nextFunction.execute.apply(functions);
        Assert.assertEquals(nextFunction.execute.apply(functions), "6");
    }

	@Test
    public void testsub() throws Exception {
        Interpereter subTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        functions.put("subFunction", new InterpreterDataType("hello world"));
        functions.put("subIndexOne", new InterpreterDataType("2"));
        functions.put("subIndexTwo", new InterpreterDataType("4"));
        BuiltInFunctionNode subFunction = (BuiltInFunctionNode) subTestItpr.getFunctions().get("sub");
        subFunction.execute.apply(functions);
        Assert.assertEquals(subFunction.execute.apply(functions), "ll");
    }

	@Test
    public void testsubstr() throws Exception {
        Interpereter substrTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        functions.put("subStringFunction", new InterpreterDataType("hello world"));
        functions.put("indexOne", new InterpreterDataType("4"));
        functions.put("indexTwo", new InterpreterDataType("5"));
        BuiltInFunctionNode substrFunction = (BuiltInFunctionNode) substrTestItpr.getFunctions().get("substr");
        substrFunction.execute.apply(functions);
        Assert.assertEquals(substrFunction.execute.apply(functions), "o");
    }

	@Test
    public void testtolower() throws Exception {
        Interpereter tolowerTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        functions.put("tolowerFunction", new InterpreterDataType("Testing"));
        BuiltInFunctionNode tolowerFunction = (BuiltInFunctionNode) tolowerTestItpr.getFunctions().get("tolower");
        tolowerFunction.execute.apply(functions);
        Assert.assertEquals(tolowerFunction.execute.apply(functions), "testing");
    }

	@Test
    public void testtoupper() throws Exception {
        Interpereter toupperTestItpr = new Interpereter(new ProgramNode(null, null, null, null), null);
        HashMap<String, InterpreterDataType> functions = new HashMap<String, InterpreterDataType>();
        functions.put("toupperFunction", new InterpreterDataType("testing"));
        BuiltInFunctionNode toupperFunction = (BuiltInFunctionNode) toupperTestItpr.getFunctions().get("toupper");
        toupperFunction.execute.apply(functions);
        Assert.assertEquals(toupperFunction.execute.apply(functions), "TESTING");
    }
	
	@Test
    public void testgetIDTNestAssignment() throws Exception {
		var varNode = new VariableReferenceNode("a");
		var operation = new OperationNode(Optional.of(new ConstantNode("2")), Optional.of(new ConstantNode("2")), OperationNode.possibleOperations.ADD);
		var assignment = new AssignmentNode(varNode, operation);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(assignment, null);
		Assert.assertTrue(testITPR.getVariables().containsKey(varNode.getName()));
		Assert.assertTrue(testITPR.getVariables().get("a").getType().compareTo("4.0") == 0);
    }
	
	@Test
    public void testgetIDTUnaryNegativeAssignment() throws Exception {
		var varNode = new VariableReferenceNode("b");
		var operation = new OperationNode(Optional.of(new ConstantNode("2")),OperationNode.possibleOperations.UNARYNEGATIVE);
		var assignment = new AssignmentNode(varNode, operation);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(assignment, null);
		Assert.assertTrue(testITPR.getVariables().containsKey(varNode.getName()));
		Assert.assertTrue(testITPR.getVariables().get("b").getType().compareTo("-2.0") == 0);
    }
    
	@Test
    public void testgetIDTTernaryAssignment() throws Exception {
	  	var varRef = new VariableReferenceNode("myTernary");
		var condition = (new OperationNode(Optional.of(new ConstantNode("hello")), Optional.of(new ConstantNode("hello")), OperationNode.possibleOperations.EQUAL));
		var trueVal = (new ConstantNode("they're the same"));
		var falseVal = (new ConstantNode("not the same"));
		var ternaryExp = (new TernaryNode(condition, trueVal, falseVal));
		var assignment = new AssignmentNode(varRef, ternaryExp);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(assignment, null);
		Assert.assertTrue(testITPR.getVariables().containsKey(varRef.getName()));
		Assert.assertTrue(testITPR.getVariables().get("myTernary").getType().compareTo("they're the same") == 0);
    }
  
  @Test
  public void testgetIDTTernaryAssignmentFalse() throws Exception {
	  	var varRef = new VariableReferenceNode("myTernary");
		var condition = (new OperationNode(Optional.of(new ConstantNode("helo")), Optional.of(new ConstantNode("hello")), OperationNode.possibleOperations.EQUAL));
		var trueVal = (new ConstantNode("they're the same"));
		var falseVal = (new ConstantNode("not the same"));
		var ternaryExp = (new TernaryNode(condition, trueVal, falseVal));
		var assignment = new AssignmentNode(varRef, ternaryExp);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(assignment, null);
		Assert.assertTrue(testITPR.getVariables().containsKey(varRef.getName()));
		Assert.assertTrue(testITPR.getVariables().get("myTernary").getType().compareTo("not the same") == 0);
  }
  
  @Test
  public void testgetIDTTernaryAssignmentTrueBoolean() throws Exception {
	  	var varRef = new VariableReferenceNode("myTernary");
		var condition = (new OperationNode(Optional.of(new ConstantNode("5")), Optional.of(new ConstantNode("192")), OperationNode.possibleOperations.LESSTHAN));
		var trueVal = (new ConstantNode("good to go"));
		var falseVal = (new ConstantNode("not good"));
		var ternaryExp = (new TernaryNode(condition, trueVal, falseVal));
		var assignment = new AssignmentNode(varRef, ternaryExp);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(assignment, null);
		Assert.assertTrue(testITPR.getVariables().containsKey(varRef.getName()));
		Assert.assertTrue(testITPR.getVariables().get("myTernary").getType().compareTo("good to go") == 0);
  }
  
  @Test
  public void testgetIDTPostInc() throws Exception {
	  	var pre = new AssignmentNode(new VariableReferenceNode("a"), new ConstantNode("10"));
	  	var postIncOperation = new OperationNode(Optional.of(pre), OperationNode.possibleOperations.POSTINCREMENT);
	  	var post = new VariableReferenceNode("b");
	  	var after = new AssignmentNode(post, postIncOperation);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(after, null);
		Assert.assertTrue(testITPR.getVariables().containsKey(pre.target.toString()));
		Assert.assertTrue(testITPR.getVariables().get("a").getType().compareTo("10") == 0);
		Assert.assertTrue(testITPR.getVariables().containsKey(post.getName()));
		Assert.assertTrue(testITPR.getVariables().get("b").getType().compareTo("11.0") == 0);
  }
  
  @Test
  public void testgetIDTPreInc() throws Exception {
	  	var pre = new AssignmentNode(new VariableReferenceNode("a"), new ConstantNode("10"));
	  	var postIncOperation = new OperationNode(Optional.of(pre), OperationNode.possibleOperations.PREINCREMENT);
	  	var post = new VariableReferenceNode("b");
	  	var after = new AssignmentNode(post, postIncOperation);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(after, null);
		Assert.assertTrue(testITPR.getVariables().containsKey(pre.target.toString()));
		Assert.assertTrue(testITPR.getVariables().get("a").getType().compareTo("10") == 0);
		Assert.assertTrue(testITPR.getVariables().containsKey(post.getName()));
		Assert.assertTrue(testITPR.getVariables().get("b").getType().compareTo("11.0") == 0);
  }
  
  @Test
  public void testgetIDTPreDec() throws Exception {
	  	var pre = new AssignmentNode(new VariableReferenceNode("a"), new ConstantNode("10"));
	  	var postIncOperation = new OperationNode(Optional.of(pre), OperationNode.possibleOperations.PREDECREMENT);
	  	var post = new VariableReferenceNode("b");
	  	var after = new AssignmentNode(post, postIncOperation);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(after, null);
		Assert.assertTrue(testITPR.getVariables().containsKey(pre.target.toString()));
		Assert.assertTrue(testITPR.getVariables().get("a").getType().compareTo("10") == 0);
		Assert.assertTrue(testITPR.getVariables().containsKey(post.getName()));
		Assert.assertTrue(testITPR.getVariables().get("b").getType().compareTo("9.0") == 0);
  }
  
  @Test
  public void testgetIDTPostDec() throws Exception {
	  	var pre = new AssignmentNode(new VariableReferenceNode("a"), new ConstantNode("10"));
	  	var postIncOperation = new OperationNode(Optional.of(pre), OperationNode.possibleOperations.POSTDECREMENT);
	  	var post = new VariableReferenceNode("b");
	  	var after = new AssignmentNode(post, postIncOperation);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(after, null);
		Assert.assertTrue(testITPR.getVariables().containsKey(pre.target.toString()));
		Assert.assertTrue(testITPR.getVariables().get("a").getType().compareTo("10") == 0);
		Assert.assertTrue(testITPR.getVariables().containsKey(post.getName()));
		Assert.assertTrue(testITPR.getVariables().get("b").getType().compareTo("9.0") == 0);
  }
  
  @Test
  public void testgetIDTArrayIndex() throws Exception {
	  	Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
	  	HashMap<String, InterpreterDataType> somearray = new HashMap<String,InterpreterDataType>();
	  	char a = 'a';
	  	for(double i = 0.0; i < 10; i++) //create an array with indicies 1-9 and string values a-j
	  		somearray.put(String.valueOf(i), new InterpreterDataType(String.valueOf(a++)));
	  	var myArray = new VariableReferenceNode("someArray", Optional.of(new OperationNode(Optional.of(new ConstantNode("5")) ,Optional.of(new ConstantNode("2")), OperationNode.possibleOperations.ADD)));
		var arrayIndexAssignment = new AssignmentNode(new VariableReferenceNode("seventhIndex"), myArray);
		testITPR.getVariables().put("someArray", new InterpreterArrayDataType(somearray));
		testITPR.getIDT(arrayIndexAssignment, null);
		Assert.assertTrue(testITPR.getVariables().containsKey("someArray"));
		Assert.assertTrue(testITPR.getVariables().containsKey("seventhIndex"));
		Assert.assertTrue(testITPR.getVariables().get("seventhIndex").getType().compareTo("h") == 0);
		Assert.assertTrue(((InterpreterArrayDataType) testITPR.getVariables().get("someArray")).getVariables().containsKey("1.0"));
  }
  
  @Test(expected = Exception.class)
  public void testgetIDTPattern() throws Exception {
		var pattern = new PatternNode("myPattern");
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(pattern, null);
  }
  
  @Test
  public void testgetIDTFunctionCall() throws Exception {
		var myFunction = new FunctionCallNode(null, null);
		var functionVar = new AssignmentNode(new VariableReferenceNode("functionResult"), myFunction);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(functionVar, null);
		Assert.assertTrue(testITPR.getVariables().containsKey("functionResult"));
		Assert.assertTrue(testITPR.getVariables().get("functionResult").getType().compareTo("") == 0);
  }
  
  @Test
  public void testgetIDTIN() throws Exception {
	  	Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
	  	HashMap<String, InterpreterDataType> somearray = new HashMap<String,InterpreterDataType>();
	  	char a = 'a';
	  	for(double i = 0.0; i <= 20; i++) //create an array with indicies 1-20
	  		somearray.put(String.valueOf(i), new InterpreterDataType(String.valueOf(a++)));
	  	testITPR.getVariables().put("someArray", new InterpreterArrayDataType(somearray));
	  	var inResult = new AssignmentNode(new VariableReferenceNode("myResult"), new OperationNode(Optional.of(new ConstantNode("5.0")),Optional.of(new VariableReferenceNode("someArray")),OperationNode.possibleOperations.IN));
		testITPR.getIDT(inResult, new HashMap<String, InterpreterDataType>());
		Assert.assertEquals(testITPR.getVariables().containsKey("myResult"),true);
  }
  
  @Test
  public void testgetIDTUnaryPos() throws Exception {
		var varNode = new VariableReferenceNode("b");
		var operation = new OperationNode(Optional.of(new ConstantNode("-2")),OperationNode.possibleOperations.UNARYPOSITIVE);
		var assignment = new AssignmentNode(varNode, operation);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(assignment, null);
		Assert.assertTrue(testITPR.getVariables().containsKey(varNode.getName()));
		Assert.assertTrue(testITPR.getVariables().get("b").getType().compareTo("2.0") == 0);
  }
  
  @Test
  public void testgetIDTDollar() throws Exception {
		var operationDollar = new OperationNode(Optional.of(new ConstantNode("2")),OperationNode.possibleOperations.DOLLAR);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(operationDollar, null);
		Assert.assertTrue(testITPR.getVariables().containsKey("$2.0"));
		Assert.assertTrue(testITPR.getVariables().get("$2.0").getType().compareTo("")==0);
  }
  
  @Test
  public void testgetIDTDollarAssignment() throws Exception {
		var operationDollar = new OperationNode(Optional.of(new ConstantNode("2")),OperationNode.possibleOperations.DOLLAR);
		var assignment = new AssignmentNode(new VariableReferenceNode("$2"), new ConstantNode("5"));
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(assignment, null);
		Assert.assertTrue(testITPR.getVariables().containsKey("$2"));
		Assert.assertTrue(testITPR.getVariables().get("$2").getType().compareTo("5")==0);
  }
  
  @Test
  public void testgetIDTConcat() throws Exception {
	  	var myString = new VariableReferenceNode("myString");
		var concat = new OperationNode(Optional.of(new ConstantNode("hello ")), Optional.of(new ConstantNode("world")),OperationNode.possibleOperations.CONCATENATION);
		var assignment = new AssignmentNode(myString, concat);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(assignment, null);
		Assert.assertTrue(testITPR.getVariables().containsKey("myString"));
		Assert.assertTrue(testITPR.getVariables().get("myString").getType().compareTo("hello world")==0);
  }
  
  @Test
  public void testgetIDTPatterns() throws Exception {
	  	var myPattern = new ConstantNode("hi");
	  	var patternRes = new VariableReferenceNode("myPatternRes");
		var concat = new OperationNode(Optional.of(new ConstantNode("hello")), Optional.of(myPattern),OperationNode.possibleOperations.MATCH);
		var assignment = new AssignmentNode(patternRes, concat);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(assignment, null);
		Assert.assertTrue(testITPR.getVariables().containsKey("myPatternRes"));
		Assert.assertTrue(testITPR.getVariables().get("myPatternRes").getType().compareTo("false")==0);
  }
  
  @Test
  public void testgetIDTPatternsNotMatch() throws Exception {
	  	var myPattern = new ConstantNode("l");
	  	var patternRes = new VariableReferenceNode("myPatternRes");
		var concat = new OperationNode(Optional.of(new ConstantNode("hello")), Optional.of(myPattern),OperationNode.possibleOperations.MATCH);
		var assignment = new AssignmentNode(patternRes, concat);
		Interpereter testITPR = new Interpereter(new ProgramNode(null, null, null, null), null);
		testITPR.getIDT(assignment, null);
		Assert.assertTrue(testITPR.getVariables().containsKey("myPatternRes"));
		Assert.assertTrue(testITPR.getVariables().get("myPatternRes").getType().compareTo("true")==0);
  }
}
