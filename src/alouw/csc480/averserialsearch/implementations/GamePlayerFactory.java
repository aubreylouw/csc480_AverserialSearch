package alouw.csc480.averserialsearch.implementations;

import alouw.csc480.adverserialsearch.interfaces.GamePlayer;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer.PlayerType;
import alouw.csc480.adverserialsearch.interfaces.GameToken;

public final class GamePlayerFactory {
	public static GamePlayer newGamePlayer(final String name, final GameToken token, final PlayerType type) {
		return new GamePlayerImpl(name, token, type);
	}
}

class GamePlayerImpl implements GamePlayer {

	final String name;
	final GameToken token;
	final PlayerType type;
	
	GamePlayerImpl (final String name, final GameToken token, final PlayerType type) {
		if (name == null) throw new IllegalArgumentException("Supply a valid name");
		if (token == null) throw new IllegalArgumentException("Supply a valid token");
		
		this.name = name; 
		this.token = token;
		this.type = type;
	}
	
	@Override
	public String getPlayerName() {
		return this.name;
	}

	@Override
	public GameToken getPlayerToken() {
		return this.token;
	}

	@Override
	public PlayerType getPlayerType() {
		return this.type;
	}
}