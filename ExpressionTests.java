import org.junit.Assert;
import org.junit.Test;

public class ExpressionTests {
	@Test
    public void testBasicSingleExpression() throws Exception {
        String expression1 = "a++";
        String expression2 = "++$b";
        String expression3 = "(d++)";
        String expression4 = "-5";
        String expression5 = "`[abc]`";
        String expression6 = "e[b++]";
        String expression7 = "$7";
        
        Lexer exp1Lex = new Lexer(expression1);
        Lexer exp2Lex = new Lexer(expression2);
        Lexer exp3Lex = new Lexer(expression3);
        Lexer exp4Lex = new Lexer(expression4);
        Lexer exp5Lex = new Lexer(expression5);
        Lexer exp6Lex = new Lexer(expression6);
        Lexer exp7Lex = new Lexer(expression7);
        
        exp1Lex.Lex();
        exp2Lex.Lex();
        exp3Lex.Lex();
        exp4Lex.Lex();
        exp5Lex.Lex();
        exp6Lex.Lex();
        exp7Lex.Lex();
        
        Parser parsing1Test = new Parser(exp1Lex.getTokens());
        Parser parsing2Test = new Parser(exp2Lex.getTokens()); 
        Parser parsing3Test = new Parser(exp3Lex.getTokens()); 
        Parser parsing4Test = new Parser(exp4Lex.getTokens()); 
        Parser parsing5Test = new Parser(exp5Lex.getTokens()); 
        Parser parsing6Test = new Parser(exp6Lex.getTokens()); 
        Parser parsing7Test = new Parser(exp7Lex.getTokens());
        
        Assert.assertEquals(parsing1Test.parseLValue().get().toString(),"a PREINCREMENT");
        Assert.assertEquals(parsing2Test.parseLValue().get().toString(),"PREINCREMENT DOLLAR b");
        Assert.assertEquals(parsing3Test.parseLValue().get().toString(),"d PREINCREMENT");
        Assert.assertEquals(parsing4Test.parseLValue().get().toString(),"SUBTRACT 5");
        Assert.assertEquals(parsing5Test.parseLValue().get().toString(),"[abc]");
        Assert.assertEquals(parsing6Test.parseLValue().get().toString(),"e b PREINCREMENT");
        Assert.assertEquals(parsing7Test.parseLValue().get().toString(),"DOLLAR 7");
     }
}
