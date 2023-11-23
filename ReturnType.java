
public class ReturnType {
	
	type typeOfRet;
	String returnValue;
	
	enum type {
		Normal, Break, Continue, Return
	} 
	
	ReturnType(type type){
		this.typeOfRet = type;
		this.returnValue = "";
	}
	
	ReturnType(type type, String returnValue){
		this.typeOfRet = type;
		this.returnValue = returnValue;
	}
	
	public String toString() {
		return (returnValue + " with type: " + typeOfRet);
	}
	
}
