import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) throws Exception {
		try {
		Path myPath = Paths.get(args[0]);
		String content = new String(Files.readAllBytes(myPath));
		Lexer fileLexer =  new Lexer(content);
		fileLexer.Lex();
		System.out.println(fileLexer);
		}
		catch(Exception E){
			System.out.println(E.getMessage());
			System.out.println(E);
		}
	}
}