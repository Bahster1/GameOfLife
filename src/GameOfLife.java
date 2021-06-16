import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameOfLife
{
	// ==================================================================================================
	// * If a cell is ON and has only 1 neighbor then it turns OFF in the next turn. (SOLITUDE)
	// * If a cell is ON and has 2 or 3 neighbors then it remains ON in the next turn.
	// * If a cell is OFF and has exactly 3 neighbors then it turns ON in the next turn. (REPRODUCTION)
	// * If a cell is ON and has 4 or more neighbors then it turns OFF in the next turn. (OVERPOPULATION)
	// ===================================================================================================
	// LINKS:
	// https://stackoverflow.com/questions/52797746/matrix-of-doubly-linked-nodes

	private static int[][] board;
	private static Random random = new Random();

	private static final int ON = 1;
	private static final int OFF = 0;

	private static final double ON_TILE_PROBABILITY = 0.5;

	private static final int MINIMUM_BOARD_SIZE = 6;
	private static final int MAXIMUM_BOARD_SIZE = 30;

	private static void readyBoard()
	{
		// Initialize board size
		int size = random.nextInt((MAXIMUM_BOARD_SIZE - MINIMUM_BOARD_SIZE) + 1) + MINIMUM_BOARD_SIZE;
		board = new int[size][size];

		// Assign ON tiles based on ON_TILE_PROBABILITY
		for (int x = 0; x < board.length; x++)
			for (int y = 0; y < board[x].length; y++)
				if (random.nextDouble() >= ON_TILE_PROBABILITY)
					board[x][y] = ON;

		displayBoard();
	}

	private static void evaluateAllTiles()
	{
		// Evaluate every tile's neighbor in an efficient way.

		// Potential approaches:
		// 1.) Only evaluate a tile if it is ON.
		//     PROBLEM: Must make sure to not check out of bounds.
		//
		// 2.) Make each tile, or the whole matrix, a special object which can evaluate adjacent tiles.
		//     Q1: How can adjacency be acknowledged?
		//     A1: Linked nodes with a top, bottom, left, and right node.
		//         Each tileNode object will react accordingly to the rules of the game.
		//
		//     Q2: How would you construct a matrix with tileNode objects?
		//     A2: Make another matrix based off of the original integer matrix.
		//         PROBLEM: This can use a lot of memory for little convenience.
		//                  This method can use the same border checking method as 1.) in order to construct the matrix
		//                  correctly.
		//         OBJECTION: nodeTiles can work better as you just need to call one method on each nodeTile instead
		//                    of evaluating every tile.
	}

	private static void displayBoard()
	{
		for (int[] col : board)
		{
			for (int tile : col) System.out.printf("%s ", (tile == OFF)? "." : tile);
			System.out.println();
		}
		System.out.println();
	}

	public static void play() throws InterruptedException {
		readyBoard();

		while (true)
		{
			evaluateAllTiles();
			displayBoard();
			TimeUnit.SECONDS.sleep(2);
		}
	}

	private class TileNode
	{
		private int data;
		private TileNode leftNode, rightNode, topNode, bottomNode;

		private TileNode()
		{
			this.data = OFF;
			this.leftNode = null;
			this.rightNode = null;
			this.topNode = null;
			this.bottomNode = null;
		}

		private TileNode(int data, TileNode leftNode, TileNode rightNode, TileNode topNode, TileNode bottomNode)
		{
			this.data = data;
			this.leftNode = leftNode;
			this.rightNode = rightNode;
			this.topNode = topNode;
			this.bottomNode = bottomNode;
		}

		private int evaluateNeighbors()
		{
			int onNodes = 0;

			if (this.leftNode != null)
				onNodes += this.leftNode.data;

			if (this.rightNode != null)
				onNodes += this.rightNode.data;

			if (this.topNode != null)
				onNodes += this.topNode.data;

			if (this.bottomNode != null)
				onNodes += this.bottomNode.data;

			return onNodes;
		}

		public void setData(int data)
		{
			if (data < 0 || data > 1)
				this.data = OFF;
			else
				this.data = data;
		}

		public void react()
		{
			int onNodes = evaluateNeighbors();

			switch (this.data)
			{
				case OFF:
					// REPRODUCTION
					if (onNodes == 3)
						setData(ON);
					break;
				case ON:
					// SOLITUDE
					if (onNodes < 2)
						setData(OFF);
					// OVERPOPULATION
					else if (onNodes > 3)
						setData(OFF);
					break;
			}
		}
	}
}
