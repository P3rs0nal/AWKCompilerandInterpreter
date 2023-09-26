import java.util.Optional;

public class VariableReferenceNode extends Node{
	String varName;
	Optional<Node> expression;
	TokenManager tk = new TokenManager(null);
	
	public VariableReferenceNode(String varName, Optional<Node> expression) {
		this.varName = varName;
		this.expression = expression;
	}
	
	public Optional<Node> parseBottomLevel(){
		// string literal
		ConstantNode node = new ConstantNode();
		return null;
	}
	
	public Optional<Node> parseLValue(){
		// dollar + parseBLVL
		OperationNode node = new OperationNode(parseBottomLevel(), expression);
		// word + openar + parseOp + closearr
		VariableReferenceNode node2 = new VariableReferenceNode(varName, expression);
		// word
		VariableReferenceNode node3 = new VariableReferenceNode(varName, null);
		return null;
	}
	
	public String toString() {
		return varName + expression.toString();
	}
	
}
