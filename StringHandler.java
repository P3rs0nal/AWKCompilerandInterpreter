
public class StringHandler {
	private String AWKFile;
	private int indexFingerPosition = 0;
	public StringHandler(String file) {
		AWKFile = file;
	}
	
	char peek(int i) {
		if(i >= AWKFile.length()) // prevents peek from looking past the last character
			return ' ';		
		return AWKFile.charAt(i);
	}
	
	String peekString(int i) {
		if(indexFingerPosition+i >= AWKFile.length())
			return AWKFile.substring(indexFingerPosition, AWKFile.length());
		return AWKFile.substring(indexFingerPosition,indexFingerPosition+i);
	}
	
	char getChar() {
		char ret = AWKFile.charAt(indexFingerPosition);
		indexFingerPosition++;
		return ret;
	}
	
	void swallow(int i) {
		indexFingerPosition += i;
	}
	
	boolean isDone() {
		if(AWKFile.length()-1 < indexFingerPosition)
			return true;
		return false;
	}
	
	String remainder() {
		return AWKFile.substring(indexFingerPosition);
	}
}