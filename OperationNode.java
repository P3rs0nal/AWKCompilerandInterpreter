import java.util.Optional;

public class OperationNode extends StatementNode{
	
	private Optional<Node> leftNode;
	private Optional<Node> rightNode;
	possibleOperations operator;
	
	enum possibleOperations{
		EQUAL, NOTEQUAL, LESSTHAN, LESSTHANEQUAL, GREATERTHAN, GREATERTHANEQUAL, AND, OR, NOT, MATCH, NOTMATCH,
		DOLLAR, PREINCREMENT, POSTINCREMENT, PREDECREMENT, POSTDECREMENT, UNARYPOSITIVE,
		UNARYNEGATIVE, IN, EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, CONCATENATION
	}
	
	public OperationNode(Optional<Node> leftNode, Optional<Node> rightNode, possibleOperations operator) {
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		this.operator = operator;
	}
	
	public OperationNode(Optional<Node> rightNode, possibleOperations operator) {
		this.leftNode = Optional.empty();
		this.rightNode = rightNode;
		this.operator = operator;
	}
	
	public String toString() {
		if(leftNode.isEmpty() && rightNode.isEmpty())
			return operator.toString() + "";
		if(leftNode.isEmpty() && rightNode.isPresent())
			return operator.toString() + " " + rightNode.get().toString() + "";
		if(rightNode.isEmpty() && leftNode.isPresent())
			return operator.toString() + " " + leftNode.get().toString() + "";
		return leftNode.get().toString() + " " + operator.toString() + " " + rightNode.get().toString() + "";
	}
}
