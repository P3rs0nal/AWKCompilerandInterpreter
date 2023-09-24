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
		String functions = "";
		String starts = "";
		String midBlocks = "";
		String ends = "";
		for(FunctionNode function : functionNodes)
			functions += function.toString();
		for(BlockNode start : startBlock)
			starts += start.toString();
		for(BlockNode block : startBlock)
			midBlocks += block.toString();
		for(BlockNode end : startBlock)
			ends += end.toString();
		return (starts + functions + midBlocks + ends);
	}
}