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

        Bitmap sizedImage = fitImage(image);
        this.slicedImages = sliceImage(sizedImage);
    }


    private Bitmap fitImage(Bitmap image) {
        // TODO slice off excess (ratio)
        // TODO scale to right dimensions

        Bitmap result = image;

        double desiredRatio = this.width / this.height;
        double imageRatio = image.getWidth() / image.getHeight();

        //////////
        //// Crop the image if necessary
        //////////
        if(imageRatio > desiredRatio) {
            // if the image is wider than desired
            // w2 = w1/h1 * h2
            result = Bitmap.createBitmap(image, 0, 0, (int)(desiredRatio * image.getHeight()), image.getHeight());
        }
        else if(imageRatio < desiredRatio) {
            // if image is taller than desired
            // h2 = h1/w1 * w2 (used 1/x inverse of desired ratio)
            result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), (int)(1/desiredRatio * image.getWidth()));
        }

        // Now scale image to the right size
        result = Bitmap.createScaledBitmap(result, this.width, this.height, false);


        return result;
    }

    private ArrayList<Bitmap> sliceImage(Bitmap image) {
        // TODO make array of the sliced images in order

        ArrayList<Bitmap> result = new ArrayList<Bitmap>();

        // loop through height
        for(int y = 0; y < image.getHeight(); y += this.tileHeight) {
            // loop through width
            for(int x = 0; x < image.getWidth(); x += this.tileWidth) {
                result.add(Bitmap.createBitmap(image, x, y, this.tileWidth, this.tileHeight));
            }
        }

        return result;
    }

    public Bitmap getImageAtIndex(int index) {
        return this.slicedImages.get(index);
    }
}
