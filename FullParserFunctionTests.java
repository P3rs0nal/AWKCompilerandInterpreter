import org.junit.Assert;
import org.junit.Test;
/*
 * Due to the structure of my toStrings, the carriage return character will result in 
 * "failed" unit tests despite them being correct. To correct this, I used outputString.trim().replace("\r","")
 * to correct any unnecessary carriage returns so the tests will correctly test the programs by
 * replacing any "\r" characters with "" spaces.
 * */
public class FullParserFunctionTests {
	@Test
    public void testEmptyFunctions() throws Exception {
         String multipleFunctions = "BEGIN function factorial(f,y,z){ } function testFunction(parameter, someOtherParameter){} END";
         String singleFunction = "BEGIN function factorial(f,y,z){ } END";

         Lexer multipleFunctionLex = new Lexer(multipleFunctions);
         Lexer singleFunctionLex = new Lexer(singleFunction);

         multipleFunctionLex.Lex();
         singleFunctionLex.Lex();
         
         Parser parsingOneFunction = new Parser(singleFunctionLex.getTokens());
         Parser parsingMultipleFunction = new Parser(multipleFunctionLex.getTokens());
         
         ProgramNode progOneFunction = parsingOneFunction.parse();
         ProgramNode progMultipleFunctions = parsingMultipleFunction.parse();
         
         Assert.assertEquals(progOneFunction.toString().trim().replace("\r", ""),"function factorial(f,y,z){\n\n\t}");
         Assert.assertEquals(progMultipleFunctions.toString().trim().replace("\r", ""),"function factorial(f,y,z){\n\n\t}\nfunction testFunction(parameter,someOtherParameter){\n\n\t}");   
    }
	
	@Test
    public void testFunctionsWithBody() throws Exception {
         String multipleFunctions = "{if($2==$3){print $1\",\"$2\",\"$3} else {print \"No Duplicates\"}} ";
         
         Lexer functionLex = new Lexer(multipleFunctions);
         
         functionLex.Lex();
         
         Parser parsingFunctionOne = new Parser(functionLex.getTokens());
         
         ProgramNode testerProgOne = parsingFunctionOne.parse();
         
         Assert.assertEquals(testerProgOne.toString().trim().replace("\r", ""),"function two(f,y,z){\n"
         		+ "	if(e = 2)\n"
         		+ "POSTINCREMENT x \n"
         		+ "	}\n"
         		+ "function testFunction(parameter,someOtherParameter){\n"
         		+ "\n"
         		+ "	}");   
    }
	
	@Test
    public void testStatementsContinueBreak() throws Exception {
         String statementFunction = "BEGIN function statement(f,y,z){ e++; x--; x = 5 + 2; j = 9*6+8^2^3; if(x+2==5){p=2; t=4;}} END";
         String ContinueBreakFunction = "BEGIN function otherStatement(parameter, someOtherParameter){continue break} END";
         
         Lexer statementLex = new Lexer(statementFunction);
         Lexer ContinueBreakLex = new Lexer(ContinueBreakFunction);
         
         statementLex.Lex();
         ContinueBreakLex.Lex();

         Parser statementsParse = new Parser(statementLex.getTokens());
         Parser ContinueBreakParse = new Parser(ContinueBreakLex.getTokens());

         ProgramNode statementsProg = statementsParse.parse();
         ProgramNode continueBreakProg = ContinueBreakParse.parse();

         Assert.assertEquals(statementsProg.toString().trim().replace("\r", ""),"function statement(f,y,z){\n"
         		+ "\tPOSTINCREMENT e	POSTDECREMENT x	x = 5 ADD 2	j = 9 MULTIPLY 6 ADD 8 EXPONENT 2 EXPONENT 3	if(x ADD 2 EQUAL 5)\n"
         		+ "p = 2 t = 4 \n"
         		+ "\t}");
         Assert.assertEquals(continueBreakProg.toString().trim().replace("\r", ""),"function otherStatement(parameter,someOtherParameter){\n"
         		+ "\tcontinue\tbreak\n"
         		+ "\t}");
    }
	
