import java.util.Optional;

public class ParseForNode extends StatementNode{

	Optional<Node> variable;
	Optional<Node> condition;
	Optional<Node> delimiter;
	Optional<Node> body;
	
	public ParseForNode(Optional<Node> variable, Optional<Node> condition, Optional<Node> delimiter, Optional<Node> body) {
		this.variable = variable;
		this.condition = condition;
		this.delimiter = delimiter;
		this.body = body;
	}

	public String toString() {
		return ("for(" + variable.get().toString() + ";" + condition.get().toString() + ";" + delimiter.get().toString() + ")" + "\n\t" + body.get().toString());
	}
}
