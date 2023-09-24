import java.util.LinkedList;
import java.util.Optional;

public class BlockNode extends Node{
	
	private LinkedList<StatementNode> statements = new LinkedList<StatementNode>();
	private Optional<Node> condition = null;
	
	public BlockNode(LinkedList<StatementNode> statements, Optional<Node> condition) {
		this.statements = statements;
		this.condition = condition;
	}
	
	public LinkedList<StatementNode> getStatements(){
		return statements;
	}
	
	public Optional<Node> getCondition(){
		return condition;
	}
	
	public void setStatements(StatementNode statements){
		this.statements.add(statements);
	}
	
	public void setCondition(Optional<Node> condition){
		this.condition = condition;
	}
	
	public String toString() {
		String statementsInList = "";
		for(StatementNode statement : statements)
			statementsInList += statement;
		//since conditions is currently null, there is nothing to show but statements
		return statementsInList;
	}
}