	@Test
    public void testIf() throws Exception {
         String ifFunction = "BEGIN function ifFunction(f,y,z){if(x+2==5){p=2; t=4;}} END";
         String ifElseFunction = "BEGIN function ifElseFunction(parameter, someOtherParameter){if(y==5){k=5} else j+=3} END";
         String ifMultipleElseFunction = "BEGIN function ifMultipleElseFunction(parameter, someOtherParameter){if(y==5){k=5} else if(m==6) j+=3 else if(l==128) p-- else j=2} END";
         
         Lexer ifFunctionLex = new Lexer(ifFunction);
         Lexer ifElseFunctionLex = new Lexer(ifElseFunction);
         Lexer ifMultipleElseFunctionLex = new Lexer(ifMultipleElseFunction);
         
         ifFunctionLex.Lex();
         ifElseFunctionLex.Lex();
         ifMultipleElseFunctionLex.Lex();

         Parser ifFunctionParse = new Parser(ifFunctionLex.getTokens());
         Parser ifElseFunctionParse = new Parser(ifElseFunctionLex.getTokens());
         Parser ifMultipleElseFunctionParse = new Parser(ifMultipleElseFunctionLex.getTokens());

         ProgramNode ifFunctionProg = ifFunctionParse.parse();
         ProgramNode ifElseFunctionProg = ifElseFunctionParse.parse();
         ProgramNode ifMultipleElseFunctionProg = ifMultipleElseFunctionParse.parse();

         Assert.assertEquals(ifFunctionProg.toString().trim().replace("\r", ""),"function ifFunction(f,y,z){\n"
         		+ "\tif(x ADD 2 EQUAL 5)\n"
         		+ "p = 2 t = 4 \n"
         		+ "\t}");
         Assert.assertEquals(ifElseFunctionProg.toString().trim().replace("\r", ""),"function ifElseFunction(parameter,someOtherParameter){\n"
         		+ "\tif(y EQUAL 5)\n"
         		+ "k = 5 else\t\n"
         		+ "j = j ADD 3 \n"
         		+ "\t}");
         Assert.assertEquals(ifMultipleElseFunctionProg.toString().trim().replace("\r", ""),"function ifMultipleElseFunction(parameter,someOtherParameter){\n"
         		+ "\tif(y EQUAL 5)\n"
         		+ "k = 5 else\tif(m EQUAL 6)\n"
         		+ "j = j ADD 3 else	if(l EQUAL 128)\n"
         		+ "POSTDECREMENT p else	\n"
         		+ "j = 2 \n"
         		+ "\t}");
    }
    
    @Test
    public void testFor() throws Exception {
         String forFunction = "BEGIN function forFunction(x,y,z){for(i;i<c;i++)p--} END";
         String forInFunction = "BEGIN function forInFunction(parameter, someOtherParameter){for(a in b) a*=2} END";
         String nestedForLoops = "BEGIN function forNestedFunction(parameter, someOtherParameter){for(i;i<k;k--){if(x==p-2){for(j in x)a=a+b}}} END";
         
         Lexer forFunctionLex = new Lexer(forFunction);
         Lexer forInFunctionLex = new Lexer(forInFunction);
         Lexer nestedForLoopsLex = new Lexer(nestedForLoops);
         
         forFunctionLex.Lex();
         forInFunctionLex.Lex();
         nestedForLoopsLex.Lex();

         Parser forFunctionParse = new Parser(forFunctionLex.getTokens());
         Parser forInFunctionParse = new Parser(forInFunctionLex.getTokens());
         Parser nestedForLoopsParse = new Parser(nestedForLoopsLex.getTokens());

         ProgramNode forFunctionProg = forFunctionParse.parse();
         ProgramNode forInFunctionProg = forInFunctionParse.parse();
         ProgramNode nestedForLoopsProg = nestedForLoopsParse.parse();

         Assert.assertEquals(forFunctionProg.toString().trim().replace("\r", ""),"function forFunction(x,y,z){\n"
         		+ "	for(i;i LESSTHAN c;POSTINCREMENT i)\n"
         		+ "	POSTDECREMENT p \n"
         		+ "	}");
         Assert.assertEquals(forInFunctionProg.toString().trim().replace("\r", ""),"function forInFunction(parameter,someOtherParameter){\n"
         		+ "	\tfor(a IN b)\n"
         		+ "	a = a MULTIPLY 2 \n"
         		+ "	}");
         Assert.assertEquals(nestedForLoopsProg.toString().trim().replace("\r", ""),"function forNestedFunction(parameter,someOtherParameter){\n"
         		+ "	for(i;i LESSTHAN k;POSTDECREMENT k)\n"
         		+ "	if(x EQUAL p SUBTRACT 2)\n"
         		+ "	for(j IN x)\n"
         		+ "	a = a ADD b   \n"
         		+ "	}");
    }
    
