package alouw.csc480.adverserialsearch.interfaces;

import java.util.List;

/*
 * An immutable node that maintains pointers to its immediate parent and children. 
 * Each node encapsulates the following:
 * 	- the depth within the tree at which it was discovered; and
 *  - whether it has been explored yet; and
 *  - whether it is a root node; and
 *  - the immutable problem state within a problem space associated with this node 
 */

public interface TreeNode{
	
	// enumerated node types
	public enum NodeType {
		MIN {
			@Override
			public NodeType opposite() {
				return MAX;
			}
		}, MAX {
			@Override
			public NodeType opposite() {
				return MIN;
			}
		};
	
		public abstract NodeType opposite();
	};
	
	// the type of node represented
	public NodeType getNodeType();
	
	// a possibly empty list of children
	public List<TreeNode> getChildrenNodes();
	
	// request a new child node after executing a move
	public TreeNode getChildNode(GameMove move) throws IllegalArgumentException;
	
	// the parent node of the current node
	// current Node == getParentNode for ROOT
	public TreeNode getParentNode();
   
	// whether the current node is the root node
	public Boolean isRootNode();
   
	// returns an object modeling some external state 
	public GameState getGameState();
   
	// depth of node in tree
	public int getDepth();
   
	// evaluates/scores the leaf node
	public int scoreLeafNode();
}
