package alouw.csc480.averserialsearch.implementations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import alouw.csc480.adverserialsearch.interfaces.GameMove;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer;
import alouw.csc480.adverserialsearch.interfaces.GameState;
import alouw.csc480.adverserialsearch.interfaces.GameToken;

public final class GameStateFactory {

	public static final GameState getNewGameState(final GamePlayer firstPlayer, final GamePlayer secondPlayer) {
		return new GameStateImpl(secondPlayer, firstPlayer);
	}
	
	public static final GameState getSuccessorGameState(final GamePlayer nextPlayer, final GameMove nextPlayerMove,
			final GameState currentGameState) {
		if (nextPlayerMove.equals(GameMove.NONE)) return currentGameState;
		return new GameStateImpl(nextPlayer, nextPlayerMove, currentGameState);
	}
}

/*
 * A particular game board represented as an NxN, 2D array of GameTokens. A null intersection is an EMPTY square.
 * 
 * The lastPlayer is the player whose move (the lastPlayerMove) created this current state; the nextPlayer is the player 
 * who will initiate the next move.
 * 
 * The constructor either returns an empty board or the ancestor board with the game move applied.
 * 
 */
class GameStateImpl implements GameState {

	final GamePlayer lastPlayer;
	final GameMove lastPlayerMove;
	final GamePlayer nextPlayer;
	int hashCode;
	
	// variables for managing how much of the gameBoard to search
	int maxRow = GameMove.SIZE-1;
	int maxColumn = 0;
	
	final GameToken[][] gameBoard = new GameToken[GameMove.SIZE-1][GameMove.SIZE-1];
	
	private GameStateImpl(final GamePlayer lastPlayer, final GamePlayer nextPlayer, final GameMove activePlayerMove) {
		this.lastPlayer = lastPlayer;
		this.nextPlayer = nextPlayer;
		this.lastPlayerMove = activePlayerMove;
	}
	
	GameStateImpl(final GamePlayer lastPlayer, final GamePlayer nextPlayer) {
		this(lastPlayer, nextPlayer, GameMove.NONE);
		this.hashCode = this.calcHashCode();
	}
	
	GameStateImpl(final GamePlayer nextPlayer, final GameMove nextPlayerMove, final GameState priorGameState) {
		// pass in next player as last played since we are creating a *NEW* game state
		this(nextPlayer, priorGameState.getLastPlayer(), nextPlayerMove);
		final GameStateImpl ancestorGame = (GameStateImpl) priorGameState;
		
		// replicate the ancestor board
		boolean rowOccupied = true;
		for (int row = GameMove.SIZE-2; row >= ancestorGame.maxRow && rowOccupied; row--) {
			rowOccupied = false;
			for (int column = 0; column <= ancestorGame.maxColumn; column++) {
				final Optional<GameToken> gameToken = ancestorGame.getGameTokenAtCoordinates(row, column);
				
				if (gameToken.isPresent()) {
					this.gameBoard[row][column] = gameToken.get();
					rowOccupied = true;
					
					// the furthest to the right we have searched thus far
					this.maxColumn = (column > this.maxColumn) ? column : this.maxColumn;
					
					// the highest up one column we have searched thus far
					this.maxRow = (row < this.maxRow) ? row : this.maxRow;
				}
			}
		}
		
		// modify the current game state by iterating over the columns ("rows" for a player)
		// and inserting the player's token in the next available space
		final int moveColumn = nextPlayerMove.ordinal();
			
		// perhaps the nextPlayer is inserting a token into a column further to the right
		this.maxColumn = (moveColumn > this.maxColumn) ? moveColumn : this.maxColumn;
			
		for (int row = GameMove.SIZE-2; row >= 0; row--) {
			if (ancestorGame.getGameTokenAtCoordinates(row, moveColumn).isPresent()) 
				continue;	// slot is taken since optional is not empty
				
			// insert the token
			this.gameBoard[row][moveColumn] = nextPlayer.getPlayerToken(); 
				
			// the highest up one column we have searched thus far
			this.maxRow = (row < this.maxRow) ? row : this.maxRow;
				
			break;
		}
	}
	
