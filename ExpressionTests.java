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
        
        Assert.assertEquals(parsing1Test.parseOperation().get().toString(),"POSTINCREMENT a");
        Assert.assertEquals(parsing2Test.parseOperation().get().toString(),"PREINCREMENT DOLLAR b");
        Assert.assertEquals(parsing3Test.parseOperation().get().toString(),"POSTINCREMENT d");
        Assert.assertEquals(parsing4Test.parseOperation().get().toString(),"UNARYNEGATIVE 5");
        Assert.assertEquals(parsing5Test.parseOperation().get().toString(),"[abc]");
        Assert.assertEquals(parsing6Test.parseOperation().get().toString(),"e POSTINCREMENT b");
        Assert.assertEquals(parsing7Test.parseOperation().get().toString(),"DOLLAR 7");
     }
	
	@Test
    public void testNoneNextedExpression() throws Exception {
        String ternary = "a?b:c";
        String assignment = "a = b";
        String doubleAssignment = "a += b";
        String or = "a||b";
        String and = "a&&b";
        String match = "abc ~ bce";
        String notMatch = "abce !~ peir";
        String greaterThan = "a > b";
        String lessThanEqual = "a <= b";
        String notEqual = "a != b";
        String stringConcat = "a b";
        String addition = "a + b";
        String multiplication = "a * b";
        String division = "a / b";
        String mod = "a % b";
        String exponent = "a ^ b";
        String array = "a[b]";
        String arrayIn = "a in b";
        
        Lexer exp1Lex = new Lexer(ternary);
        Lexer exp2Lex = new Lexer(assignment);
        Lexer exp3Lex = new Lexer(or);
        Lexer exp4Lex = new Lexer(doubleAssignment);
        Lexer exp5Lex = new Lexer(and);
        Lexer exp6Lex = new Lexer(match);
        Lexer exp7Lex = new Lexer(notMatch);
        Lexer exp8Lex = new Lexer(greaterThan);
        Lexer exp9Lex = new Lexer(lessThanEqual);
        Lexer exp10Lex = new Lexer(notEqual);
        Lexer exp11Lex = new Lexer(stringConcat);
        Lexer exp12Lex = new Lexer(addition);
        Lexer exp13Lex = new Lexer(multiplication);
        Lexer exp14Lex = new Lexer(division);
        Lexer exp15Lex = new Lexer(mod);
        Lexer exp16Lex = new Lexer(exponent);
        Lexer exp17Lex = new Lexer(array);
        Lexer exp18Lex = new Lexer(arrayIn);
        
        exp1Lex.Lex();
        exp2Lex.Lex();
        exp3Lex.Lex();
        exp4Lex.Lex();
        exp5Lex.Lex();
        exp6Lex.Lex();
        exp7Lex.Lex();
        exp8Lex.Lex();
        exp9Lex.Lex();
        exp10Lex.Lex();
        exp11Lex.Lex();
        exp12Lex.Lex();
        exp13Lex.Lex();
        exp14Lex.Lex();
        exp15Lex.Lex();
        exp16Lex.Lex();
        exp17Lex.Lex();
        exp18Lex.Lex();
        
        Parser parsing1Test = new Parser(exp1Lex.getTokens());
        Parser parsing2Test = new Parser(exp2Lex.getTokens()); 
        Parser parsing3Test = new Parser(exp3Lex.getTokens()); 
        Parser parsing4Test = new Parser(exp4Lex.getTokens()); 
        Parser parsing5Test = new Parser(exp5Lex.getTokens()); 
        Parser parsing6Test = new Parser(exp6Lex.getTokens()); 
        Parser parsing7Test = new Parser(exp7Lex.getTokens());
        Parser parsing8Test = new Parser(exp8Lex.getTokens());
        Parser parsing9Test = new Parser(exp9Lex.getTokens());
        Parser parsing10Test = new Parser(exp10Lex.getTokens());
        Parser parsing11Test = new Parser(exp11Lex.getTokens());
        Parser parsing12Test = new Parser(exp12Lex.getTokens());
        Parser parsing13Test = new Parser(exp13Lex.getTokens());
        Parser parsing14Test = new Parser(exp14Lex.getTokens());
        Parser parsing15Test = new Parser(exp15Lex.getTokens());
        Parser parsing16Test = new Parser(exp16Lex.getTokens());
        Parser parsing17Test = new Parser(exp17Lex.getTokens());
        Parser parsing18Test = new Parser(exp18Lex.getTokens());

        Assert.assertEquals(parsing1Test.parseOperation().get().toString(),"a?b:c");
        Assert.assertEquals(parsing2Test.parseOperation().get().toString(),"a = b");
        Assert.assertEquals(parsing3Test.parseOperation().get().toString(),"a OR b");
        Assert.assertEquals(parsing4Test.parseOperation().get().toString(),"a = a ADD b");
        Assert.assertEquals(parsing5Test.parseOperation().get().toString(),"a AND b");
        Assert.assertEquals(parsing6Test.parseOperation().get().toString(),"abc MATCH bce");
        Assert.assertEquals(parsing7Test.parseOperation().get().toString(),"abce NOTMATCH peir");
        Assert.assertEquals(parsing8Test.parseOperation().get().toString(),"a GREATERTHAN b");
        Assert.assertEquals(parsing9Test.parseOperation().get().toString(),"a LESSTHANEQUAL b");
        Assert.assertEquals(parsing10Test.parseOperation().get().toString(),"a NOTEQUAL b");
        Assert.assertEquals(parsing11Test.parseOperation().get().toString(),"a CONCATENATION b");
        Assert.assertEquals(parsing12Test.parseOperation().get().toString(),"a ADD b");
        Assert.assertEquals(parsing13Test.parseOperation().get().toString(),"a MULTIPLY b");
        Assert.assertEquals(parsing14Test.parseOperation().get().toString(),"a DIVIDE b");
        Assert.assertEquals(parsing15Test.parseOperation().get().toString(),"a MODULO b");
        Assert.assertEquals(parsing16Test.parseOperation().get().toString(),"a EXPONENT b");
        Assert.assertEquals(parsing17Test.parseOperation().get().toString(),"a b");
        Assert.assertEquals(parsing18Test.parseOperation().get().toString(),"a IN b");
     }
	
	@Test
    public void testCompoundExpression() throws Exception {
        String ternary = "a?b?c:d:e";
        String assignment = "a = b + c";
        String doubleAssignment = "a += (b+c)";
        String or = "a||b||c||d";
        String and = "a&&b&&c&&d";
        String match = "abc ~ bce";
        String notMatch = "abce !~ peir";
        String greaterThan = "a > b";
        String lessThanEqual = "a <= b";
        String notEqual = "a != b";
        String stringConcat = "a b c d e f g";
        String addition = "a + b + c + d + e";
        String multiplication = "a * b * c * e * f * g";
        String division = "a / b * c + d - e * f / g";
        String mod = "a % b";
        String exponent = "a ^ b ^ c ^ d";
        String array = "a[b + c]";
        String arrayIn = "a in b";
        String mixer = "(a + (b-c)) * (d^e) something";
        
        Lexer exp1Lex = new Lexer(ternary);
        Lexer exp2Lex = new Lexer(assignment);
        Lexer exp3Lex = new Lexer(or);
        Lexer exp4Lex = new Lexer(doubleAssignment);
        Lexer exp5Lex = new Lexer(and);
        Lexer exp6Lex = new Lexer(match);
        Lexer exp7Lex = new Lexer(notMatch);
        Lexer exp8Lex = new Lexer(greaterThan);
        Lexer exp9Lex = new Lexer(lessThanEqual);
        Lexer exp10Lex = new Lexer(notEqual);
        Lexer exp11Lex = new Lexer(stringConcat);
        Lexer exp12Lex = new Lexer(addition);
        Lexer exp13Lex = new Lexer(multiplication);
        Lexer exp14Lex = new Lexer(division);
        Lexer exp15Lex = new Lexer(mod);
        Lexer exp16Lex = new Lexer(exponent);
        Lexer exp17Lex = new Lexer(array);
        Lexer exp18Lex = new Lexer(arrayIn);
        Lexer exp19Lex = new Lexer(mixer);
        
        exp1Lex.Lex();
        exp2Lex.Lex();
        exp3Lex.Lex();
        exp4Lex.Lex();
        exp5Lex.Lex();
        exp6Lex.Lex();
        exp7Lex.Lex();
        exp8Lex.Lex();
        exp9Lex.Lex();
        exp10Lex.Lex();
        exp11Lex.Lex();
        exp12Lex.Lex();
        exp13Lex.Lex();
        exp14Lex.Lex();
        exp15Lex.Lex();
        exp16Lex.Lex();
        exp17Lex.Lex();
        exp18Lex.Lex();
        exp19Lex.Lex();
        
        Parser parsing1Test = new Parser(exp1Lex.getTokens());
        Parser parsing2Test = new Parser(exp2Lex.getTokens()); 
        Parser parsing3Test = new Parser(exp3Lex.getTokens()); 
        Parser parsing4Test = new Parser(exp4Lex.getTokens()); 
        Parser parsing5Test = new Parser(exp5Lex.getTokens()); 
        Parser parsing6Test = new Parser(exp6Lex.getTokens()); 
        Parser parsing7Test = new Parser(exp7Lex.getTokens());
        Parser parsing8Test = new Parser(exp8Lex.getTokens());
        Parser parsing9Test = new Parser(exp9Lex.getTokens());
        Parser parsing10Test = new Parser(exp10Lex.getTokens());
        Parser parsing11Test = new Parser(exp11Lex.getTokens());
        Parser parsing12Test = new Parser(exp12Lex.getTokens());
        Parser parsing13Test = new Parser(exp13Lex.getTokens());
        Parser parsing14Test = new Parser(exp14Lex.getTokens());
        Parser parsing15Test = new Parser(exp15Lex.getTokens());
        Parser parsing16Test = new Parser(exp16Lex.getTokens());
        Parser parsing17Test = new Parser(exp17Lex.getTokens());
        Parser parsing18Test = new Parser(exp18Lex.getTokens());
        Parser parsing19Test = new Parser(exp19Lex.getTokens());
        
        Assert.assertEquals(parsing1Test.parseOperation().get().toString(),"a?b?c:d:e");
        Assert.assertEquals(parsing2Test.parseOperation().get().toString(),"a = b ADD c");
        Assert.assertEquals(parsing3Test.parseOperation().get().toString(),"a OR b OR c OR d");
        Assert.assertEquals(parsing4Test.parseOperation().get().toString(),"a = a ADD b ADD c");
        Assert.assertEquals(parsing5Test.parseOperation().get().toString(),"a AND b AND c AND d");
        Assert.assertEquals(parsing6Test.parseOperation().get().toString(),"abc MATCH bce");
        Assert.assertEquals(parsing7Test.parseOperation().get().toString(),"abce NOTMATCH peir");
        Assert.assertEquals(parsing8Test.parseOperation().get().toString(),"a GREATERTHAN b");
        Assert.assertEquals(parsing9Test.parseOperation().get().toString(),"a LESSTHANEQUAL b");
        Assert.assertEquals(parsing10Test.parseOperation().get().toString(),"a NOTEQUAL b");
        Assert.assertEquals(parsing11Test.parseOperation().get().toString(),"a CONCATENATION b CONCATENATION c CONCATENATION d CONCATENATION e CONCATENATION f CONCATENATION g");
        Assert.assertEquals(parsing12Test.parseOperation().get().toString(),"a ADD b ADD c ADD d ADD e");
        Assert.assertEquals(parsing13Test.parseOperation().get().toString(),"a MULTIPLY b MULTIPLY c MULTIPLY e MULTIPLY f MULTIPLY g");
        Assert.assertEquals(parsing14Test.parseOperation().get().toString(),"a DIVIDE b MULTIPLY c ADD d SUBTRACT e MULTIPLY f DIVIDE g");
        Assert.assertEquals(parsing15Test.parseOperation().get().toString(),"a MODULO b");
        Assert.assertEquals(parsing16Test.parseOperation().get().toString(),"a EXPONENT b EXPONENT c EXPONENT d");
        Assert.assertEquals(parsing17Test.parseOperation().get().toString(),"a b ADD c");
        Assert.assertEquals(parsing18Test.parseOperation().get().toString(),"a IN b");
        Assert.assertEquals(parsing19Test.parseOperation().get().toString(),"a ADD b SUBTRACT c MULTIPLY d EXPONENT e CONCATENATION something");
     }
	
	@Test(expected = Exception.class)
    public void testExceptionRightPara() throws Exception {
       String missing = "a + (c";
       Lexer missingRight = new Lexer(missing);
       missingRight.Lex();
       Parser parseMissing = new Parser(missingRight.getTokens());
       parseMissing.parseOperation();
    }
	@Test(expected = Exception.class)
    public void testExceptionUnfinishedTern() throws Exception {
       String missing = "a?b";
       Lexer missingRight = new Lexer(missing);
       missingRight.Lex();
       Parser parseMissing = new Parser(missingRight.getTokens());
       parseMissing.parseOperation();
    }
	@Test(expected = Exception.class)
    public void testExceptionMissingRightBracket() throws Exception {
       String missing = "a + [c";
       Lexer missingRight = new Lexer(missing);
       missingRight.Lex();
       Parser parseMissing = new Parser(missingRight.getTokens());
       parseMissing.parseOperation();
    }
	@Test(expected = Exception.class)
    public void testExceptionMissingRighthandExpression() throws Exception {
       String missing = "a + ";
       Lexer missingRight = new Lexer(missing);
       missingRight.Lex();
       Parser parseMissing = new Parser(missingRight.getTokens());
       parseMissing.parseOperation();
    }
}
