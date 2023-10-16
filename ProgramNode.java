import java.util.LinkedList;

public class ProgramNode extends Node{
	private LinkedList<FunctionNode> functionNodes = new LinkedList<FunctionNode>();
	private LinkedList<BlockNode> startBlock = new LinkedList<BlockNode>();
	private LinkedList<BlockNode> block = new LinkedList<BlockNode>();
	private LinkedList<BlockNode> endBlock = new LinkedList<BlockNode>();
	
	public ProgramNode(LinkedList<FunctionNode> functions, LinkedList<BlockNode> start, LinkedList<BlockNode> block, LinkedList<BlockNode> end) {
		this.functionNodes = functions;
		this.startBlock = start;
		this.block = block;
		this.endBlock = end;
	}
	
	public LinkedList<FunctionNode> getFunctions(){
		return functionNodes;
	}
	
	public LinkedList<BlockNode> getStartBlocks(){
		return startBlock;
	}
	
	public LinkedList<BlockNode> getBlocks(){
		return block;
	}
	
	public LinkedList<BlockNode> getEndBlocks(){
		return endBlock;
	}
	
	public void setEndBlocks(BlockNode block){
		endBlock.add(block);
	}
	
	public void setFunctions(FunctionNode function){
		functionNodes.add(function);
	}
	
	public void setStartBlocks(BlockNode block){
		startBlock.add(block);
	}
	
	public void setBlocks(BlockNode block){
		this.block.add(block);
	}
	
	public String toString() {
		String ret = "";
		if(!functionNodes.isEmpty())
			for(Node f: functionNodes) {
				if(f != null)
				ret+= f.toString();
			}
		if(!block.isEmpty())
			for(Node b: block) {
				if(b != null)
				ret+= "" + b.toString();
			}
		if(!startBlock.isEmpty())
			for(Node b: startBlock) {
				if(b != null)
				ret+= "" + b.toString();
			}
		if(!endBlock.isEmpty())
			for(Node b: endBlock) {
				if(b != null)
				ret+= "" + b.toString();
			}
		return ret;
	}
}