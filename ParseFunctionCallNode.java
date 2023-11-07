import java.util.LinkedList;

public class ParseFunctionCallNode extends StatementNode{

	private String functionName;
	private LinkedList<String> parameters;
	
	public ParseFunctionCallNode(String functionName, LinkedList<String> parameters) {
		this.functionName = functionName;
		this.parameters = parameters;
	}
	
	public String toString() {
		String parametersInList = "";
		if(parameters != null)
			for(String parameter: parameters)
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
