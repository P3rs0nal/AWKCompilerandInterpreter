import java.util.Optional;

public class ParseWhileNode extends StatementNode{
	Optional<Node> condition;
	Optional<Node> body;
	
	public ParseWhileNode(Optional<Node> condition, Optional<Node> body) {
		this.condition = condition;
		this.body = body;
	}

	public String toString() {
		return "while("+ condition.get().toString() + ")\n\t" + body.get().toString();
	}
}
