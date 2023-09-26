import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

public class unitTests{
	
    @Test
    public void testLexer() throws Exception {
        String empty = "";
        String notEmpty = "This is a sentence";
        String number = "123 333 123.3";
        String mixed = "These are words and numbers 123 6.4231 321 word2with3numbers";
        String mixedNewLine = "These are words and numbers 123 6.4231 321 word2with3numbers \n";
        String mixedNewLine2 = "These are words and numbers \n 123 .64231 321 word2with3numbers \n";
        String mixedNewLine3 = "These are words and numbers \r 123 .64231 321 word2with3numbers \n";
        
        Lexer emptyLex =  new Lexer(empty);
        Lexer sentenceLex =  new Lexer(notEmpty);
        Lexer numberLex =  new Lexer(number);
        Lexer mixedLex = new Lexer(mixed);
        Lexer newLineLex = new Lexer(mixedNewLine);
        Lexer newLineLex2 = new Lexer(mixedNewLine2);
        Lexer newLineLex3 = new Lexer(mixedNewLine3);
        
        emptyLex.Lex();
        sentenceLex.Lex();
        numberLex.Lex();
        mixedLex.Lex();
        newLineLex.Lex();
        newLineLex2.Lex();
        newLineLex3.Lex();
        
        Assert.assertTrue(emptyLex.toString().equals("[SEPARATOR]"));
        Assert.assertTrue(sentenceLex.toString().compareTo("[WORD (This), WORD (is), WORD (a), WORD (sentence), SEPARATOR]")==0);
        Assert.assertTrue(numberLex.toString().equals("[NUMBER (123), NUMBER (333), NUMBER (123.3), SEPARATOR]"));
        Assert.assertTrue(mixedLex.toString().equals("[WORD (These), WORD (are), WORD (words), WORD (and), WORD (numbers), NUMBER (123), NUMBER (6.4231), NUMBER (321), WORD (word2with3numbers), SEPARATOR]"));
        Assert.assertTrue(newLineLex.toString().equals("[WORD (These), WORD (are), WORD (words), WORD (and), WORD (numbers), NUMBER (123), NUMBER (6.4231), NUMBER (321), WORD (word2with3numbers), SEPARATOR, SEPARATOR]"));
        Assert.assertTrue(newLineLex2.toString().equals("[WORD (These), WORD (are), WORD (words), WORD (and), WORD (numbers), SEPARATOR, NUMBER (123), NUMBER (.64231), NUMBER (321), WORD (word2with3numbers), SEPARATOR, SEPARATOR]"));
        Assert.assertTrue(newLineLex3.toString().equals("[WORD (These), WORD (are), WORD (words), WORD (and), WORD (numbers), NUMBER (123), NUMBER (.64231), NUMBER (321), WORD (word2with3numbers), SEPARATOR, SEPARATOR]"));
    }
    @Test(expected = Exception.class)
    public void testIndexUnexpectedCharacterException() throws Exception {
        String error = "Here is a sentence with an un&acceptable characters.:3]2lfe/;ec,q=21";
        Lexer errorLex = new Lexer(error);
        errorLex.Lex();
    }
    @Test
    public void testSymbolsAndStringLiteral() throws Exception {
        String notEmpty = "This is a sentence && \"hello\"";
        String number = "123 333 123.3 = 23 test";
        String mixed = "These are words and symbols with some numbers 123 6.4231 321 word2with3numbers && ^ #donotreadthis \n next line () ";
        String symbols = "$ * ( { / + - = ! ; :";
        
        Lexer sentenceLex =  new Lexer(notEmpty);
        Lexer numberLex =  new Lexer(number);
        Lexer mixedLex = new Lexer(mixed);
        Lexer symbolsLex = new Lexer(symbols);
        
        sentenceLex.Lex();
        numberLex.Lex();
        mixedLex.Lex();
        symbolsLex.Lex();
        
        Assert.assertTrue(sentenceLex.toString().compareTo("[WORD (This), WORD (is), WORD (a), WORD (sentence), DOUBLEAND, STRINGLITERAL (hello), SEPARATOR]")==0);
        Assert.assertTrue(numberLex.toString().equals("[NUMBER (123), NUMBER (333), NUMBER (123.3), EQUALTO, NUMBER (23), WORD (test), SEPARATOR]"));
        Assert.assertTrue(mixedLex.toString().equals("[WORD (These), WORD (are), WORD (words), WORD (and), "
        		+ "WORD (symbols), WORD (with), WORD (some), WORD (numbers), NUMBER (123), NUMBER (6.4231), "
        		+ "NUMBER (321), WORD (word2with3numbers), DOUBLEAND, POWERTO, SEPARATOR, NEXT, WORD (line), OPENPARENTHSIS, CLOSEPARENTHSIS, SEPARATOR]"));
        Assert.assertTrue(symbolsLex.toString().equals("[DOLLARSIGN, STAR, OPENPARENTHSIS, OPENBRACE, FORWARDSLASH, PLUS, MINUS, EQUALTO, EXCLAMATION, SEPARATOR, COLON, SEPARATOR]"));
    }
    @Test
    public void testPatternandDoubleSymbols() throws Exception {
         String doubleSymbols = "++ -- += -= *= ^= <= == >=";
         String mixedSymbols = "++ -- == += % ^ ^= -= - = + = *= && || ] (3&&4||5)";
         String quotes = "she said \"hello world\" and more";
         String quoteAndPattern = "she said \"hello world\" like 3 times `hello world`";
         
         Lexer symbolsLex = new Lexer(doubleSymbols);
         Lexer mixedLex = new Lexer(mixedSymbols);
         Lexer doubleQuotesLex = new Lexer(quotes);
         Lexer patternLex = new Lexer(quoteAndPattern);
         
         symbolsLex.Lex();
         mixedLex.Lex();
         doubleQuotesLex.Lex();
         patternLex.Lex();
         
         Assert.assertTrue(symbolsLex.toString().equals("[INCREMENT, DECREMENT, ADDTO, SUBTRACTTO, MULTIPLYTO, RAISETO, LESSTHANEQUALTO, ISEQUALTO, GREATTHANEQUALTO, SEPARATOR]"));
         Assert.assertTrue(mixedLex.toString().equals("[INCREMENT, DECREMENT, ISEQUALTO, ADDTO, MODULO, POWERTO, RAISETO, SUBTRACTTO, MINUS, EQUALTO, PLUS, EQUALTO, MULTIPLYTO, DOUBLEAND, DOUBLEOR, CLOSEBRACKET, OPENPARENTHSIS, NUMBER (3), DOUBLEAND, NUMBER (4), DOUBLEOR, NUMBER (5), CLOSEPARENTHSIS, SEPARATOR]"));
         Assert.assertTrue(doubleQuotesLex.toString().equals("[WORD (she), WORD (said), STRINGLITERAL (hello world), WORD (and), WORD (more), SEPARATOR]"));
         Assert.assertTrue(patternLex.toString().equals("[WORD (she), WORD (said), STRINGLITERAL (hello world), WORD (like), NUMBER (3), WORD (times), PATTERN (hello world), SEPARATOR]"));
    }
    @Test
    public void testComments() throws Exception {
         String comment = "some words and numbers 3123 3.2 int i = 0; i--; i++; i==i #do not read this\n";
         String comment2 = "# absoulte nonsense 03498c23nck23jn;foijewcfo;234f34p2imfnionc23 \n";
         String comment3 = "# absoulte nonsense 03498c23nck23jn;foijewcfo;234f34p2imfnionc23 \n read this 3";
         
         Lexer commentLex = new Lexer(comment);
         Lexer comment2Lex = new Lexer(comment2);
         Lexer comment3Lex = new Lexer(comment3);
         
         commentLex.Lex();
         comment2Lex.Lex();
         comment3Lex.Lex();

         Assert.assertTrue(commentLex.toString().equals("[WORD (some), WORD (words), WORD (and), WORD (numbers), NUMBER (3123), NUMBER (3.2), WORD (int), WORD (i), EQUALTO, NUMBER (0), SEPARATOR, WORD (i), DECREMENT, SEPARATOR, WORD (i), INCREMENT, SEPARATOR, WORD (i), ISEQUALTO, WORD (i), SEPARATOR, SEPARATOR]"));
         Assert.assertTrue(comment2Lex.toString().equals("[SEPARATOR, SEPARATOR]"));
         Assert.assertTrue(comment3Lex.toString().equals("[SEPARATOR, WORD (read), WORD (this), NUMBER (3), SEPARATOR]"));

    }
    @Test
    public void testPeekandMatchandRemoveandMoreTokens() throws Exception {
         String function = "function factorial(f){"
         		+ " "
         		+ " if (f<=1) return 1;"
         		+ " return f*factorial(f-1);"
         		+ " }";
         
         Lexer functionLex = new Lexer(function);
         
         functionLex.Lex();
         
         Assert.assertTrue(functionLex.toString().equals("[FUNCTION, WORD (factorial), OPENPARENTHSIS, WORD (f), CLOSEPARENTHSIS, OPENBRACE, IF, OPENPARENTHSIS, WORD (f), LESSTHANEQUALTO, NUMBER (1), CLOSEPARENTHSIS, RETURN, NUMBER (1), SEPARATOR, RETURN, WORD (f), STAR, WORD (factorial), OPENPARENTHSIS, WORD (f), MINUS, NUMBER (1), CLOSEPARENTHSIS, SEPARATOR, CLOSEBRACE, SEPARATOR]"
         		+ ""));
         
         TokenManager tokenMNG = new TokenManager(functionLex.getTokens());
         Assert.assertEquals(tokenMNG.toString(),"[FUNCTION, WORD (factorial), OPENPARENTHSIS, WORD (f), CLOSEPARENTHSIS, OPENBRACE, IF, OPENPARENTHSIS, WORD (f), LESSTHANEQUALTO, NUMBER (1), CLOSEPARENTHSIS, RETURN, NUMBER (1), SEPARATOR, RETURN, WORD (f), STAR, WORD (factorial), OPENPARENTHSIS, WORD (f), MINUS, NUMBER (1), CLOSEPARENTHSIS, SEPARATOR, CLOSEBRACE, SEPARATOR]");
         //loop through the list of tokens, peeking at 0 should match matchAndRemove, function self-increments so moreTokens
         //should also stop when the entire list has been processed
         while(tokenMNG.moreTokens())
        	 Assert.assertEquals(tokenMNG.peek(0).toString(),(tokenMNG.matchAndRemove(tokenMNG.peek(0).get().getTokenType()).toString()));
         Assert.assertFalse(tokenMNG.moreTokens());
    }
    @Test
    public void testAcceptSeperators() throws Exception {
        String function = "function factorial(f){"
        		+ " "
        		+ " "
        		+ "\n "
        		+ "\n "
        		+ ";;;; "
        		+ ";; "
        		+ "\n "
        		+ "if (f<=1) return 1;"
        		+ " return f*factorial(f-1);"
        		+ " }";
        
        Lexer functionLex = new Lexer(function);
        
        functionLex.Lex();
        
        Parser parsingTest = new Parser(functionLex.getTokens());
        
        while(!parsingTest.getTokens().isEmpty()) {
        	if(parsingTest.getTokens().peek().getTokenType().equals(Token.Tokens.SEPARATOR))
              	 Assert.assertTrue((parsingTest.acceptSeperators()));
        	else
        		parsingTest.getTokens().removeFirst(); //remove non-separator tokens to allow acceptSeparators to be tested multiple times
        }   
     }
    @Test
    public void testParseFunctionandParseAction() throws Exception {
    	/* due to being unable to handle statements, the tests will consistent of 
    	 * functions with empty bodies
    	 * 
    	 * since parseAction does not consist of any "false" condition, there is no way to test
    	 * exception throwing since the function will infinitely loop (parseAction will always return true)
    	 */
         String function = "BEGIN function factorial(f,y,z){ } function testFunction(parameter, someOtherParameter){} END";
         
         Lexer functionLex = new Lexer(function);
         
         functionLex.Lex();
         
         Parser parsingFunctionOne = new Parser(functionLex.getTokens());
         
         ProgramNode testerProgOne = parsingFunctionOne.parse();
         
         System.out.println(testerProgOne);
         Assert.assertEquals(testerProgOne.toString(),"function factorial(f,y,z){\n"
         		+ "\tnull\n}\n"
         		+ "function testFunction(parameter,someOtherParameter){\n"
         		+ "\tnull\n}\n");   
    }
}