    @Test
    public void testWhileAndDoWhile() throws Exception {
         String whileFunction = "BEGIN function whileFunction(f,y,z){while(i<=2)i--} END";
         String doWhileFunction = "BEGIN function doWhileFunction(parameter, someOtherParameter){do k++ while(x<=2)} END";
         String nestedWhileFunctions = "BEGIN function nestedWhileFunction(parameter, someOtherParameter){while(x>2){do{x++}while(p>2)} do{x-- while(x<2){loop++}} while(loop==loop)} END";
         
         Lexer whileFunctionLex = new Lexer(whileFunction);
         Lexer doWhileFunctionLex = new Lexer(doWhileFunction);
         Lexer nestedWhileFunctionsLex = new Lexer(nestedWhileFunctions);
         
         whileFunctionLex.Lex();
         doWhileFunctionLex.Lex();
         nestedWhileFunctionsLex.Lex();

         Parser whileFunctionParse = new Parser(whileFunctionLex.getTokens());
         Parser doWhileFunctionParse = new Parser(doWhileFunctionLex.getTokens());
         Parser nestedWhileFunctionsParse = new Parser(nestedWhileFunctionsLex.getTokens());

         ProgramNode whileFunctionProg = whileFunctionParse.parse();
         ProgramNode doWhileFunctionProg = doWhileFunctionParse.parse();
         ProgramNode nestedWhileFunctionsProg = nestedWhileFunctionsParse.parse();
         
         Assert.assertEquals(whileFunctionProg.toString().trim().replace("\r", ""),"function whileFunction(f,y,z){\n"
         		+ "	while(i LESSTHANEQUAL 2)\n"
         		+ "	POSTDECREMENT i \n"
         		+ "	}");
         Assert.assertEquals(doWhileFunctionProg.toString().trim().replace("\r", ""),"function doWhileFunction(parameter,someOtherParameter){\n"
         		+ "	do\n"
         		+ "	POSTINCREMENT k \n"
         		+ "	while(x LESSTHANEQUAL 2)\n"
         		+ "	}"); 
         Assert.assertEquals(nestedWhileFunctionsProg.toString().trim().replace("\r", ""),"function nestedWhileFunction(parameter,someOtherParameter){\n"
         		+ "	while(x GREATERTHAN 2)\n"
         		+ "	do\n"
         		+ "	POSTINCREMENT x \n"
         		+ "	while(p GREATERTHAN 2) 	do\n"
         		+ "	POSTDECREMENT x while(x LESSTHAN 2)\n"
         		+ "	POSTINCREMENT loop  \n"
         		+ "	while(loop EQUAL loop)\n"
         		+ "	}"); 
    }
    
    @Test
    public void testFunctionCall() throws Exception {
         String functionCall = "BEGIN function functionCall(f,y,z){while(i<=2)i-- testFunction(x,y,z)} END";
         String nestedfunctionCall = "BEGIN function nestedFunctionCall(parameter, someOtherParameter){while(loop==loop) someOtherFunction(p,x,h,e)} END";
         
    	 Lexer functionCallLex = new Lexer(functionCall);
         Lexer nestedfunctionCallLex = new Lexer(nestedfunctionCall);
         
         functionCallLex.Lex();
         nestedfunctionCallLex.Lex();

         Parser functionCallParse = new Parser(functionCallLex.getTokens());
         Parser nestedfunctionCallParse = new Parser(nestedfunctionCallLex.getTokens());

         ProgramNode functionCallProg = functionCallParse.parse();
         ProgramNode nestedFunctionCallProg = nestedfunctionCallParse.parse();
         
         Assert.assertEquals(functionCallProg.toString().trim().replace("\r", ""),"function functionCall(f,y,z){\n"
         		+ "	while(i LESSTHANEQUAL 2)\n"
         		+ "	POSTDECREMENT i CONCATENATION testFunction(x y z) \n"
         		+ "	}");
         Assert.assertEquals(nestedFunctionCallProg.toString().trim().replace("\r", ""),"function nestedFunctionCall(parameter,someOtherParameter){\n"
         		+ "	while(loop EQUAL loop)\n"
         		+ "	someOtherFunction(p x h e) \n"
         		+ "	}");
    }
    
