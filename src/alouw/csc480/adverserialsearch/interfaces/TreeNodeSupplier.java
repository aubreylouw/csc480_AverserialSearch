package alouw.csc480.adverserialsearch.interfaces;

public interface TreeNodeSupplier {

	// there are more children to retrieve from the supplying TreeNode
	public boolean hasNext();
	
	// get the next child ; throws NoSuchElementException if hasNext() is false
	public TreeNode getNextChild();
	
	// get the wrapped TreeNode
	public TreeNode getSupplierNode();
}
