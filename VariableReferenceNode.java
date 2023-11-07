import java.util.Optional;

public class VariableReferenceNode extends Node{
	private String varName;
	private Optional<Node> expression;
	
	public VariableReferenceNode(String varName, Optional<Node> expression) {
		this.varName = varName;
		this.expression = expression;
	}
	
	public VariableReferenceNode(String varName) {
		this.varName = varName;
		this.expression = Optional.empty();
	}
	
	public String getName() {
		return varName;
	}
	
	public Optional<Node> getExpression(){
		return expression;
	}
	
	public String toString() {
		if(expression.isEmpty())
			return varName;
		return varName.toString() + " " + expression.get().toString() + "";
	}
	
}
