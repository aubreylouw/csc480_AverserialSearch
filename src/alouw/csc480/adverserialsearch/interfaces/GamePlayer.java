package alouw.csc480.adverserialsearch.interfaces;

/*
 * A player of games
 */
public interface GamePlayer {
	
	public enum PlayerType {AI, HUMAN};

	public PlayerType getPlayerType();
	public String getPlayerName();
	public GameToken getPlayerToken();
}