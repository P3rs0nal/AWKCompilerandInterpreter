public class ConstantNode extends Node{
	private String constantName;
	
	public ConstantNode(String inp) {
		constantName = inp;
	}
	
	public String toString() {
		return constantName;
	}
	
	public String getName() {
		return constantName;
	}
}
