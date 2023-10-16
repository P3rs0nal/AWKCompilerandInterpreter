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
		for(String parameter: parameters)
			parametersInList += parameter + " ";
		try {
			parametersInList = parametersInList.substring(0, parametersInList.length()-1);
			}
			catch (Exception E){
				
			}
		return functionName + "(" + parametersInList + ")";
	}

}
