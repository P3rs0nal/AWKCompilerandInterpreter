public class PatternNode extends Node{
	private String pattern;
	
	public PatternNode(String inp) {
		pattern = inp;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public String toString() {
		return pattern;
	}
}
