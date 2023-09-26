import java.util.Optional;

public class OperationNode extends Node{
	
	Optional<Node> leftNode;
	Optional<Node> rightNode;
	
	enum possibleOperations{
		EQUAL, NOTEQUAL, LESSTHAN, LESSTHANEQUAL, GREATTHAN, GREATERTHANEQUAL, AND, OR, NOT, MATCH, NOTMATCH,
		DOLLAR, PREINCREMENT, POSTINCREMENT, PREDECREMENT, POSTDECREMENT, UNARYPOSITIVE,
		UNARYNEGATIVE, IN, EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, CONCATENATION
	}
	
	public OperationNode(Optional<Node> leftNode, Optional<Node> rightNode) {
		this.leftNode = leftNode;
		this.rightNode = rightNode;
	}
	
	public String toString() {
		return leftNode.toString() + rightNode.toString();
	}
}
