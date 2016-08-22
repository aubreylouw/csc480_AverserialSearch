package alouw.csc480.averserialsearch.implementations;

import java.util.ArrayDeque;
import alouw.csc480.adverserialsearch.interfaces.SearchFunction;
import alouw.csc480.adverserialsearch.interfaces.TreeNode;

/*
 * The main driver class for navigating a set of tree nodes. 
 * 
 */
public class SearchTree {
	
	// queue to be traversed when searching for a solution
	private final ArrayDeque<TreeNode> searchTree = new ArrayDeque<>();
	
	// user supplied arguments to construct a SearchTree
	private final SearchFunction searchFunction;
	
	// the root node anchoring the searchTree
	private final TreeNode rootNode;
	
	// the maxinimum number of turns allowed (i.e. tree depth == plies * 2)
	private final int numPlies;

	SearchTree(final SearchFunction searchFunction, final TreeNode rootNode, final int numPlies) {
		this.searchFunction = searchFunction;
		this.rootNode = rootNode;
		this.numPlies = numPlies;
		this.searchTree.add(this.rootNode);
	}
	
	public TreeNode search() {
		return this.searchFunction.apply(this.searchTree, this.numPlies).get();
	}
}