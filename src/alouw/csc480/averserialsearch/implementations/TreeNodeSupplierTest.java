package alouw.csc480.averserialsearch.implementations;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import alouw.csc480.adverserialsearch.interfaces.GameMove;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer;
import alouw.csc480.adverserialsearch.interfaces.GameState;
import alouw.csc480.adverserialsearch.interfaces.GameToken;
import alouw.csc480.adverserialsearch.interfaces.TreeNode;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer.PlayerType;
import alouw.csc480.adverserialsearch.interfaces.TreeNode.NodeType;
import alouw.csc480.adverserialsearch.interfaces.TreeNodeSupplier;

public class TreeNodeSupplierTest {
	final GamePlayer human = GamePlayerFactory.newGamePlayer("Aubrey", GameToken.WHITE, PlayerType.HUMAN);
	final GamePlayer comp = GamePlayerFactory.newGamePlayer("LV9111", GameToken.BLACK, PlayerType.AI);
	final GameState gameBoard = GameStateFactory.getNewGameState(comp, human);
	
	@Test
	public void testHasNext() {
		TreeNode root = TreeNodeFactory.getNewRootNode(NodeType.MAX, gameBoard);
		TreeNodeSupplier supplier = new TreeNodeSupplierImpl(root);
		Assert.assertEquals(root, supplier.getSupplierNode());
		
		Set<GameMove> remainingMoves = new HashSet<>();
		for (GameMove move: GameMove.values()) remainingMoves.add(move);
		remainingMoves.remove(GameMove.NONE);
		GameMove move;
	
		Assert.assertTrue(supplier.hasNext());
		move = supplier.getNextChild().getGameState().getLastGameMove();
		Assert.assertTrue(remainingMoves.contains(move));
		remainingMoves.remove(move);
		
		Assert.assertTrue(supplier.hasNext());
		Assert.assertTrue(supplier.hasNext());
		Assert.assertTrue(supplier.hasNext());
		Assert.assertTrue(supplier.hasNext());
		move = supplier.getNextChild().getGameState().getLastGameMove();
		Assert.assertTrue(remainingMoves.contains(move));
		remainingMoves.remove(move);
		
		Assert.assertTrue(supplier.hasNext());
		move = supplier.getNextChild().getGameState().getLastGameMove();
		Assert.assertTrue(remainingMoves.contains(move));
		remainingMoves.remove(move);
		
		Assert.assertTrue(supplier.hasNext());
		move = supplier.getNextChild().getGameState().getLastGameMove();
		Assert.assertTrue(remainingMoves.contains(move));
		remainingMoves.remove(move);
		
		Assert.assertTrue(supplier.hasNext());
		move = supplier.getNextChild().getGameState().getLastGameMove();
		Assert.assertTrue(remainingMoves.contains(move));
		remainingMoves.remove(move);
		
		Assert.assertTrue(supplier.hasNext());
		move = supplier.getNextChild().getGameState().getLastGameMove();
		Assert.assertTrue(remainingMoves.contains(move));
		remainingMoves.remove(move);
		
		Assert.assertTrue(supplier.hasNext());
		move = supplier.getNextChild().getGameState().getLastGameMove();
		Assert.assertTrue(remainingMoves.contains(move));
		remainingMoves.remove(move);
		
		Assert.assertTrue(supplier.hasNext());
		move = supplier.getNextChild().getGameState().getLastGameMove();
		Assert.assertTrue(remainingMoves.contains(move));
		remainingMoves.remove(move);
		
		Assert.assertTrue(supplier.hasNext());
		move = supplier.getNextChild().getGameState().getLastGameMove();
		Assert.assertTrue(remainingMoves.contains(move));
		remainingMoves.remove(move);
		
		Assert.assertEquals(0, remainingMoves.size());
		
		try {
			Assert.assertFalse(supplier.hasNext());
			supplier.getNextChild();
			Assert.fail();
		} catch (NoSuchElementException nse) {
			
		}
	}
}
