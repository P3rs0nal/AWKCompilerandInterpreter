import java.util.Optional;

public class ParseReturnNode extends StatementNode{
	Optional<Node> returnValue;
	
	public ParseReturnNode(Optional<Node> returnValue) {
		this.returnValue = returnValue;
	}

	public String toString() {
		if(returnValue.isEmpty())
			return "return()";
		return "return("+returnValue.get()+")";
	}
}
