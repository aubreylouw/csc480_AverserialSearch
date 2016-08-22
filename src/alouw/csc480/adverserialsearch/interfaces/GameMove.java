package alouw.csc480.adverserialsearch.interfaces;

/*
 * A representation of a valid move on the game board. 
 * 
 */
public enum GameMove {
	COLUMN_ONE {
		@Override
		public GameMove nextMove() {
			return COLUMN_TWO;
		}
	}, 
	COLUMN_TWO{
		@Override
		public GameMove nextMove() {
			return COLUMN_THREE;
		}
	}, 
	COLUMN_THREE{
		@Override
		public GameMove nextMove() {
			return COLUMN_FOUR;
		}
	}, 
	COLUMN_FOUR	{
		@Override
		public GameMove nextMove() {
			return COLUMN_FIVE;
		}
	},
	COLUMN_FIVE{
		@Override
		public GameMove nextMove() {
			return COLUMN_SIX;
		}
	}, 
	COLUMN_SIX{
		@Override
		public GameMove nextMove() {
			return COLUMN_SEVEN;
		}
	}, 
	COLUMN_SEVEN{
		@Override
		public GameMove nextMove() {
			return COLUMN_EIGHT;
		}
	}, 
	COLUMN_EIGHT{
		@Override
		public GameMove nextMove() {
			return COLUMN_NINE;
		}
	}, 
	COLUMN_NINE{
		@Override
		public GameMove nextMove() {
			return NONE;
		}
	},
	NONE{
		@Override
		public GameMove nextMove() {
			return NONE;
		}
	};
	
	public static final int SIZE = GameMove.values().length;
	public abstract GameMove nextMove();
}