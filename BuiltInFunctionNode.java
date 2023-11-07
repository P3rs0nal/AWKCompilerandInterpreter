import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.*;

public class BuiltInFunctionNode extends FunctionNode{
	boolean variadic;
	String name;
	LinkedList<String> parameters;
	Function<HashMap<String,InterpreterDataType>, String> execute;
	
	public BuiltInFunctionNode(Function<HashMap<String,InterpreterDataType>, String> execute, boolean variadic) {
		this.execute = execute;
		this.variadic = variadic;
	}
	
	public String toString() {
		return name;
	}
}
