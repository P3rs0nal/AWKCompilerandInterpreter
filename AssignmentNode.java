
public class AssignmentNode extends StatementNode{
	Node target;
	Node expression;
	
	public AssignmentNode(Node target, Node expression) {
		this.target = target;
		this.expression = expression;
	}
	
	public String toString() {
		return (target + " = " + expression + "");
	}
}