    @Test
    public void testReturn() throws Exception {
         String returnCall = "BEGIN function returnCall(f,y,z){return x} END";
         String emptyReturnCall = "BEGIN function nestedReturnCall(parameter, someOtherParameter){return } END";
         String nestedReturnCall = "BEGIN function returnCall(f,y,z){while(x<2)return x} END";

    	 Lexer returnCallLex = new Lexer(returnCall);
         Lexer emptyReturnCallLex = new Lexer(emptyReturnCall);
    	 Lexer nestedReturnCallLex = new Lexer(nestedReturnCall);

         returnCallLex.Lex();
         emptyReturnCallLex.Lex();
         nestedReturnCallLex.Lex();

         Parser returnCallParse = new Parser(returnCallLex.getTokens());
         Parser emptyReturnCallParse = new Parser(emptyReturnCallLex.getTokens());
         Parser nestedReturnCallParse = new Parser(nestedReturnCallLex.getTokens());

         ProgramNode returnCallProg = returnCallParse.parse();
         ProgramNode emptyReturnCallProg = emptyReturnCallParse.parse();
         ProgramNode nestedReturnCallProg = nestedReturnCallParse.parse();

         Assert.assertEquals(returnCallProg.toString().trim().replace("\r", ""),"function returnCall(f,y,z){\n"
         		+ "	return(x)\n"
         		+ "	}");
         Assert.assertEquals(emptyReturnCallProg.toString().trim().replace("\r", ""),"function nestedReturnCall(parameter,someOtherParameter){\n"
         		+ "	return()\n"
         		+ "	}");
         Assert.assertEquals(nestedReturnCallProg.toString().trim().replace("\r", ""),"function returnCall(f,y,z){\n"
         		+ "	while(x LESSTHAN 2)\n"
         		+ "	return(x) \n"
         		+ "	}");
    }
    
    @Test
    public void testDelete() throws Exception {
         String deleteCall = "BEGIN function returnCall(f,y,z){delete x} END";
         String deleteInArrayCall = "BEGIN function nestedReturnCall(parameter, someOtherParameter){delete x[k in b]} END";

    	 Lexer deleteCallLex = new Lexer(deleteCall);
         Lexer deleteInArrayCallLex = new Lexer(deleteInArrayCall);

         deleteCallLex.Lex();
         deleteInArrayCallLex.Lex();

         Parser deleteCallParse = new Parser(deleteCallLex.getTokens());
         Parser deleteInArrayCallParse = new Parser(deleteInArrayCallLex.getTokens());

         ProgramNode deleteCallProg = deleteCallParse.parse();
         ProgramNode deleteInArrayCallProg = deleteInArrayCallParse.parse();

         Assert.assertEquals(deleteCallProg.toString().trim().replace("\r", ""),"function returnCall(f,y,z){\n"
         		+ "	delete x\n"
         		+ "	}");
         Assert.assertEquals(deleteInArrayCallProg.toString().trim().replace("\r", ""),"function nestedReturnCall(parameter,someOtherParameter){\n"
         		+ "	delete x k IN b\n"
         		+ "	}");
    }
    
    @Test
    public void testCompoundFunction() throws Exception {
         String returnCall = "BEGIN function returnCall(f,y,z){x = 5; while(x<19){x++; for(i; i<=2; i++){if(x<=i){x++}else if(x>=i){p++} else return x}}} END";

    	 Lexer returnCallLex = new Lexer(returnCall);

         returnCallLex.Lex();

         Parser returnCallParse = new Parser(returnCallLex.getTokens());

         ProgramNode returnCallProg = returnCallParse.parse();

         Assert.assertEquals(returnCallProg.toString().trim().replace("\r", ""),"function returnCall(f,y,z){\n"
         		+ "	x = 5	while(x LESSTHAN 19)\n"
         		+ "	POSTINCREMENT x for(i;i LESSTHANEQUAL 2;POSTINCREMENT i)\n"
         		+ "	if(x LESSTHANEQUAL i)\n"
         		+ "POSTINCREMENT x else	if(x GREATERTHANEQUAL i)\n"
         		+ "POSTINCREMENT p else	\n"
         		+ "return(x)   \n"
         		+ "	}");
    }
    