	private final Optional<GameToken> getGameTokenAtCoordinates(int row, int column) {
		return Optional.ofNullable(this.gameBoard[row][column]);
	}

	/*
	 * The scoring function works as follows:
	 * (a) score the board vertically
	 * (b) score the board horizontally
	 * (C) score the board diagonally
	 */
	private int calculatePlayerScore(GamePlayer player) {
	
		int playerScore = 0;
		final GameToken playerToken = player.getPlayerToken();
		// sum vertically
		for (int column = 0; column <= this.maxColumn; column++) {	
			Optional<GameToken> previousToken = this.getGameTokenAtCoordinates(GameMove.SIZE-2, column);
			
			if (!previousToken.isPresent()) continue;
			for (int row = GameMove.SIZE-3; row >= this.maxRow; row--) {
				final Optional<GameToken> thisToken = this.getGameTokenAtCoordinates(row, column);
				
				// skip to next column if row is empty
				if (!thisToken.isPresent()) break;	
				
				// if this token is the same as the previous token we have a sequence
				// if the sequence is the same color as our player token, we score it
				// finally we set this token as the previous to0oken
				if (thisToken.get().equals(previousToken.get()) && 
						thisToken.get().equals(playerToken)) playerScore+= 2;
				
				previousToken = thisToken;
			}					
		}
		
		// sum horizontally
		for (int row = GameMove.SIZE-2; row >= this.maxRow; row--) {
			Optional<GameToken> previousToken = this.getGameTokenAtCoordinates(row, 0);
			
			for (int column = 1; column <= this.maxColumn; column++) {
				final Optional<GameToken> thisToken = this.getGameTokenAtCoordinates(row, column);
				
				if (thisToken.isPresent()) {
					final GameToken token = thisToken.get(); 
					if (previousToken.isPresent()) {
						if (token.equals(previousToken.get()) && 
								token.equals(playerToken)) playerScore+= 2;
					}
				} 
				
				previousToken = thisToken;
			}					
		}
		
		int diagonalScore = 0;
		// sum diagonally
		for (int row = GameMove.SIZE-2; row >= this.maxRow; row--) {
			for (int column = 0; column <= this.maxColumn; column++) {
				
				final Optional<GameToken> thisToken = this.getGameTokenAtCoordinates(row, column);
						
				if (!thisToken.isPresent()) continue;;	// skip ahead to the next column
				if (!thisToken.get().equals(playerToken)) continue; // no need to count these tokens
				
				// check diagonals: top left, top right, bottom left, bottom right; add .5
				if (row+1 < GameMove.SIZE-1) {
					if (column - 1 >= 0) {
						final Optional<GameToken> adjacentToken = this.getGameTokenAtCoordinates(row+1, column-1);
						if (adjacentToken.isPresent() && adjacentToken.get().equals(thisToken.get()) &&
										adjacentToken.get().equals(playerToken)) diagonalScore += 1;
					}
							
					if (column + 1 < GameMove.SIZE-1) {
						final Optional<GameToken> adjacentToken = this.getGameTokenAtCoordinates(row+1, column+1);
						if (adjacentToken.isPresent() && adjacentToken.get().equals(thisToken.get()) &&
								adjacentToken.get().equals(playerToken)) diagonalScore += 1;
					}
				}
						
				if (row-1 >= 0) {
					if (column - 1 >= 0) {
						final Optional<GameToken> adjacentToken = this.getGameTokenAtCoordinates(row-1, column-1);
						if (adjacentToken.isPresent() && adjacentToken.get().equals(thisToken.get()) &&
								adjacentToken.get().equals(playerToken)) diagonalScore += 1;
					}
							
					if (column + 1 < GameMove.SIZE-1) {
						final Optional<GameToken> adjacentToken = this.getGameTokenAtCoordinates(row-1, column+1);
						if (adjacentToken.isPresent() && adjacentToken.get().equals(thisToken.get()) &&
								adjacentToken.get().equals(playerToken)) diagonalScore += 1;
					}
				}
			}
		}
		
		return playerScore + diagonalScore/2;
	}
	
