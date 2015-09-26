package f15.ssui.p1;

import java.util.Random;

public class BoardShuffler {

    private final int SHUFFLE_TIMES;
    private GameBoard gameBoard;

    private Random generator = new Random(); // random tile location generator

    private TileSwapper swapper;

    /**
     * Constructor
     */
    public BoardShuffler(int SHUFFLE_TIMES, GameBoard gameBoard, TileSwapper swapper) {
        this.SHUFFLE_TIMES = SHUFFLE_TIMES;
        this.gameBoard = gameBoard;
        this.swapper = swapper;
    }


    /**
     * Call this to shuffle the entire board
     */
    public void shuffleBoard() {
        for(int i = 0; i < this.SHUFFLE_TIMES; i++) {
            swapRandomTile();
        }
    }

    // Randomly pick an adjacent tile to the blank and swap it
    public void swapRandomTile() {
        int row = gameBoard.getBlankTile().getRow();
        int col = gameBoard.getBlankTile().getCol();

        boolean moved = false;

        while(!moved) {
            int rand = generator.nextInt(4); // 0-3 I believe
            int nextCol = col;
            int nextRow = row;

            // Pick a direction to move
            if(rand == 0) { // up
                nextRow++;
            }else if(rand == 1) { // right
                nextCol++;
            }else if(rand == 2) { // down
                nextRow--;
            }else if(rand == 3) { // left
                nextCol--;
            }

            if(isValidCoordinate(nextCol, nextRow)) {
                TileView tileToSwap = gameBoard.getTileAt(nextCol, nextRow);
                swapper.swapTileWithBlank(tileToSwap);

                moved = true;
            }
        }
    }

    private boolean isValidCoordinate(int col, int row) {
        if(row < 0 || col < 0) {
            return false;
        }
        else if(row >= gameBoard.NUM_ROWS || col >= gameBoard.NUM_ROWS) { // >= because # is a total, not 0-counting
            return false;
        }
        else {
            return true;
        }
    }

}
