
public class TernaryNode extends Node{
	Node left;
	Node right;
	Node center;
	public TernaryNode(Node left, Node center, Node right) {
		this.left = left;
		this.right = right;
		this.center = center;
	}
	
	public String toString() {
		return (left + "?" + center + ":" + right);
	}
}