	@Override
	public int compareTo(GameState arg0) {
		return 0;
	}

	@Override
	public int getPlayerScore(GamePlayer player) {
		if (!(player.equals(this.lastPlayer) || player.equals(this.nextPlayer)))
			throw new IllegalArgumentException(player + " is not active on this game board");
		
		final int playerScore = this.calculatePlayerScore(player);
		
		assert(playerScore>=0);
		
		return playerScore;
	}

	@Override
	public boolean isMoveLegal(GameMove move) {
		
		if (move.equals((GameMove.NONE))) 
			return false;
		
		return !this.getGameTokenAtCoordinates(0, move.ordinal()).isPresent();
	}

	@Override
	public GameState executeMove(GamePlayer player, GameMove move) throws IllegalArgumentException {
		
		if (!this.isMoveLegal(move)) 
			throw new IllegalArgumentException (player + "'s move {" + move + "} is illegal.");
		
		return GameStateFactory.getSuccessorGameState(player, move, this);
	}

	@Override
	public Set<GameState> getAllValidSucessorStates() {
		final Set<GameState> successors = new HashSet<>(GameMove.SIZE-1);
		
		for (GameMove nextMove : GameMove.values()) {
			if (this.isMoveLegal(nextMove))
				successors.add(GameStateFactory.getSuccessorGameState(this.nextPlayer, nextMove, this));
		}
		
		return successors;
	}

	@Override
	public GamePlayer getLastPlayer() {
		return this.lastPlayer;
	}

	@Override
	public Set<GamePlayer> getAllGamePlayers() {
		final Set<GamePlayer> players = new HashSet<>();
		players.add(lastPlayer); players.add(nextPlayer);
		return players;
	}

	@Override
	public String printGameState() {
		final StringBuilder gameBoardString = new StringBuilder();
		
		for (int row = 0; row < GameMove.SIZE-1; row++) {
			for (int column = 0; column < GameMove.SIZE-1; column++) {
				gameBoardString.append(" | ");
				final Optional<GameToken> gameToken = Optional.ofNullable(this.gameBoard[row][column]);
				if (gameToken.isPresent()) gameBoardString.append(String.format("%1$"+ 5 + "s", gameToken.get()));
				else gameBoardString.append(String.format("%1$"+ 5 + "s", ""));
			}
			gameBoardString.append(" |").append("\n");
		}
		
		gameBoardString.append("Last Player [")
					   .append(this.lastPlayer.getPlayerName())
					   .append("] score = ")
					   .append(this.calculatePlayerScore(this.lastPlayer))
					   .append("\n"); 
		gameBoardString.append("Next Player [")
		   			   .append(this.nextPlayer.getPlayerName())
		               .append("] score = ")
		               .append(this.calculatePlayerScore(this.nextPlayer))
		               .append("\n");
		
		return gameBoardString.toString();
	}
	
	@Override
	public boolean equals(Object that){
		if (this == that) return true;
		if (!(that instanceof GameStateImpl)) return false;
		
		final GameStateImpl thatState = (GameStateImpl) that;
		
		final boolean result = this.lastPlayer.equals(thatState.lastPlayer);
		
		return result && Arrays.deepEquals(this.gameBoard, thatState.gameBoard); 
	}
	
	private int calcHashCode() {
		return Objects.hash(Arrays.deepHashCode(this.gameBoard), this.lastPlayer,
				this.nextPlayer, this.maxColumn, this.maxRow);
	}
	
	@Override 
	public int hashCode() {
		if (this.hashCode == 0)
			this.hashCode = this.calcHashCode();
		
		return this.hashCode;
	}

	@Override
	public GamePlayer getNextPlayer() {
		return this.nextPlayer;
	}

	@Override
	public GameMove getLastGameMove() {
		return this.lastPlayerMove;
	}

	@Override
	public boolean isGameOver() {
		boolean result = true;
		
		for (int row = 0; row < GameMove.SIZE-1 && result; row++) {
			for (int column = 0; column < GameMove.SIZE-1 && result; column++) {
				result = this.getGameTokenAtCoordinates(row,column).isPresent();
			}
		}
		return result;
	}
}