    @Test
    public void testBuiltInFunction() throws Exception {
         String getlineCall = "{getline}";
         String printCall = "{print}";
         String printfCall = "{printf}";
         String exitCall = "{exit}";
         String nextfileCall = "{nextfile}";
         String nextCall = "{next}";

    	 Lexer getlineCallLex = new Lexer(getlineCall);
    	 Lexer printCallLex = new Lexer(printCall);
    	 Lexer printfCallLex = new Lexer(printfCall);
    	 Lexer exitCallLex = new Lexer(exitCall);
    	 Lexer nextfileCallLex = new Lexer(nextfileCall);
    	 Lexer nextCallLex = new Lexer(nextCall);

    	 getlineCallLex.Lex();
    	 printCallLex.Lex();
    	 printfCallLex.Lex();
    	 exitCallLex.Lex();
    	 nextfileCallLex.Lex();
    	 nextCallLex.Lex();

         Parser getlineCallParse = new Parser(getlineCallLex.getTokens());
         Parser printCallParse = new Parser(printCallLex.getTokens());
         Parser printfCallParse = new Parser(printfCallLex.getTokens());
         Parser exitCallParse = new Parser(exitCallLex.getTokens());
         Parser nextfileCallParse = new Parser(nextfileCallLex.getTokens());
         Parser nextCallParse = new Parser(nextCallLex.getTokens());

         ProgramNode getlineCallProg = getlineCallParse.parse();
         ProgramNode printCallProg = printCallParse.parse();
         ProgramNode printfCallProg = printfCallParse.parse();
         ProgramNode exitCallProg = exitCallParse.parse();
         ProgramNode nextfileCallProg = nextfileCallParse.parse();
         ProgramNode nextCallProg = nextCallParse.parse();

         Assert.assertEquals(getlineCallProg.toString().trim().replace("\r", ""),"getline");
         Assert.assertEquals(printCallProg.toString().trim().replace("\r", ""),"print");
         Assert.assertEquals(printfCallProg.toString().trim().replace("\r", ""),"printf");
         Assert.assertEquals(exitCallProg.toString().trim().replace("\r", ""),"exit");
         Assert.assertEquals(nextfileCallProg.toString().trim().replace("\r", ""),"nextfile");
         Assert.assertEquals(nextCallProg.toString().trim().replace("\r", ""),"next");
    }
    
    @Test
    public void testCompoundBuiltInFunction() throws Exception {
         String getlineCall = "{for(i=0;i<5;i++) getline hh}";
         String printCall = "{if(x==0) print \"hello\"}";
         String printfCall = "{while(k==2) printf \"%-10s %s\n\", $1, $2}";
         String exitCall = "{if(x==2) k=2 else exit 5}";
         String nextfileCall = "BEGIN {print \"file content\" while((getline < fileName)>0){print $0} nextfile print \"more file content\" while((getline < otherFile) > 0) print $1}";
         String nextCall = "{if(x == y) {next} print $1}";

    	 Lexer getlineCallLex = new Lexer(getlineCall);
    	 Lexer printCallLex = new Lexer(printCall);
     	 Lexer printfCallLex = new Lexer(printfCall);
    	 Lexer exitCallLex = new Lexer(exitCall);
    	 Lexer nextfileCallLex = new Lexer(nextfileCall);
    	 Lexer nextCallLex = new Lexer(nextCall);

    	 getlineCallLex.Lex();
    	 printCallLex.Lex();
    	 printfCallLex.Lex();
    	 exitCallLex.Lex();
    	 nextfileCallLex.Lex();
    	 nextCallLex.Lex();

         Parser getlineCallParse = new Parser(getlineCallLex.getTokens());
         Parser printCallParse = new Parser(printCallLex.getTokens());
         Parser printfCallParse = new Parser(printfCallLex.getTokens());
         Parser exitCallParse = new Parser(exitCallLex.getTokens());
         Parser nextfileCallParse = new Parser(nextfileCallLex.getTokens());
         Parser nextCallParse = new Parser(nextCallLex.getTokens());

         ProgramNode getlineCallProg = getlineCallParse.parse();
         ProgramNode printCallProg = printCallParse.parse();
         ProgramNode printfCallProg = printfCallParse.parse();
         ProgramNode exitCallProg = exitCallParse.parse();
         ProgramNode nextfileCallProg = nextfileCallParse.parse();
         ProgramNode nextCallProg = nextCallParse.parse();

         Assert.assertEquals(getlineCallProg.toString().trim().replace("\r", ""),"for(i = 0;i LESSTHAN 5;POSTINCREMENT i)\n"
         		+ "	getline hh");
         Assert.assertEquals(printCallProg.toString().trim().replace("\r", ""),"if(x EQUAL 0)\n"
         		+ "print hello");
         Assert.assertEquals(printfCallProg.toString().trim().replace("\r", ""),"while(k EQUAL 2)\n"
         		+ "	printf %-10s %s\n"
         		+ " DOLLAR 1 DOLLAR 2");
         Assert.assertEquals(exitCallProg.toString().trim().replace("\r", ""),"if(x EQUAL 2)\n"
         		+ "k = 2 else	\n"
         		+ "exit 5");
         Assert.assertEquals(nextfileCallProg.toString().trim().replace("\r", ""),"print file content\n"
         		+ " while(getline \n"
         		+ " LESSTHAN fileName GREATERTHAN 0)\n"
         		+ "	print DOLLAR 0\n"
         		+ "  nextfile \n"
         		+ " CONCATENATION print more file content\n"
         		+ " while(getline \n"
         		+ " LESSTHAN otherFile GREATERTHAN 0)\n"
         		+ "	print DOLLAR 1");
         Assert.assertEquals(nextCallProg.toString().trim().replace("\r", ""),"if(x EQUAL y)\n"
         		+ "next \n"
         		+ "  print DOLLAR 1");
    }
    
