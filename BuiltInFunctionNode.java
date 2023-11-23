import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.*;

public class BuiltInFunctionNode extends FunctionNode{
	boolean variadic;
	LinkedList<String> parameters = new LinkedList<String>();
	Function<HashMap<String,InterpreterDataType>, String> execute;
	
	public BuiltInFunctionNode(Function<HashMap<String,InterpreterDataType>, String> execute, boolean variadic) {
		this.execute = execute;
		this.variadic = variadic;
	}
	
	public String toString() {
		String ret = "";
		if(!parameters.isEmpty())
			for(String para: parameters)
				ret+= para;
		return "here" + ret;
	}
}
