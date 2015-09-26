package f15.ssui.p1;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;

public class TileSwapper {

    private GameBoard gameBoard;

    public TileSwapper(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void swapTileWithBlank(TileView tile) {
        BitmapDrawable tileDrawable = (BitmapDrawable) tile.getDrawable();
        BitmapDrawable blankDrawable = (BitmapDrawable) gameBoard.getBlankTile().getDrawable();

        // Extra test to see if the images exist first
        if(tileDrawable != null && blankDrawable != null) {
            // Swap the images
            tile.setImageBitmap(blankDrawable.getBitmap());
            gameBoard.getBlankTile().setImageBitmap(tileDrawable.getBitmap());
        }
        else {
            // TODO just ignore this stuff, not needed anymore
            // If no bitmap found, swap the background colors instead
            int tileColor = ((ColorDrawable) tile.getBackground()).getColor();
            int blankColor = ((ColorDrawable) gameBoard.getBlankTile().getBackground()).getColor();

            tile.setBackgroundColor(blankColor);
            gameBoard.getBlankTile().setBackgroundColor(tileColor);
        }

        // swap imgNums as well to keep track of win state
        gameBoard.getBlankTile().swapImgNums(tile);

        // Update the blank tile reference
        gameBoard.setBlankTile(tile);
    }
}
