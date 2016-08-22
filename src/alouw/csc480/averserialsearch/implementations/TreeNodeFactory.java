package alouw.csc480.averserialsearch.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import alouw.csc480.adverserialsearch.interfaces.GameMove;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer;
import alouw.csc480.adverserialsearch.interfaces.GameState;
import alouw.csc480.adverserialsearch.interfaces.TreeNode;
import alouw.csc480.adverserialsearch.interfaces.TreeNode.NodeType;

public final class TreeNodeFactory {
	
	public static TreeNode getNewRootNode(final NodeType nodeType, final GameState gameState) {
		return new TreeNodeImpl(nodeType, gameState);
	}
	
	public static TreeNode getNewTreeNode(final TreeNode parentNode, final GameState gameState) {
		return new TreeNodeImpl(parentNode, gameState);
	}
}

/*
 * An immutable tree node encapsulating the metadata a search algorithm would need. Internally the node uses a GameState
 * class to track the arcana of the game being played.
 */
class TreeNodeImpl implements TreeNode {
	private final boolean isRootNode;
	private final GameState GameState;
	private final TreeNode parentNode;
	private final int nodeDepth;
	private final NodeType nodeType;
	private final GamePlayer maxPlayer;
	private final GamePlayer minPlayer;
	private final int nodeID;
	private final int hashCode;
	
	TreeNodeImpl(final NodeType nodeType, final GameState state) {
		if (state == null) throw new IllegalArgumentException("GameState cannot be <null>");
		
		this.nodeType = nodeType;
		this.isRootNode = true;
		this.parentNode = this;
		this.GameState = state;
		this.nodeDepth = 0;
		this.maxPlayer = state.getNextPlayer();
		this.minPlayer = state.getLastPlayer();
		this.nodeID = 0;
		this.hashCode = this.calcHashCode();
	}
	
	TreeNodeImpl(final TreeNode parentNode, final GameState state) {
		if (parentNode== null) throw new IllegalArgumentException("ParentNode cannot be <null>");
		if (state == null) throw new IllegalArgumentException("GameState cannot be <null>");
		
		this.nodeType = parentNode.getNodeType().opposite();
		this.isRootNode = false;
		this.parentNode = parentNode;
		this.GameState = state;
		this.nodeDepth = 1 + this.parentNode.getDepth();
		this.maxPlayer = ((TreeNodeImpl) this.parentNode).minPlayer;
		this.minPlayer = ((TreeNodeImpl) this.parentNode).maxPlayer;
		this.nodeID = ((TreeNodeImpl) this.parentNode).nodeID + 1;
		this.hashCode = this.calcHashCode();
	}
	
	@Override
	public List<TreeNode> getChildrenNodes() {
		List<TreeNode> childrenNodes = new ArrayList<>();
					
		for (GameState gameState : this.GameState.getAllValidSucessorStates()) 
			childrenNodes.add(TreeNodeFactory.getNewTreeNode(this, gameState));
	
		return childrenNodes;
	}

	@Override
	public TreeNode getParentNode() {
		return this.parentNode;
	}

	@Override
	public Boolean isRootNode() {
		return this.isRootNode;
	}

	@Override
	public GameState getGameState() {
		return this.GameState;
	}

	@Override
	public int getDepth() {
		return this.nodeDepth;
	}

	public String toString() {
		final StringBuilder resultString = new StringBuilder();
		
		resultString.append(this.nodeID).append("_").append(this.nodeType).append("_").append(this.hashCode());
		
		return resultString.toString();
	}
	
	@Override
	public boolean equals(Object that){
		if (this == that) return true;
		if (!(that instanceof TreeNodeImpl)) return false;
		
		final TreeNodeImpl thatNode = (TreeNodeImpl) that;
		
		boolean result = Objects.equals(this.getGameState(), thatNode.getGameState());
		result = result && this.nodeID == thatNode.nodeID;
		result = result && this.nodeType.equals(thatNode.nodeType);
		
		return result;
	}
	
	private int calcHashCode() {
		return Objects.hash(this.GameState, this.nodeID, 
				this.nodeType, this.GameState.getLastGameMove());
	}
	
	@Override 
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public NodeType getNodeType() {
		return this.nodeType;
	}

	@Override
	public TreeNode getChildNode(final GameMove move) {
		if (!this.GameState.isMoveLegal(move)) throw new IllegalArgumentException(move + " is not a legal move.");
		
		return TreeNodeFactory.getNewTreeNode(this, 
				GameStateFactory.getSuccessorGameState(this.GameState.getNextPlayer(), move, this.GameState));
	}

	@Override
	public int scoreLeafNode() {
		final GamePlayer maxPlayer;
		final GamePlayer minPlayer;
		final GameState game = this.GameState;
		
		switch (this.nodeType) {
			case MAX: {
				maxPlayer = game.getNextPlayer();
				minPlayer = game.getLastPlayer();
			}; break;
			default : {
				maxPlayer = game.getLastPlayer();
				minPlayer = game.getNextPlayer();
			}; break;
		}
		
		return game.getPlayerScore(maxPlayer) - game.getPlayerScore(minPlayer);
	}
}
