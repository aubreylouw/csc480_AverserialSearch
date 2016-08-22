package alouw.csc480.averserialsearch.implementations;

import java.util.Scanner;
import alouw.csc480.adverserialsearch.interfaces.GameMove;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer;
import alouw.csc480.adverserialsearch.interfaces.GamePlayer.PlayerType;
import alouw.csc480.adverserialsearch.interfaces.GameState;
import alouw.csc480.adverserialsearch.interfaces.GameToken;
import alouw.csc480.adverserialsearch.interfaces.SearchFunction;
import alouw.csc480.adverserialsearch.interfaces.TreeNode;
import alouw.csc480.adverserialsearch.interfaces.TreeNode.NodeType;

public class SearchMain {

	private static final Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) { 
		final GamePlayer maxPlayer = GamePlayerFactory.newGamePlayer("The Machine", GameToken.BLACK, PlayerType.AI);
		final GamePlayer minPlayer = GamePlayerFactory.newGamePlayer("Sarah Connor", GameToken.WHITE, PlayerType.HUMAN);
		final GameState initialGameState = GameStateFactory.getNewGameState(minPlayer, maxPlayer);
	
		System.out.println ("Welcome " + minPlayer.getPlayerName() +"! Your opponent is " + maxPlayer.getPlayerName());
		System.out.println();
		
		GameState currentGame = initialGameState;
		boolean isGameOver = false;
		do {
			int inputMove = 0;
			boolean validInput= false;
			do {		
				System.out.print(minPlayer.getPlayerName() + ", place your token in a column [1-" + (GameMove.SIZE-1) + "]: ");
				try {
					inputMove = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException nfe) {
					System.out.println("Enter a number between 1 and 9"); continue;
				}
				
				if (!(inputMove >= 1 && inputMove <= 9))
					System.out.println("Enter a number between 1 and 9");
				else {
					GameMove userMove = mapIntToGameMove(inputMove);
		            validInput = currentGame.isMoveLegal(userMove);
		            if (!validInput)
		            	System.out.println(userMove + " is full. Please check the game board and place your token in a valid column.");
				}
			} while (!validInput);
			
			// print the new board
            currentGame = GameStateFactory.getSuccessorGameState(minPlayer, mapIntToGameMove(inputMove), currentGame);
            
            System.out.println("Last to play was: " + currentGame.getLastPlayer().getPlayerName());
            
            System.out.println();
            System.out.println(currentGame.printGameState());
            System.out.println(); 
            
            isGameOver = currentGame.isGameOver();
            
            if (!isGameOver) {
            	// find AI move by creating a tree and searching x plies deep
                // the next move on currentGame is the MAX player
                TreeNode rootNode = TreeNodeFactory.getNewRootNode(NodeType.MAX, currentGame);
                SearchTree searchTree = new SearchTree(SearchFunction.MINIMAX, rootNode, 5);
                //SearchTree searchTree = new SearchTree(SearchFunction.MINIMAX_WITH_PRUNING, rootNode, 10);
                TreeNode bestNode = searchTree.search();
                
                // execute AI move
                currentGame = GameStateFactory.getSuccessorGameState(maxPlayer, bestNode.getGameState().getLastGameMove(), currentGame);
                System.out.println("Last to play was: " + currentGame.getLastPlayer().getPlayerName());
                System.out.println();
                System.out.println(currentGame.printGameState());
                System.out.println();
            }
            
            isGameOver = currentGame.isGameOver();
		} while (!isGameOver);
	}
	
	public static GameMove mapIntToGameMove(int move) {
		GameMove result = GameMove.NONE;
		
		switch (move) {
			case 1: result = GameMove.COLUMN_ONE; break;
			case 2: result = GameMove.COLUMN_TWO; break;
			case 3: result = GameMove.COLUMN_THREE; break;
			case 4: result = GameMove.COLUMN_FOUR; break;
			case 5: result = GameMove.COLUMN_FIVE; break;
			case 6: result = GameMove.COLUMN_SIX; break;
			case 7: result = GameMove.COLUMN_SEVEN; break;
			case 8: result = GameMove.COLUMN_EIGHT; break;
			case 9: result = GameMove.COLUMN_NINE; break;
		}
		
		return result;
	}
}