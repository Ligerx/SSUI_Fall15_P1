package f15.ssui.p1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

public class GameBoard extends ViewGroup {

    // Grid size
    final int NUM_ROWS = 4;
    final int NUM_COLUMNS = 4;

    // ref to the blank tile
    private TileView blankTile;


    /**
     * Constructors
     */
    public GameBoard(Context context) {
        super(context);
    }

    public GameBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 4x4 grid
        if(getChildCount() != 16) { Log.e("children", "Not given 16 children!?"); return; }

        int width = getWidth();
        int height = getHeight();

        int tileWidth = width / NUM_COLUMNS;
        int tileHeight = height / NUM_ROWS;

        // Image stuff
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.duck);
        ImageSplitter tileImages = new ImageSplitter(width, height, tileWidth, tileHeight, image);

        // Loop through rows
        for (int row = 0; row < NUM_ROWS; row++) {
            // Loop through columns
            for (int col = 0; col < NUM_COLUMNS; col++) {
                Log.d("column and row", "col: " + col + " row: " + row);

                int currentIndex = coordinateToIndex(col, row);

                // Get tile and set it up
                TileView tile = getTileAt(col, row);
                tile.setGridLocation(col, row);
                tile.setImgNum(currentIndex);


                tile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("tile touch", "in Tile on click");

                        GameBoard gameBoard = (GameBoard) view.getParent();
                        gameBoard.clickTile((TileView) view);
                    }
                });



                if(tileImages != null) {
                    tile.setImageBitmap(tileImages.getImageAtIndex(currentIndex));
                }
                else {
                    // Give tile a random color (temporary)
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    tile.setBackgroundColor(color);
                }


                // 15 is the last tile. Special case
                if(15 == currentIndex) {
                    setBlankTile(tile);
                    tile.setBackgroundColor(Color.WHITE);
                }

                // Set tile corners
                tile.layout(col * tileWidth, row * tileHeight,
                        (col * tileWidth) + tileWidth, (row * tileHeight) + tileHeight);
            }
        }
    }


    // Changes a coordinate (AxB) into an index # to get children
    private int coordinateToIndex(int col, int row) {
        return col + row * 4;
    }

    // Getter, conveniently turns coordinates into a TileView
    private TileView getTileAt(int col, int row) {
        Log.d("GetTileAt", String.valueOf(col+row*4));
        return (TileView) getChildAt(coordinateToIndex(col, row));
    }

    // Call when a tile is clicked
    // Game's tile switching logic happens here
    private void clickTile(TileView tile) {
        Log.d("gameboard handle click", "parent got click notification");
        Log.d("gameboard handle click", "tile row: "+tile.getRow()+" column: "+tile.getCol());
        Log.d("gameboard handle click", "tile imgNum: " + tile.getImgNum());

        if(!isNextToBlankTile(tile)) { Log.d("gameboard handle click", "Not next to blank tile"); return; }

        swapTileWithBlank(tile);
        // TODO check for win condition

        // TODO show message on win?
        // TODO prevent further actions on win?
    }

    private boolean isNextToBlankTile(TileView tile) {
        int xDist = Math.abs(tile.getCol() - getBlankTile().getCol());
        int yDist = Math.abs(tile.getRow() - getBlankTile().getRow());

        // Clicked tile must be exactly 1 away in 1 direction
        // and 0 away in another direction
        //
        // NOTE: this method also prevents clicking the blank tile to be true
        if((xDist == 1 && yDist == 0) ||
           (xDist == 0 && yDist == 1)) {
            return true;
        } else {
            return false;
        }
    }



    private void swapTileWithBlank(TileView tile) {
        // FIXME the tiles are currently only a background color.
        // There's no actual bitmap on them.
        // Need to figure out how to split the image first.

        BitmapDrawable tileDrawable = (BitmapDrawable)tile.getDrawable();
        BitmapDrawable blankDrawable = (BitmapDrawable)getBlankTile().getDrawable();

        // Extra test to see if the images exist first
        if(tileDrawable != null && blankDrawable != null) {
            // Swap the images
            tile.setImageBitmap(tileDrawable.getBitmap());
            getBlankTile().setImageBitmap(blankDrawable.getBitmap());
        }
        else {
            // If no bitmap found, swap the background colors instead
            int tileColor = ((ColorDrawable) tile.getBackground()).getColor();
            int blankColor = ((ColorDrawable) getBlankTile().getBackground()).getColor();

            tile.setBackgroundColor(blankColor);
            getBlankTile().setBackgroundColor(tileColor);
        }

        // Update the blank tile reference
        setBlankTile(tile);
    }


    private void setBlankTile(TileView tile) {
        this.blankTile = tile;
    }

    private TileView getBlankTile() {
        return this.blankTile;
    }

}
