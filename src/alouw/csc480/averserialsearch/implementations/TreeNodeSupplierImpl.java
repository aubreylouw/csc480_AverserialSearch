package alouw.csc480.averserialsearch.implementations;

import java.util.NoSuchElementException;

import alouw.csc480.adverserialsearch.interfaces.GameMove;
import alouw.csc480.adverserialsearch.interfaces.TreeNode;
import alouw.csc480.adverserialsearch.interfaces.TreeNodeSupplier;

public class TreeNodeSupplierImpl implements TreeNodeSupplier {
	
	private final TreeNode node;
	
	// initialize state of supplier
	public TreeNode nextNode = null;
	public boolean hasNext = false;
	public GameMove nextMove = null;
	
	public TreeNodeSupplierImpl(TreeNode node) {
		this.node =  node;
	}

	@Override
	public boolean hasNext() {
		
		// return true if we know we have another child w/out advancing move forward
		if (this.hasNext) return this.hasNext;
		
		// 1st use of supplier
		if (this.nextMove == null) {
			this.nextMove = GameMove.COLUMN_ONE;
		}
		// fail immediately if we have exhausted all moves
		else {
			this.nextMove = this.nextMove.nextMove();
			this.hasNext =  !this.nextMove.equals(GameMove.NONE);
			if (!this.hasNext) return this.hasNext;
		}

		// find the next legal move
		while (!this.nextMove.equals(GameMove.NONE) && 
				!this.node.getGameState().isMoveLegal(this.nextMove))
			this.nextMove = this.nextMove.nextMove();
		
		// update state model
		this.hasNext =  !this.nextMove.equals(GameMove.NONE);
		
		return this.hasNext;
	}

	@Override
	public TreeNode getNextChild() {
		// cheap to call function if hasNext==true
		if (!this.hasNext()) 
			throw new NoSuchElementException("No more elements to be retrieved.");

		// we know this move is legal
		this.nextNode = this.node.getChildNode(this.nextMove);

		// set the variable to false 
		this.hasNext = false;
		
		return this.nextNode;
	}

	@Override
	public TreeNode getSupplierNode() {
		return this.node;
	}
	
	@Override
	public String toString() {
		return this.node + "_Supplier(" +  this.hasNext() + ")";
	}

}
