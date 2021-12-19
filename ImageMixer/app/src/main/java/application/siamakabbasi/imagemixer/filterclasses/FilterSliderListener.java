package application.siamakabbasi.imagemixer.filterclasses;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.concurrent.ExecutionException;

import application.siamakabbasi.imagemixer.R;
import application.siamakabbasi.imagemixer.commonclasses.BitmapOperations;
import application.siamakabbasi.imagemixer.commonclasses.DataManager;

import static android.support.v7.widget.AppCompatDrawableManager.get;

/**
 * Created by alex on 04.12.16.
 */

public class FilterSliderListener implements SeekBar.OnSeekBarChangeListener {

    private int redProgress;
    private int greenProgress;
    private int blueProgress;
    private SeekBar mRedSeekBar;
    private SeekBar mGreenSeekBar;
    private SeekBar mBlueSeekBar;
    private BitmapOperations bo;
    private DataManager dm;
    private ImageView mainImage;
    private String LOG_TAG = "FilterSliderListener";
    private int w;
    private int h;

    FilterSliderListener(SeekBar rSeekBar, SeekBar gSeekBar, SeekBar bSeekBar, ImageView img) {
        redProgress = 255;
        greenProgress = 255;
        blueProgress = 255;
        mRedSeekBar = rSeekBar;
        mGreenSeekBar = gSeekBar;
        mBlueSeekBar = bSeekBar;

        mRedSeekBar.setProgress(redProgress);
        mGreenSeekBar.setProgress(greenProgress);
        mBlueSeekBar.setProgress(blueProgress);

        mRedSeekBar.setOnSeekBarChangeListener(this);
        mGreenSeekBar.setOnSeekBarChangeListener(this);
        mBlueSeekBar.setOnSeekBarChangeListener(this);

        bo = BitmapOperations.getInstance();
        dm = DataManager.getInstance();
        w=dm.getMainBitmap(0).getWidth();
        h=dm.getMainBitmap(0).getHeight();
        mainImage = img;

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
        //Log.i(LOG_TAG,"progress:"+progress+ " "+dm.getMainBitmap(0).getConfig());

        if (seekBar == mRedSeekBar) {
            redProgress=progressValue;

        } else if (seekBar == mGreenSeekBar) {
            greenProgress=progressValue;
        } else if (seekBar == mBlueSeekBar) {
           blueProgress=progressValue;
        }



       bo.RGBAdjustFilter(dm.getMainBitmap(0), dm.getMainImageCustom(), redProgress, greenProgress, blueProgress);
       mainImage.setImageBitmap(dm.getMainImageCustom());
        //bitmap2.setPixels(src,0,w,0,0,w,h);
        //dm.setMainImageCustom(bitmap);
        //mainImage.setImageBitmap(bitmap);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
