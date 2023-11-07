import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType{
	private HashMap<String, InterpreterDataType> variableStorage = new HashMap<String,InterpreterDataType>();
	
	public InterpreterArrayDataType(HashMap<String, InterpreterDataType> variableStorage) {
		this.variableStorage = variableStorage;
	}
	
	public HashMap<String, InterpreterDataType> getVariables(){
		return variableStorage;
	}
	
	public String toString() {
		return variableStorage.toString();
	}
}

