package alouw.csc480.averserialsearch.implementations;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import alouw.csc480.adverserialsearch.interfaces.GamePlayer;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer.PlayerType;
import alouw.csc480.adverserialsearch.interfaces.GameToken;

public class GamePlayerTest {

	@Rule
	public ExpectedException genericException = ExpectedException.none();
	
	@Test
	public void testConstructorInvalidArguments() {
		genericException.expect(IllegalArgumentException.class);
		GamePlayerFactory.newGamePlayer(null, null, null);
		Assert.fail();
	}
	
	public void testConstructorValidArguments() {
		GamePlayer player1 = GamePlayerFactory.newGamePlayer("Bob", GameToken.WHITE, PlayerType.AI);
		GamePlayer player2 = GamePlayerFactory.newGamePlayer("Bob", GameToken.BLACK, PlayerType.HUMAN);
		
		Assert.assertEquals("Bob", player1.getPlayerName());
		Assert.assertEquals(GameToken.WHITE, player1.getPlayerToken());
		Assert.assertEquals("Bob", player2.getPlayerName());
		Assert.assertEquals(GameToken.BLACK, player1.getPlayerToken());
		
		Assert.assertNotEquals(player1, player2);
		Assert.assertEquals(GamePlayerFactory.newGamePlayer("Susan", GameToken.WHITE, PlayerType.AI),
				GamePlayerFactory.newGamePlayer("Susan", GameToken.WHITE, PlayerType.AI));
		Assert.assertEquals(GamePlayerFactory.newGamePlayer("Susan", GameToken.WHITE, PlayerType.AI).hashCode(),
				GamePlayerFactory.newGamePlayer("Susan", GameToken.WHITE, PlayerType.AI).hashCode());
		
		Assert.assertNotEquals(GamePlayerFactory.newGamePlayer("Susan", GameToken.WHITE, PlayerType.AI),
				GamePlayerFactory.newGamePlayer("Susan", GameToken.WHITE, PlayerType.HUMAN));
		Assert.assertNotEquals(GamePlayerFactory.newGamePlayer("Susan", GameToken.WHITE, PlayerType.AI).hashCode(),
				GamePlayerFactory.newGamePlayer("Susan", GameToken.WHITE, PlayerType.HUMAN).hashCode());
	}
}