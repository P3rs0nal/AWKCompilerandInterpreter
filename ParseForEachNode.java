
public class ParseForEachNode extends StatementNode{
	
	Node condition;
	Node body;
	
	public ParseForEachNode(Node condition, Node body) {
		this.condition = condition;
		this.body = body;
	}
	public String toString() {
		return ("\tfor(" + condition.toString() + ")\n\t") + body.toString();
	}
	
}
