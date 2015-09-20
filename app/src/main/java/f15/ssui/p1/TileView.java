package f15.ssui.p1;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by admin on 9/20/15.
 */
public class TileView extends ImageView {

    // Position of the tile in the grid
    final int xPos;
    final int yPos;

    // displayed image's number. In order = solved.
    int imgNum;

    public TileView(Context context, int xPos, int yPos, int imgNum) {
        super(context);

        this.xPos = xPos;
        this.yPos = yPos;
        this.imgNum = imgNum;
    }
}
