import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

public class FunctionNode extends Node{
	
	private String name;
	private LinkedList<String> parameters;
	private LinkedList<StatementNode> statements;
	
	public FunctionNode(String name, LinkedList<String> parameters, LinkedList<StatementNode> statements) {
		this.name = name;
		this.parameters = parameters;
		this.statements = statements;
	}
	
	protected FunctionNode(String name, LinkedList<String> parameters) {
		this.name = name;
		this.parameters = parameters;
		this.statements = null;
	}
	
	public FunctionNode() {
		this.name = null;
		this.parameters = null;
		this.statements = null;
	}
	public void addStatements(StatementNode statement) {
		statements.add(statement);
	}
	
	public String getName() {
		return name;
	}
	
	public LinkedList<String> getParameters(){
		return parameters;
	}
	
	public LinkedList<StatementNode> getStatements(){
		return statements;
	}
	
	public String toString() {
		String parametersInList = "";
		String statementsInList = "";
		if(parameters != null)
			for(String parameter : parameters)
				parametersInList += parameter + ",";
		try {
		parametersInList = parametersInList.substring(0, parametersInList.length()-1);
		}
		catch (Exception E){
		}
		if(statements != null)
			for(StatementNode statement : statements) {
				if(statement != null)
					statementsInList += "\t" + statement;
				}
		return ("function " + name + "(" + parametersInList + "){\n" + statementsInList + "\n\t}\n");
	}
	
}