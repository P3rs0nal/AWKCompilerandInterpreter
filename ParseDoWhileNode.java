import java.util.Optional;

public class ParseDoWhileNode extends StatementNode{
	
	Optional<Node> condition;
	Optional<Node> body;

	public ParseDoWhileNode(Optional<Node> condition, Optional<Node> body) {
		this.condition = condition;
		this.body = body;
	}
	
	public String toString() {
		return ("do\n\t" + body.get().toString() + "\n\twhile(" + condition.get().toString() + ")");
	}
	
}
