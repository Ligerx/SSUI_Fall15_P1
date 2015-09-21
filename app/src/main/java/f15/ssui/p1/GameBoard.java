package f15.ssui.p1;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

public class GameBoard extends ViewGroup /*implements View.OnTouchListener*/ {

    // Grid size
    final int NUM_ROWS = 4;
    final int NUM_COLUMNS = 4;

    // Location of blank tile
    private int blankLocation = 15;

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

        // Loop through rows
        for (int row = 0; row < NUM_ROWS; row++) {
            // Loop through columns
            for (int col = 0; col < NUM_COLUMNS; col++) {
                Log.d("column and row", "col: " + col + " row: " + row);

                // Get tile and set it up
                TileView tile = getTileAt(col, row);
                tile.setGridLocation(col, row);
                tile.setImgNum(coordinateToIndex(col, row));


                tile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("tile touch", "in Tile on click");

                        GameBoard gameBoard = (GameBoard) view.getParent();
                        gameBoard.clickTile((TileView) view);
                    }
                });

//                tile.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        Log.d("tile touch", "in Tile Touch");
//                        TileView x = (TileView) v;
////                        GameBoard y = (GameBoard) v;
//
//                        if(event.getAction() == MotionEvent.ACTION_DOWN){
//                            Log.d("tile touch", "is action down");
//                            return false; // Bubble up to action to the board!
//                        }
//
//                        return true; // Not the event we want, end the action
//                    }
//
//                });



                // Give tile a random color (temporary)
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                tile.setBackgroundColor(color);

                // Set tile corners
                tile.layout(col * tileWidth, row * tileHeight,
                        (col * tileWidth) + tileWidth, (row * tileHeight) + tileHeight);
            }
        }
    }



//    // This object's listener
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        TileView tile = (TileView) v;
//        Log.i("onClick info", "Clicked view's info: -------------------");
//        Log.i("onClick info", "col: "+tile.getCol());
//        Log.i("onClick info", "row: "+tile.getRow());
//        Log.i("onClick info", "img num:"+tile.getImgNum());
//
//        return true;
//    }



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
        Log.d("gameboard handle click", "tile imgNum: "+tile.getImgNum());

    }

}
