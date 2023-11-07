
public class InterpreterDataType {
	private String data;
	
	public InterpreterDataType(String data) {
		this.data=data;
	}
	
	public String getType() {
		return data;
	}
	
	public void setType(String newData) {
		this.data = newData;
	}
	
	public InterpreterDataType() {
		this.data = "";
	}
	
	public String toString() {
		return data;
	}
}
