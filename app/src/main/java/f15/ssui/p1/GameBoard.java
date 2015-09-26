package f15.ssui.p1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameBoard extends ViewGroup {

    // Grid size
    final int NUM_ROWS = 4;
    final int NUM_COLUMNS = 4;

    final int SHUFFLE_TIMES = 100; // # times to shuffle the board

    private TileView blankTile; // ref to the blank tile

    private ArrayList<Integer> restoredImageOrder; // put order that image tiles should be restored here

    public BoardShuffler shuffler;

    public TileSwapper swapper;

    public ImageSplitter imageSplitter;

    /**
     * Constructors
     */
    public GameBoard(Context context) {
        super(context);
        this.swapper = new TileSwapper(this);
        this.shuffler = new BoardShuffler(this.SHUFFLE_TIMES, this, this.swapper);
    }

    public GameBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swapper = new TileSwapper(this);
        this.shuffler = new BoardShuffler(this.SHUFFLE_TIMES, this, this.swapper);
    }


    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 4x4 grid
        if(getChildCount() != 16) { Log.e("children", "Not given 16 children!?"); return; }

        // Image stuff
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.duck);
        ImageSplitter tileImages = new ImageSplitter(getWidth(), getHeight(), NUM_COLUMNS, NUM_ROWS, image);
        this.imageSplitter = tileImages; // save ref to imageSplitter so I can show solution

        int tileWidth = getWidth() / NUM_COLUMNS;
        int tileHeight = getHeight() / NUM_ROWS;

        //// Go through all tiles and set them up
        // Loop through rows
        for (int row = 0; row < NUM_ROWS; row++) {
            // Loop through columns
            for (int col = 0; col < NUM_COLUMNS; col++) {
                Log.d("column and row", "col: " + col + " row: " + row);

                TileView tile = setupAndGetTileView(col, row, tileImages);

                setTileOnClickListener(tile);

                // Last tile (based on image number, not tile location) is a special case
               if(NUM_ROWS * NUM_COLUMNS - 1 == tile.getImgNum()) {
                    setBlankTile(tile);
                    tile.setImageBitmap(createWhiteTile(tileWidth, tileHeight));
                }

                // Lay out the tile on the display
                tile.layout(col * tileWidth, row * tileHeight,
                        (col * tileWidth) + tileWidth, (row * tileHeight) + tileHeight);
            }
        }

        //// ALSO SHUFFLE THE BOARD unless you're restoring previous state
        if(restoredImageOrder == null) {
            shuffler.shuffleBoard();
        }
    }

    private void setTileOnClickListener(TileView tile) {
        // On click listener to communicate to the parent GameBoard
        tile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tile touch", "in Tile on click");

                GameBoard gameBoard = (GameBoard) view.getParent();
                gameBoard.clickTile((TileView) view);
            }
        });
    }

    // Get the current order the tiles are in. Useful for recreating puzzle state
    public ArrayList<Integer> getCurrentImageOrder() {
        Log.d("current image order", "---- getCurrentImageOrder()");

        ArrayList<Integer> order = new ArrayList<>();

        for(int i = 0; i < getChildCount(); i++) {
            TileView tile = (TileView) getChildAt(i);
            order.add(tile.getImgNum());

            Log.d("current image order", "" + tile.getImgNum());
        }

        return order;
    }

    private TileView setupAndGetTileView(int col, int row, ImageSplitter tileImages) {
        // Setup TileView data
        TileView tile = getTileAt(col, row);
        tile.setGridLocation(col, row);

        // Setup the image shown
        if(tileImages != null) {
            Bitmap tileImage;

            if(this.restoredImageOrder != null) {
                // Restore previous image tile bitmap AND num
                int previousLocation = this.restoredImageOrder.get(coordinateToIndex(col, row));

                Log.d("reset image order", "" + previousLocation);

                tile.setImgNum(previousLocation);
                tileImage = tileImages.getImageAtIndex(previousLocation);
            }
            else {
                // else set default image tile bitmap and num
                tile.setImgNum(coordinateToIndex(col, row));
                tileImage = tileImages.getImageAtIndex(coordinateToIndex(col, row));
            }

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
    public TileView getTileAt(int col, int row) {
        Log.d("GetTileAt", String.valueOf(col + row * 4));
        return (TileView) getChildAt(coordinateToIndex(col, row));
    }

    // Call when a tile is clicked
    // Game's tile switching logic happens here
    private void clickTile(TileView tile) {
        Log.d("gameboard handle click", "parent got click notification");
        Log.d("gameboard handle click", "tile row: "+tile.getRow()+" column: "+tile.getCol());
        Log.d("gameboard handle click", "tile imgNum: " + tile.getImgNum());

        // Escape click if already won
        if(isWinningState()) { Log.d("gameboard handle click", "Already in win state"); return; }

        // Escape click if not next to a blank tile
        if(!isNextToBlankTile(tile)) { Log.d("gameboard handle click", "Not next to blank tile"); return; }

        swapper.swapTileWithBlank(tile);

        // Update score
        GameActivity gameActivity = (GameActivity) getContext();
        gameActivity.incrementScore();

        // Show winning message on victory
        if(isWinningState()) {
            Toast.makeText(getContext(), "You figured it out!",
            Toast.LENGTH_LONG).show();
        }
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

    boolean isWinningState() {
        int numTiles = getChildCount();

        for(int i = 0; i < numTiles; i++) {
            // Check that all TileViews' imgNum are in order
            TileView tile = (TileView) getChildAt(i);
            if(tile.getImgNum() != i) { return false; }
        }

        return true;
    }

    public void setBlankTile(TileView tile) {
        this.blankTile = tile;
    }

    public TileView getBlankTile() {
        return this.blankTile;
    }

    public void setRestoredImageOrder(ArrayList<Integer> restoredImageOrder) {
        this.restoredImageOrder = restoredImageOrder;

        for(int i = 0; i < restoredImageOrder.size(); i++) {
            Log.d("setrestored image order", "" + restoredImageOrder.get(i));
        }
    }

}
