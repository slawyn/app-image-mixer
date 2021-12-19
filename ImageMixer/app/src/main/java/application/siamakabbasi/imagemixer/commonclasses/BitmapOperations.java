package application.siamakabbasi.imagemixer.commonclasses;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.jabistudio.androidjhlabs.filter.GammaFilter;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.InvertFilter;
import com.jabistudio.androidjhlabs.filter.OilFilter;
import com.jabistudio.androidjhlabs.filter.PointFilter;
import com.jabistudio.androidjhlabs.filter.RGBAdjustFilter;
import com.jabistudio.androidjhlabs.filter.util.PixelUtils;

import java.util.Vector;

import application.siamakabbasi.imagemixer.commonclasses.myfilters.EarlyBird;
import application.siamakabbasi.imagemixer.filterclasses.FilterType;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Benutzer-Account on 25.11.2016.
 */

public class BitmapOperations {


    //http://www.jhlabs.com/ip/filters/index.html
    //http://stackoverflow.com/questions/16292146/android-how-to-apply-diffenernt-image-effects-on-bitmap-like-sepia-blackand-w
    //https://github.com/mordonez-me/instagram-filters-jhlabs-android/blob/master/ImageFilters/src/main/java/com/jabistudio/androidjhlabs/filter/GammaFilter.java
    private String LOG_TAG = "BitmapOperations";
    private static BitmapOperations instance;
    private Point mScreenSize;
    private float mDPI;

    private BitmapOperations() {
    }

    static {
        instance = new BitmapOperations();

    }

    public float getScreenDPI() {
        return mDPI;
    }

    public static BitmapOperations getInstance() {
        return instance;
    }

    public void init(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        mScreenSize = new Point();
        display.getSize(mScreenSize);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mDPI = (metrics.density);

    }

    public Bitmap filter(Bitmap bitmap, FilterType type) {
        Bitmap result = null;
        switch (type) {
            case NOFILTER:
                Log.i(LOG_TAG, "Running NOFILTER");
                result = bitmap;
                break;
            case EARLYBIRD:
                Log.i(LOG_TAG, "Running EARLYBIRD");
                result = EarlyBirdFilter(bitmap, null);
                break;
            case GAUSSIAN:
                Log.i(LOG_TAG, "Running GAUSSIAN");
                result = GaussianFilter(bitmap, 5.0f);
                break;
            case FILTER1:
                Log.i(LOG_TAG, "Running FILTER1");
                result = InvertFilter(bitmap);
                break;
            case FILTER2:
                Log.i(LOG_TAG, "Running FILTER2");
                result = bitmap;
                break;
            default:
                Log.i(LOG_TAG, "Running UNKNOWN FILTER" + type);
                System.exit(1);
                break;
        }
        Log.i(LOG_TAG, "Result:" + "width=" + result.getWidth() + "height=" + result.getHeight() + " dpi=" + result.getDensity());
        return result;
    }

    private Bitmap GaussianFilter(Bitmap bitmap, float radius) {

        GaussianFilter gf = new GaussianFilter(radius);
        Log.i(LOG_TAG, "GaussianFilter:" + gf);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] src = new int[w * h];
        bitmap.getPixels(src, 0, w, 0, 0, w, h);
        gf.filter(src, w, h);
        Bitmap result = Bitmap.createBitmap(src, w, h, Bitmap.Config.ARGB_8888);

