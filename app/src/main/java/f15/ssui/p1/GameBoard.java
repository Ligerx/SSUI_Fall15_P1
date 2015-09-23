package f15.ssui.p1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    // random tile location generator
    private Random generator = new Random();


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

        //// Go through all tiles and set them up
        // Loop through rows
        for (int row = 0; row < NUM_ROWS; row++) {
            // Loop through columns
            for (int col = 0; col < NUM_COLUMNS; col++) {
                Log.d("column and row", "col: " + col + " row: " + row);

                TileView tile = setupAndGetTileView(col, row, tileImages);

                // On click listener to communicate to the parent GameBoard
                tile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("tile touch", "in Tile on click");

                        GameBoard gameBoard = (GameBoard) view.getParent();
                        gameBoard.clickTile((TileView) view);
                    }
                });

                // 15 is the last tile. Special case
                if(15 == coordinateToIndex(col, row)) {
                    setBlankTile(tile);
                    tile.setImageBitmap(createWhiteTile(tileWidth, tileHeight));
                }

                // Lay out the tile on the display
                tile.layout(col * tileWidth, row * tileHeight,
                        (col * tileWidth) + tileWidth, (row * tileHeight) + tileHeight);
            }
        }

        //// ALSO SHUFFLE THE BOARD
        shuffleBoard(50);
    }


    public void shuffleBoard(int times) {
        for(int i = 0; i < times; i++) {
            swapRandomTile();
        }
    }

    // Randomly pick an adjacent tile to the blank and swap it
    public void swapRandomTile() {
        int row = this.blankTile.getRow();
        int col = this.blankTile.getCol();

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
                TileView tileToSwap = getTileAt(nextCol, nextRow);
                swapTileWithBlank(tileToSwap);

                moved = true;
            }
        }
    }

    private boolean isValidCoordinate(int col, int row) {
        if(row < 0 || col < 0) {
            return false;
        }
        else if(row >= NUM_ROWS || col >= NUM_ROWS) { // >= because # is a total, not 0-counting
            return false;
        }
        else {
            return true;
        }
    }




    private TileView setupAndGetTileView(int col, int row, ImageSplitter tileImages) {
        // Setup TileView data
        TileView tile = getTileAt(col, row);
        tile.setGridLocation(col, row);
        tile.setImgNum(coordinateToIndex(col, row));

        // Setup the image shown
        if(tileImages != null) {
            Bitmap tileImage = tileImages.getImageAtIndex(coordinateToIndex(col, row));
            addWhiteBorder(tileImage, 5);

            tile.setImageBitmap(tileImage);
        }
        else {
            // Give tile a random color (temporary, for testing)
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            tile.setBackgroundColor(color);
        }

        return tile;
    }

    private Bitmap createWhiteTile(int width, int height) {
        Bitmap whiteTile = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        whiteTile.eraseColor(Color.WHITE);

        return whiteTile;
    }

    private Bitmap addWhiteBorder(Bitmap image, int borderSize) {
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderSize);
        paint.setColor(Color.WHITE);

        canvas.drawRect(0, 0, image.getWidth(), image.getHeight(), paint);
        return image;
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

        // TODO update score
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
        BitmapDrawable tileDrawable = (BitmapDrawable)tile.getDrawable();
        BitmapDrawable blankDrawable = (BitmapDrawable)getBlankTile().getDrawable();

        // Extra test to see if the images exist first
        if(tileDrawable != null && blankDrawable != null) {
            // Swap the images
            tile.setImageBitmap(blankDrawable.getBitmap());
            getBlankTile().setImageBitmap(tileDrawable.getBitmap());
        }
        else {
            // TODO just ignore this stuff, not needed anymore
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
