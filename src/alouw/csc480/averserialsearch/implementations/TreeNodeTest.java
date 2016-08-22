package alouw.csc480.averserialsearch.implementations;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import alouw.csc480.adverserialsearch.interfaces.GameMove;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer.PlayerType;
import alouw.csc480.adverserialsearch.interfaces.GameState;
import alouw.csc480.adverserialsearch.interfaces.GameToken;
import alouw.csc480.adverserialsearch.interfaces.TreeNode;
import alouw.csc480.adverserialsearch.interfaces.TreeNode.NodeType;

public class TreeNodeTest {

	final GamePlayer human = GamePlayerFactory.newGamePlayer("Aubrey", GameToken.WHITE, PlayerType.HUMAN);
	final GamePlayer comp = GamePlayerFactory.newGamePlayer("LV9111", GameToken.BLACK, PlayerType.AI);
	final GameState gameBoard = GameStateFactory.getNewGameState(comp, human);
	
	@Test
	public void testRootNote() {
		TreeNode root = TreeNodeFactory.getNewRootNode(NodeType.MAX, gameBoard);
		
		Assert.assertEquals(gameBoard, root.getGameState());
		Assert.assertEquals(NodeType.MAX, root.getNodeType());
		Assert.assertTrue(root.isRootNode());
	}
	
	@Test
	public void testSuccessors() {
		TreeNode root = TreeNodeFactory.getNewRootNode(NodeType.MAX, gameBoard);
		TreeNode nextNode = root.getChildNode(GameMove.COLUMN_ONE);
		
		Assert.assertEquals(comp, nextNode.getGameState().getLastPlayer());
		Assert.assertEquals(0, nextNode.getGameState().getPlayerScore(human));
		Assert.assertEquals(0, nextNode.getGameState().getPlayerScore(comp));
		
		Assert.assertEquals(NodeType.MIN, nextNode.getNodeType());
		Assert.assertFalse(nextNode.isRootNode());
		
		List<TreeNode> nextNodes = nextNode.getChildrenNodes();
		Assert.assertEquals(GameMove.SIZE-1, nextNodes.size());
		
		nextNode = nextNodes.get(2).getChildNode(GameMove.COLUMN_ONE);
		Assert.assertEquals(NodeType.MIN, nextNode.getNodeType());
		Assert.assertEquals(comp, nextNode.getGameState().getLastPlayer());
		Assert.assertEquals(2, nextNode.getGameState().getPlayerScore(comp));
		Assert.assertEquals(0, nextNode.getGameState().getPlayerScore(human));
	}

	@Test
	public void testMiniMaxAssignmentSimple() {
		TreeNode root = TreeNodeFactory.getNewRootNode(NodeType.MAX, gameBoard);
		
		// MAX :: move by comp
		Assert.assertEquals(NodeType.MAX, root.getNodeType());
		TreeNode nextNode = root.getChildNode(GameMove.COLUMN_ONE);
		Assert.assertEquals(nextNode.getGameState().getLastPlayer(), comp);
		
		// MIN :: move by human
		Assert.assertEquals(root, nextNode.getParentNode());
		Assert.assertEquals(NodeType.MIN, nextNode.getNodeType());
		
		nextNode = nextNode.getChildNode(GameMove.COLUMN_TWO);
		Assert.assertEquals(nextNode.getGameState().getLastPlayer(), human);
		
		// MAX :: move by comp
		Assert.assertEquals(root, nextNode.getParentNode().getParentNode());
		Assert.assertEquals(NodeType.MAX, nextNode.getNodeType());
		
		nextNode = nextNode.getChildNode(GameMove.COLUMN_ONE);
		Assert.assertEquals(nextNode.getGameState().getLastPlayer(), comp);
		
		// MIN :: move by human
		Assert.assertEquals(root, nextNode.getParentNode().getParentNode().getParentNode());
		Assert.assertEquals(NodeType.MIN, nextNode.getNodeType());
		
		nextNode = nextNode.getChildNode(GameMove.COLUMN_FOUR);
		Assert.assertEquals(nextNode.getGameState().getLastPlayer(), human);
		
		// verify chain is correct
		Assert.assertEquals(root, nextNode.getParentNode().getParentNode().getParentNode().getParentNode());
		Assert.assertEquals(NodeType.MAX, nextNode.getNodeType());
		
		//Assert.assertEquals("*", nextNode.getGameState().printGameState());
		
		// backtrack min/max scores from each node
		Assert.assertEquals(2, nextNode.getGameState().getPlayerScore(comp));
		Assert.assertEquals(0, nextNode.getGameState().getPlayerScore(human));
		
		// verify backtracking works
		Assert.assertEquals(NodeType.MIN, nextNode.getParentNode().getNodeType());
		final int score = nextNode.getGameState().getPlayerScore(comp) - 
				nextNode.getGameState().getPlayerScore(human);
		Assert.assertEquals(2, score);
		
		nextNode.scoreLeafNode();
	}

	
	@Test
	public void testMiniMaxAssignmentComplex_MAX() {
		TreeNode root = TreeNodeFactory.getNewRootNode(NodeType.MAX, gameBoard);
		
		// MAX :: move by comp
		TreeNode nextNode = root.getChildNode(GameMove.COLUMN_ONE);
		
		// MIN :: move by human
		nextNode = nextNode.getChildNode(GameMove.COLUMN_TWO);
		Assert.assertEquals(NodeType.MAX, nextNode.getNodeType());
		
		// test that each child gets the correct score
		int firstMaxScore = 0;
		TreeNode firstMaxValueNode = null;
		for (TreeNode child: nextNode.getChildrenNodes()) {
			final int score = child.getGameState().getPlayerScore(comp) - child.getGameState().getPlayerScore(human);
			
			if (score > firstMaxScore) {
				firstMaxScore = score;
				firstMaxValueNode = child;
			}
		}
		
		// test that the max score was calculated and the node assigned 
		Assert.assertTrue(firstMaxValueNode != null);
		Assert.assertEquals(2, firstMaxScore);
		
		// MAX :: make the best move for comp
		nextNode = nextNode.getChildNode(firstMaxValueNode.getGameState().getLastGameMove());
		Assert.assertEquals(NodeType.MIN, nextNode.getNodeType());
		
		// test that the max score was calculated and the node assigned 
		Assert.assertEquals(2, nextNode.getGameState().getPlayerScore(comp));
		Assert.assertEquals(0, nextNode.getGameState().getPlayerScore(human));
		
		// test that each child gets the correct score
		int firstMinScore = -1000;
		TreeNode firstMinValueNode = null;
		for (TreeNode child: nextNode.getChildrenNodes()) {
			child.scoreLeafNode();
			final int score = child.getGameState().getPlayerScore(comp) - child.getGameState().getPlayerScore(human);
			
			if (score > firstMinScore) {
				firstMinScore = score;
				firstMinValueNode = child;
			}
		}
		
		// test that the max score was calculated and the node assigned 
		Assert.assertTrue(firstMinValueNode != null);
		Assert.assertEquals(2, firstMinScore);
	}
}