        return result;

    }

    public Bitmap LayerFilter(Bitmap bitmap0, int pos0x, int pos0y, Bitmap bitmap1, int pos1x, int pos1y, int canvasHeight, int canvasWidth, int alpha) {

        int w0 = bitmap0.getWidth();
        int h0 = bitmap0.getHeight();
        int[] src0 = new int[w0 * h0];
        bitmap0.getPixels(src0, 0, w0, 0, 0, w0, h0);

        int w1 = bitmap1.getWidth();
        int h1 = bitmap1.getHeight();
        int[] src1 = new int[w1 * h1];
        bitmap1.getPixels(src1, 0, w1, 0, 0, w1, h1);


        int[] srcresult = new int[canvasHeight * canvasWidth];


        int bitmap0_boundsx0 = pos0x;
        int bitmap0_boundsx1 = pos0x + w0;

        int bitmap0_boundsy0 = pos0y;
        int bitmap0_boundsy1 = pos0y + h0;

        int bitmap1_boundsx0 = pos1x;
        int bitmap1_boundsx1 = pos1x + w1;

        int bitmap1_boundsy0 = pos1y;
        int bitmap1_boundsy1 = pos1y + h1;


        //TODO add more bounds

        int idx = 0;
        int idx0 = 0;
        int idx1 = 0;


        int mask = 0x00ffffff;
        int opaque = 0xff000000;
        int nalpha = 255 - alpha;
        for (int y = 0; y < canvasHeight; y++) {
            for (int x = 0; x < canvasWidth; x++) {

                idx = x + y * canvasWidth;
                if (x >= bitmap0_boundsx0 && x < bitmap0_boundsx1 && y >= bitmap0_boundsy0 && y < bitmap0_boundsy1 && idx0 < src0.length) {
                    srcresult[idx] = src0[idx0];
                    idx0++;
                } else {
                    srcresult[idx] = 0xff00ff00;

                }

                if (x >= bitmap1_boundsx0 && x < bitmap1_boundsx1 && y >= bitmap1_boundsy0 && y < bitmap1_boundsy1 && idx1 < src1.length) {
                    int color0 = srcresult[idx] & mask;
                    int color1 = src1[idx1];

                    int r0 = (color0 >> 16) & 0xff;
                    int g0 = (color0 >> 8) & 0xff;
                    int b0 = color0 & 0xff;

                    int r1 = (color1 >> 16) & 0xff;
                    int g1 = (color1 >> 8) & 0xff;
                    int b1 = color1 & 0xff;


                    int r = PixelUtils.clamp(((r1 * alpha / 255) + (r0 * nalpha / 255))) << 16;
                    int g = PixelUtils.clamp(((g1 * alpha / 255) + (g0 * nalpha / 255))) << 8;
                    int b = PixelUtils.clamp(((b1 * alpha / 255) + (b0 * nalpha / 255)));


                    srcresult[idx] = opaque | r | g | b;


                    idx1++;
                }

            }

        }

        Bitmap result = Bitmap.createBitmap(srcresult, canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);

        return result;
    }

    private Bitmap EarlyBirdFilter(Bitmap bitmap, Resources res) {
        Bitmap result = new EarlyBird().transform(bitmap, res);
        //result.setDensity(mDefaultDensity);
        return result;
    }

    //not working yet
    private Bitmap GammaFilter(Bitmap bitmap, float gamma) {
        GammaFilter gf = new GammaFilter(gamma);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] src = new int[w * h];
        bitmap.getPixels(src, 0, w, 0, 0, w, h);
        gf.filter(src, w, h);
        Bitmap result = Bitmap.createBitmap(src, w, h, Bitmap.Config.ARGB_8888);

        return result;
    }


    public void RGBAdjustFilter(Bitmap bitmap, Bitmap custom, int redFactor, int greenFactor, int blueFactor) {

        int w0 = bitmap.getWidth();
        int h0 = bitmap.getHeight();
        int [] src0= new int[w0 * h0];
        bitmap.getPixels(src0, 0, w0, 0, 0, w0, h0);



        int idx = 0;
        int opaque = 0xff000000;

        boolean checkR = false;
        boolean checkG = false;
        boolean checkB = false;


        for (int y = 0; y < h0; y++) {
            for (int x = 0; x < w0; x++) {


                idx = x + y * w0;

                int color0 = src0[idx];


                int r = (color0 >> 16) & 0xff;
                int g = (color0 >> 8) & 0xff;
                int b = color0 & 0xff;


                    r = (r * redFactor / 255);

                    g = (g * greenFactor / 255);

                    b = (b * blueFactor / 255);


                src0[idx] = opaque | (r << 16) | (g << 8) | b;
            }
        }
        custom.setPixels(src0,0,w0,0,0,w0,h0);

    }

    //not working yet
    private Bitmap InvertFilter(Bitmap bitmap) {
        InvertFilter inf = new InvertFilter();
        int w0 = bitmap.getWidth();
        int h0 = bitmap.getHeight();
        int[] src0 = new int[w0 * h0];

        bitmap.getPixels(src0, 0, w0, 0, 0, w0, h0);
        inf.filter(src0, w0, h0);

        return Bitmap.createBitmap(src0, w0, h0, Bitmap.Config.ARGB_8888);

    }

    //not working
    private Bitmap OilFilter(Bitmap bitmap, int range, int levels, Rect transformedSpace) {
        OilFilter of = new OilFilter();
        of.setLevels(levels);
        of.setRange(range);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] src = new int[w * h];
        bitmap.getPixels(src, 0, w, 0, 0, w, h);
        Bitmap result = Bitmap.createBitmap(src, w, h, Bitmap.Config.ARGB_8888);

        return result;
    }


    //Resize method
    public Bitmap resizeToScreen(Bitmap bmap, int factor, boolean vertical, Context context) {

        int maxSize;
        if (vertical)
            maxSize = mScreenSize.y / factor;
        else
            maxSize = mScreenSize.x / factor;

        int w = bmap.getWidth();
        int h = bmap.getHeight();
        int outWidth;
        int outHeight;

        if (w > h) {
            outWidth = maxSize;
            outHeight = (h * maxSize) / w;
        } else {
            outHeight = maxSize;
            outWidth = (w * maxSize) / h;
        }

        return Bitmap.createScaledBitmap(bmap, outWidth, outHeight, false);

    }





}
