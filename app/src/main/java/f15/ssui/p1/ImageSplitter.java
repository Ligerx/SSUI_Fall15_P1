package f15.ssui.p1;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ImageSplitter {

    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;

    private Bitmap image; // fitted and scaled
    private ArrayList<Bitmap> slicedImages = new ArrayList<Bitmap>();


    public ImageSplitter(int width, int height, int tileWidth, int tileHeight, Bitmap image) {
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.image = fitImage(image);
        this.slicedImages = sliceImage();
    }


    private Bitmap fitImage(Bitmap image) {
        // TODO slice off excess (ratio)
        // TODO scale to right dimensions
    }

    private ArrayList<Bitmap> sliceImage() {
        this.image;
    }
}
