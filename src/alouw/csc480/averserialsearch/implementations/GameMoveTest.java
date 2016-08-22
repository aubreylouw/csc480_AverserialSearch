package alouw.csc480.averserialsearch.implementations;

import org.junit.Assert;
import org.junit.Test;

import alouw.csc480.adverserialsearch.interfaces.GameMove;

public class GameMoveTest {
	
	final int MIN = 0;
	final int MAX = 9;
	
	/* 
	 * Test that the valid moves are enumerated in the correct order &&
	 * 	that there are no moves that exceed the current MIN/MAX n*n board grid
	 */
	@Test
	public void testEnumerationOrder() {
		int initialOrdinal = -1;
		
		for (GameMove move : GameMove.values()) {
			if (!move.equals(GameMove.NONE)) Assert.assertTrue(move.ordinal() >= MIN && move.ordinal() < MAX);
			Assert.assertTrue(move.ordinal() + " > " + initialOrdinal, move.ordinal() > initialOrdinal);
			initialOrdinal = move.ordinal();
		}
		
		Assert.assertTrue(initialOrdinal == MAX);
		
		Assert.assertTrue(10 == GameMove.SIZE);
	}
}