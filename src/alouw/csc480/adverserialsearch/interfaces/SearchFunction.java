package alouw.csc480.adverserialsearch.interfaces;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;

import alouw.csc480.adverserialsearch.interfaces.TreeNode.NodeType;
import alouw.csc480.averserialsearch.implementations.TupleFactory;

/*
 * The search functions operate exclusively on tree nodes.
 */
public enum SearchFunction {
	MINIMAX {
		@Override
		public Optional<TreeNode> apply(final ArrayDeque<TreeNode> queue, final int numPlies) {
			final TreeNode rootNode = queue.pop();
			
			Tuple<Integer, TreeNode> result = SearchFunction.MinMaxRecursiveSearch(rootNode, numPlies);
			
			return Optional.ofNullable(result.getRightValue());
		}
	}, MINIMAX_WITH_PRUNING {
		@Override
		public Optional<TreeNode> apply(final ArrayDeque<TreeNode> queue, final int numPlies) {	
			final TreeNode rootNode = queue.pop();
			
			Tuple<Integer, TreeNode> result = SearchFunction.PruningMinMaxRecursiveSearch(rootNode, 
					numPlies, minValue, maxValue);
		
			return Optional.ofNullable(result.getRightValue());		}
	};
	
	/*
	 * A recursive DFS of the children (successor) nodes of an initial, root node. The search continues until the maximum'
	 * depth or until a terminal successor node is located (no legal moves remaining). The function returns a tuple of <score, node>
	 * where score represents the score of the entire subtree explored below the node.  
	 */
	private static Tuple<Integer, TreeNode> MinMaxRecursiveSearch (final TreeNode currentNode, 
			final int maxDepth) {
		
		Tuple<Integer, TreeNode> result =  null;
		final List<TreeNode> children = currentNode.getChildrenNodes();
		
		if (currentNode.getDepth() == maxDepth || children.isEmpty()) 
			result = TupleFactory.getNewTuple(Integer.valueOf(currentNode.scoreLeafNode()), currentNode);
		else {
			final NodeType currentNodeType = currentNode.getNodeType();
			switch (currentNodeType) {
				case MAX: {
					int bestValue = minValue;
					for (TreeNode child : children) {
						final int score = MinMaxRecursiveSearch(child, maxDepth).getLeftValue().intValue();
						if (score >= bestValue) {
							bestValue = score;
							result = TupleFactory.getNewTuple(Integer.valueOf(bestValue), child);
						} 
					}
					
				}; break;
				case MIN:{
					int bestValue = maxValue;
					for (TreeNode child : children) {
						final int score = MinMaxRecursiveSearch(child, maxDepth).getLeftValue().intValue();
						if (score <= bestValue) {
							bestValue = score;
							result = TupleFactory.getNewTuple(Integer.valueOf(bestValue), child);
						} 
					}
				}; break;
			}
		}	
			
		return result;
	}
	/*
	 * A recursive DFS of the children (successor) nodes of an initial, root node. The search continues until the maximum'
	 * depth, a terminal successor node is located (no legal moves remaining), or the algorithm detects that further subtrees can be 
	 * pruned (alpha/beta pruning). 
	 * 
	 * The function returns a tuple of <score, node> where score represents the score of the entire subtree explored below the node.  
	 */	
	private static Tuple<Integer, TreeNode> PruningMinMaxRecursiveSearch (final TreeNode currentNode, 
			final int maxDepth, int alphaValue, int betaValue) {
		Tuple<Integer, TreeNode> result =  null;
		final List<TreeNode> children = currentNode.getChildrenNodes();
		
		if (currentNode.getDepth() == maxDepth || children.isEmpty()) {
			result = TupleFactory.getNewTuple(Integer.valueOf(currentNode.scoreLeafNode()), currentNode);
		}
		else {
			final NodeType currentNodeType = currentNode.getNodeType();
			switch (currentNodeType) {
				case MAX: {
					int bestValue = minValue;
					for (TreeNode child : children) {
						final Tuple<Integer, TreeNode> tempResult = PruningMinMaxRecursiveSearch(child, 
								maxDepth, alphaValue, betaValue);
						final int score = tempResult.getLeftValue().intValue();
						if (score > bestValue) {
							bestValue = score;
							result = TupleFactory.getNewTuple(Integer.valueOf(bestValue), child);
						}
						
						if (bestValue >= betaValue) return result;
						alphaValue = Math.max(alphaValue, bestValue);
					}
				}; break;
				
				case MIN:{
					int bestValue = maxValue;
					for (TreeNode child : children) {
						final Tuple<Integer, TreeNode> tempResult = PruningMinMaxRecursiveSearch(child, 
								maxDepth, alphaValue, betaValue);
						final int score = tempResult.getLeftValue().intValue();
						if (score < bestValue) {
							bestValue = score;
							result = TupleFactory.
									getNewTuple(Integer.valueOf(bestValue), child);
						} 
						
						if (bestValue <= alphaValue) return result;
						betaValue = Math.min(betaValue, bestValue);
					}
				}; break;
			}
		}	
			
		return result;
	}
	
	
	public abstract Optional<TreeNode> apply(final ArrayDeque<TreeNode> searchTree, final int numPlies);
	private final static Integer maxValue = Integer.MAX_VALUE;
	private final static Integer minValue = Integer.MIN_VALUE;
}