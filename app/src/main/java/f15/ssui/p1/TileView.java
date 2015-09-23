package f15.ssui.p1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TileView extends ImageView {

    // Position of the tile in the grid
    private int col;
    private int row;

    // displayed image's number. In order = solved.
    private int imgNum;


    /**
     * Constructors
     */
    public TileView(Context context) {
        super(context);
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    // convenience method to swap around img nums after swapping images
    public void swapImgNums(TileView other) {
        int otherImgNum = other.getImgNum();

        other.setImgNum(getImgNum());
        setImgNum(otherImgNum);
    }


    // Setters
    void setGridLocation(int col, int row) {
        this.col = col;
        this.row = row;
    }

    void setImgNum(int imgNum) {
        this.imgNum = imgNum;
    }



    // Getters
    int getCol() {
        return this.col;
    }

    int getRow() {
        return this.row;
    }

    int getImgNum() {
        return this.imgNum;
    }

}
