import java.util.LinkedList;

public class FunctionCallNode extends StatementNode{

	private FunctionNode functionNode;
	private LinkedList<Node> statements;
	
	public FunctionCallNode(FunctionNode functionNode, LinkedList<Node> statements) {
		this.functionNode = functionNode;
		this.statements = statements;
	}
	
	public String toString() {
		return null;
	}

}
