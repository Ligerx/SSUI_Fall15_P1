package f15.ssui.p1;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by admin on 9/20/15.
 */
public class GameBoard extends ViewGroup {

    // Grid size
    final int NUM_ROWS = 4;
    final int NUM_COLUMNS = 4;

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


        // Loop through rows
        for (int row = 0; row < NUM_ROWS; row++) {
            // Loop through columns
            for (int col = 0; col < NUM_COLUMNS; col++) {
                TileView tile = getTileAt(col, row);


                tile.setBackgroundColor(Color.rgb(100, 100, 50));

                tile.layout(col);
            }
        }

//        final int numChildren = getChildCount();
//
//        // These are the far left and right edges in which we are performing layout.
//        int leftPos = getPaddingLeft();
//        int rightPos = right - left - getPaddingRight();
//
//        // This is the middle region inside of the gutter.
//        final int middleLeft = leftPos + mLeftWidth;
//        final int middleRight = rightPos - mRightWidth;
//
//        // These are the top and bottom edges in which we are performing layout.
//        final int parentTop = getPaddingTop();
//        final int parentBottom = bottom - top - getPaddingBottom();
//
//        for (int i = 0; i < count; i++) {
//            final View child = getChildAt(i);
//            if (child.getVisibility() != GONE) {
//                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
//
//                final int width = child.getMeasuredWidth();
//                final int height = child.getMeasuredHeight();
//
//                // Compute the frame in which we are placing this child.
//                if (lp.position == LayoutParams.POSITION_LEFT) {
//                    mTmpContainerRect.left = leftPos + lp.leftMargin;
//                    mTmpContainerRect.right = leftPos + width + lp.rightMargin;
//                    leftPos = mTmpContainerRect.right;
//                } else if (lp.position == LayoutParams.POSITION_RIGHT) {
//                    mTmpContainerRect.right = rightPos - lp.rightMargin;
//                    mTmpContainerRect.left = rightPos - width - lp.leftMargin;
//                    rightPos = mTmpContainerRect.left;
//                } else {
//                    mTmpContainerRect.left = middleLeft + lp.leftMargin;
//                    mTmpContainerRect.right = middleRight - lp.rightMargin;
//                }
//                mTmpContainerRect.top = parentTop + lp.topMargin;
//                mTmpContainerRect.bottom = parentBottom - lp.bottomMargin;
//
//                // Use the child's gravity and size to determine its final
//                // frame within its container.
//                Gravity.apply(lp.gravity, width, height, mTmpContainerRect, mTmpChildRect);
//
//                // Place the child.
//                child.layout(mTmpChildRect.left, mTmpChildRect.top,
//                        mTmpChildRect.right, mTmpChildRect.bottom);
//            }
//        }
    }

    // Getter, conveniently turns coordinates into a TileView
    private TileView getTileAt(int x, int y) {
        return (TileView) getChildAt(x + y);
    }

}
