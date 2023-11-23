import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) throws Exception {
		try {
			String wordFile = "testFile.txt";
			String codeName = "code.txt.txt";
			Path word = Paths.get(wordFile);
			Path code = Paths.get(codeName);
			String content = new String(Files.readAllBytes(code));
			Lexer fileLexer =  new Lexer(content);
			fileLexer.Lex();
			Parser fileParser = new Parser(fileLexer);
			ProgramNode prog = fileParser.parse();
			Interpereter fileInterpreter = new Interpereter(prog, word);
			fileInterpreter.interpretProgram(prog);
		}
		catch(Exception E){
			System.out.println(E.getMessage());
			System.out.println(E);
		}
	}
}