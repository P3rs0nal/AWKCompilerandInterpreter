import java.util.Optional;

public class ParseDeleteNode extends StatementNode{
	Optional<Node> arrayName;
	
	public ParseDeleteNode(Optional<Node> arrayName) {
		this.arrayName = arrayName;
	}

	public String toString() {
		return "delete " + arrayName.get().toString();
	}
}
