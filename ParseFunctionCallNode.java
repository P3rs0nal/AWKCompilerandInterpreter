import java.util.LinkedList;

public class ParseFunctionCallNode extends StatementNode{

	private String functionName;
	private LinkedList<Node> parameters;
	
	public ParseFunctionCallNode(String functionName, LinkedList<Node> parameters) {
		this.functionName = functionName;
		this.parameters = parameters;
	}
	
	public String getFunction() {
		return functionName;
	}
	
	public LinkedList<Node> getStatements(){
		return parameters;
	}
	
	public String toString() {
		String parametersInList = "";
		if(parameters != null)
			for(Node parameter: parameters)
				parametersInList += parameter + " ";
			try{
				parametersInList = parametersInList.substring(0, parametersInList.length()-1);
			}
			catch (Exception E){
				
			}
		if(this.functionName == "getline" || this.functionName == "print" || this.functionName == "printf" || this.functionName == "exit" || this.functionName == "nextfile" || this.functionName == "next")
			return (functionName + " " + parametersInList + "\n");
		return functionName + "(" + parametersInList + ")";
	}

}