    @Test(expected = Exception.class)
    public void testMissingWhileConditionExceptionCall() throws Exception {
        String exception = "BEGIN function returnCall(f,y,z){while()return x} END";
   	    Lexer exceptionLex = new Lexer(exception);
   	    exceptionLex.Lex();
        Parser exceptionParse = new Parser(exceptionLex.getTokens());
        ProgramNode exceptionProg = exceptionParse.parse();
    }
    
    @Test(expected = Exception.class)
    public void testMissingIfConditionExceptionCall() throws Exception {
        String exception = "BEGIN function returnCall(f,y,z){if()return x} END";
   	    Lexer exceptionLex = new Lexer(exception);
   	    exceptionLex.Lex();
        Parser exceptionParse = new Parser(exceptionLex.getTokens());
        ProgramNode exceptionProg = exceptionParse.parse();
    }
    
    @Test(expected = Exception.class)
    public void testMissingBraceConditionExceptionCall() throws Exception {
        String exception = "BEGIN function returnCall(f,y,z)if(x)return x}} END";
   	    Lexer exceptionLex = new Lexer(exception);
   	    exceptionLex.Lex();
        Parser exceptionParse = new Parser(exceptionLex.getTokens());
        ProgramNode exceptionProg = exceptionParse.parse();
    }
    
    @Test(expected = Exception.class)
    public void testMissingDeleteConditionExceptionCall() throws Exception {
        String exception = "BEGIN function returnCall(f,y,z){delete return x} END";
   	    Lexer exceptionLex = new Lexer(exception);
   	    exceptionLex.Lex();
        Parser exceptionParse = new Parser(exceptionLex.getTokens());
        ProgramNode exceptionProg = exceptionParse.parse();
    }
    
    @Test(expected = Exception.class)
    public void testMissingDoWhileConditionExceptionCall() throws Exception {
        String exception = "BEGIN function returnCall(f,y,z){do x++ while() return x} END";
   	    Lexer exceptionLex = new Lexer(exception);
   	    exceptionLex.Lex();
        Parser exceptionParse = new Parser(exceptionLex.getTokens());
        ProgramNode exceptionProg = exceptionParse.parse();
    }
    
    @Test(expected = Exception.class)
    public void testMissingOpenParaConditionExceptionCall() throws Exception {
        String exception = "BEGIN function returnCall(f,y,z){do x++ while) return x} END";
   	    Lexer exceptionLex = new Lexer(exception);
   	    exceptionLex.Lex();
        Parser exceptionParse = new Parser(exceptionLex.getTokens());
        ProgramNode exceptionProg = exceptionParse.parse();
    }
    
    @Test(expected = Exception.class)
    public void testInvalidStatementExceptionCall() throws Exception {
        String exception = "BEGIN function returnCall(f,y,z){do x+ while(x--) return x} END";
   	    Lexer exceptionLex = new Lexer(exception);
   	    exceptionLex.Lex();
        Parser exceptionParse = new Parser(exceptionLex.getTokens());
        ProgramNode exceptionProg = exceptionParse.parse();
    }
    
    @Test(expected = Exception.class)
    public void testInvalidFunctionCallExceptionCall() throws Exception {
        String exception = "BEGIN function returnCall(f,y,z){if(p==2){do return x while(x!=9)}else if(x) } END";
   	    Lexer exceptionLex = new Lexer(exception);
   	    exceptionLex.Lex();
        Parser exceptionParse = new Parser(exceptionLex.getTokens());
        ProgramNode exceptionProg = exceptionParse.parse();
    }
}
