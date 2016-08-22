package alouw.csc480.adverserialsearch.interfaces;

import java.util.Set;

/*
 * An abstract definition of a particular game state.
 */
public interface GameState extends Comparable<GameState> {
	
	// returns false if there are no legal moves remaining 
	public boolean isGameOver();
	
	// returns the move made by the active player to create this game state
	public GameMove getLastGameMove();
	
	// returns the player who executed the move that created this game state
	public GamePlayer getLastPlayer();
	
	// returns the opponent player who will move next
	public GamePlayer getNextPlayer();
	
	// returns a set of immutable players with tokens on this game board
	public Set<GamePlayer> getAllGamePlayers();
	
	// returns the player's current score for this game board
	// exception thrown if the player argument is not in the set of all game players
	public int getPlayerScore(GamePlayer anyValidPlayer)
		throws IllegalArgumentException;
	
	// tests whether the transformation operation is legal
	public boolean isMoveLegal(GameMove nextPlayerMove);
	
	// applies the selected move and returns a new, successor problem state
	// exception thrown if the move is illegal for the current board
	public GameState executeMove(GamePlayer nextPlayer, GameMove nextPlayerMove) 
			throws IllegalArgumentException;
	
	// returns a set of immutable problem states that can legally be derived from applying all legal
	// moves to the current state ; it does not include the current set
	public Set<GameState> getAllValidSucessorStates();
	
	// pretty print the game state
	public String printGameState();
}