package f15.ssui.p1;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

// Takes an image and some dimensions
// Crops and scales an image to fit the given viewport and tile dimensions
// Slices them and puts them into an array
public class ImageSplitter {

    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private int numColumns;
    private int numRows;

    public Bitmap editedImage; // fitted and scaled
    private ArrayList<Bitmap> slicedImages = new ArrayList<>();


    public ImageSplitter(int width, int height, int numColumns, int numRows, Bitmap image) {
        this.width = width;
        this.height = height;

        this.numColumns = numColumns;
        this.numRows = numRows;

        this.tileWidth = width / numColumns;
        this.tileHeight = height / numRows;

        this.editedImage = fitImage(image);
        this.slicedImages = sliceImage(editedImage);
    }

    // TODO center the image for trimming??? maybe not necessary?
    private Bitmap fitImage(Bitmap image) {
        Bitmap result = image;

        // These need to be cast to double so division works
        double desiredRatio = this.width / (double) this.height;
        double imageRatio = image.getWidth() / (double) image.getHeight();

        //////////
        //// Crop the image if necessary
        //////////
        if(imageRatio > desiredRatio) {
            // if the image is wider than desired
            // w2 = w1/h1 * h2

            //System.out.println(this.width);
            //System.out.println(this.height);
            //System.out.println(desiredRatio);
            //System.out.println(imageRatio);
            //System.out.println((desiredRatio * image.getHeight()));
            //System.out.println((int)(desiredRatio * image.getHeight()));
            //System.out.println(image.getHeight());

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

    // Slice image into ordered pieces
    private ArrayList<Bitmap> sliceImage(Bitmap image) {
        ArrayList<Bitmap> result = new ArrayList<>();

        Log.d("splitting", "---------------------");
        Log.d("splitting", "bitmap width: " + image.getWidth() + ", bitmap height: " + image.getHeight());

        // loop through rows
        for(int y = 0; y < numRows; y++) {
            // loop through columns
            for(int x = 0; x < numColumns; x++) {
                Log.d("splitting", "----");
                Log.d("splitting", "x loc: " + (x * tileWidth) + ", y loc: " + (y * tileHeight));
                Log.d("splitting", "tileWidth: " + tileWidth + ", height: " + tileHeight);
                Log.d("splitting", "x dimens:" + (x * tileWidth) + " x " + (x * tileWidth + tileWidth));
                Log.d("splitting", "y dimens:" + (y * tileHeight) + " x " + (y * tileHeight + tileHeight));

                result.add(Bitmap.createBitmap(image, x * tileWidth, y * tileHeight, tileWidth, tileHeight));
            }
        }

        return result;
    }

    public Bitmap getImageAtIndex(int index) {
        return this.slicedImages.get(index);
    }

}
