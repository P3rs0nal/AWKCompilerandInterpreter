
public class ParseIfNode extends StatementNode{
	
	Node condition;
	Node operation;
	ParseIfNode next;
	
	public ParseIfNode(Node condition, Node operation, ParseIfNode next) {
		this.condition = condition;
		this.operation = operation;
		this.next = next;
	}
	public ParseIfNode(Node condition, Node operation) {
		this.condition = condition;
		this.operation = operation;
		this.next = null;
	}
	
	public String toString() {
		if(next != null)
			return("if(" + condition + ")\n" + operation + "else\t" + next.toString());
		if(condition == null)
			return ("\n" +operation.toString());
		return ("if(" + condition + ")\n" + operation);
	}
}
