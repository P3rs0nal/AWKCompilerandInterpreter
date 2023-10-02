import java.util.LinkedList;

public class FunctionNode extends Node{
	
	private String name;
	private LinkedList<String> parameters;
	private LinkedList<StatementNode> statements;
	
	public FunctionNode(String name, LinkedList<String> parameters, LinkedList<StatementNode> statements) {
		this.name = name;
		this.parameters = parameters;
		this.statements = statements;
	}
	
	public String toString() {
		String parametersInList = "";
		String statementsInList = "";
		for(String parameter : parameters)
			parametersInList += parameter + ",";
		try {
		parametersInList = parametersInList.substring(0, parametersInList.length()-1);
		}
		catch (Exception E){
			
		}
		for(StatementNode statement : statements)
			statementsInList += statement;
		return ("function " + name + "(" + parametersInList + "){\n" + "\t" + statementsInList + "\n}\n");
	}
	
}