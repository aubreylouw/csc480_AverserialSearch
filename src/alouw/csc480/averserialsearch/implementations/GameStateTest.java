package alouw.csc480.averserialsearch.implementations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import alouw.csc480.adverserialsearch.interfaces.GameMove;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer.PlayerType;
import alouw.csc480.adverserialsearch.interfaces.GameState;
import alouw.csc480.adverserialsearch.interfaces.GameToken;

public class GameStateTest {

	final GamePlayer playerOne = GamePlayerFactory.newGamePlayer("Lord", GameToken.BLACK, PlayerType.AI);
	final GamePlayer playerTwo = GamePlayerFactory.newGamePlayer("Peasant", GameToken.WHITE, PlayerType.HUMAN);

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void testNewGameConstructor() {
		GameState newGame = GameStateFactory.getNewGameState(playerOne, playerTwo);
		
		Assert.assertTrue(newGame.getLastPlayer().equals(playerTwo));
		Assert.assertTrue(newGame.getNextPlayer().equals(playerOne));
		Assert.assertTrue(newGame.getAllGamePlayers().contains(playerOne));
		Assert.assertTrue(newGame.getAllGamePlayers().contains(playerTwo));
	}
	
	@Test
	public void testSuccessorGameConstructor() {
		GameState newGame = GameStateFactory.getNewGameState(playerOne, playerTwo);
		Assert.assertTrue(newGame.getLastPlayer().equals(playerTwo));
		Assert.assertTrue(newGame.getNextPlayer().equals(playerOne));
		
		GameState nextGame = GameStateFactory.getSuccessorGameState(playerOne, GameMove.COLUMN_FOUR, newGame);
		Assert.assertTrue(nextGame.getLastPlayer().equals(playerOne));
		Assert.assertTrue(nextGame.getAllGamePlayers().contains(playerOne));
		Assert.assertTrue(nextGame.getAllGamePlayers().contains(playerTwo));
		
		Set<GameState> nextGames = newGame.getAllValidSucessorStates();
		Assert.assertTrue(nextGames.contains(nextGame));
		Assert.assertTrue(nextGames.size() == GameMove.SIZE-1);
		
		// occupy all column one slots
		for (int i = 0; i < GameMove.SIZE-2; i++)
			nextGame = GameStateFactory.getSuccessorGameState(playerOne, GameMove.COLUMN_FOUR, nextGame);
	
		nextGames = nextGame.getAllValidSucessorStates();
		Assert.assertTrue(nextGames.size() == GameMove.SIZE-2);
	}
	
