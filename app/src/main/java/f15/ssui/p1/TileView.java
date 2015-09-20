package f15.ssui.p1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by admin on 9/20/15.
 */
public class TileView extends ImageView {

    // Position of the tile in the grid
    private int xPos;
    private int yPos;

    // displayed image's number. In order = solved.
    private int imgNum;

//    public TileView(Context context, int xPos, int yPos, int imgNum) {
//        super(context);
//
//        this.xPos = xPos;
//        this.yPos = yPos;
//        this.imgNum = imgNum;
//    }


    public TileView(Context context) {
        super(context);
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//
//    public TileView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public TileView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    // Setters
    void setGridLocation(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    void setImageNumber(int imgNum) {
        this.imgNum = imgNum;
    }

    // Getters
    int getXPosition() {
        return this.xPos;
    }

    int getYPosition() {
        return this.yPos;
    }

    int getImageNumber() {
        return this.imgNum;
    }

}