	@Test
	public void testScoring() {
		GameState newGame = GameStateFactory.getNewGameState(playerTwo, playerOne);
		GameState nextGame = newGame.executeMove(playerTwo, GameMove.COLUMN_ONE);
		Assert.assertEquals(0, nextGame.getPlayerScore(playerOne));
		Assert.assertEquals(0, nextGame.getPlayerScore(playerTwo));
		
		nextGame = nextGame.executeMove(playerOne, GameMove.COLUMN_ONE);
		Assert.assertEquals(0, nextGame.getPlayerScore(playerOne));
		Assert.assertEquals(0, nextGame.getPlayerScore(playerTwo));
		
		nextGame = nextGame.executeMove(playerTwo, GameMove.COLUMN_ONE);
		Assert.assertEquals(0, nextGame.getPlayerScore(playerOne));
		Assert.assertEquals(0, nextGame.getPlayerScore(playerTwo));
		
		nextGame = nextGame.executeMove(playerOne, GameMove.COLUMN_TWO);
		Assert.assertEquals(1, nextGame.getPlayerScore(playerOne));
		Assert.assertEquals(0, nextGame.getPlayerScore(playerTwo));
		
		nextGame = nextGame.executeMove(playerTwo, GameMove.COLUMN_ONE);
		Assert.assertEquals(1, nextGame.getPlayerScore(playerOne));
		Assert.assertEquals(2, nextGame.getPlayerScore(playerTwo));
		
		nextGame = nextGame.executeMove(playerOne, GameMove.COLUMN_TWO);
		Assert.assertEquals(5, nextGame.getPlayerScore(playerOne));
		Assert.assertEquals(2, nextGame.getPlayerScore(playerTwo));
		
		nextGame = nextGame.executeMove(playerTwo, GameMove.COLUMN_FOUR);
		Assert.assertEquals(5, nextGame.getPlayerScore(playerOne));
		Assert.assertEquals(2, nextGame.getPlayerScore(playerTwo));
		
		nextGame = nextGame.executeMove(playerOne, GameMove.COLUMN_TWO);
		Assert.assertEquals(8, nextGame.getPlayerScore(playerOne));
		Assert.assertEquals(2, nextGame.getPlayerScore(playerTwo));
		
		nextGame = nextGame.executeMove(playerTwo, GameMove.COLUMN_FOUR);
		Assert.assertEquals(8, nextGame.getPlayerScore(playerOne));
		Assert.assertEquals(4, nextGame.getPlayerScore(playerTwo));
		
		nextGame = nextGame.executeMove(playerOne, GameMove.COLUMN_THREE);
		Assert.assertEquals(11, nextGame.getPlayerScore(playerOne));
		Assert.assertEquals(4, nextGame.getPlayerScore(playerTwo));
		
		nextGame = nextGame.executeMove(playerTwo, GameMove.COLUMN_TWO);
		Assert.assertEquals(11, nextGame.getPlayerScore(playerOne));
		Assert.assertEquals(7, nextGame.getPlayerScore(playerTwo));
		
		//Assert.assertEquals("***", nextGame.printGameState());
	}
	
	@Test
	
	public void testScoringAnomaly() {
		final GamePlayer human = GamePlayerFactory.newGamePlayer("Aubrey", GameToken.WHITE, PlayerType.HUMAN);
		final GamePlayer comp = GamePlayerFactory.newGamePlayer("LV9111", GameToken.BLACK, PlayerType.AI);
		final GameState gameBoard = GameStateFactory.getNewGameState(comp, human);
		
		GameState thisBoard = gameBoard.executeMove(comp, GameMove.COLUMN_ONE);
		Assert.assertEquals(0, thisBoard.getPlayerScore(comp));
		Assert.assertEquals(0, thisBoard.getPlayerScore(human));
		
		thisBoard = thisBoard.executeMove(human, GameMove.COLUMN_TWO);
		Assert.assertEquals(0, thisBoard.getPlayerScore(comp));
		Assert.assertEquals(0, thisBoard.getPlayerScore(human));
		
		thisBoard = thisBoard.executeMove(comp, GameMove.COLUMN_ONE);
		Assert.assertEquals(2, thisBoard.getPlayerScore(comp));
		Assert.assertEquals(0, thisBoard.getPlayerScore(human));
		
		thisBoard = thisBoard.executeMove(human, GameMove.COLUMN_FOUR);
		Assert.assertEquals(2, thisBoard.getPlayerScore(comp));
		Assert.assertEquals(0, thisBoard.getPlayerScore(human));
	}
	
	@Test
	public void testInvalidMovesVertically() {
		GameState newGame = GameStateFactory.getNewGameState(playerOne, playerTwo);
		
		for (GameMove move: GameMove.values()) {
			if (!move.equals(GameMove.NONE))
					newGame = newGame.executeMove(playerTwo, GameMove.COLUMN_ONE);
		}
		
		try {
			newGame.executeMove(playerOne, GameMove.COLUMN_ONE);
			Assert.fail();
		} catch (IllegalArgumentException iae) {}
	}
	
	@Test
	public void testInvalidMovesHorizontally() {
		GameState newGame = GameStateFactory.getNewGameState(playerOne, playerTwo);
		
		for (GameMove move: GameMove.values()) {
			if (!move.equals(GameMove.NONE))
					newGame = newGame.executeMove(playerTwo, move);
		}
		
		try {
			newGame.executeMove(playerOne, GameMove.NONE);
			Assert.fail();
		} catch (IllegalArgumentException iae) {}
	}
}