package application.siamakabbasi.imagemixer;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import application.siamakabbasi.imagemixer.commonclasses.BitmapOperations;
import application.siamakabbasi.imagemixer.commonclasses.DataManager;


public class ManipulationActivity extends AppCompatActivity implements View.OnClickListener {
	private ImageView imgtop;
	private ImageView imgbottom;
	private SeekBar skbsettransparenty;
	private TextView text;
	private View content;
	private int alpha;
	private String LOG_TAG = "ManipulationActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manipulation);


		//Set up memeber variables
		initAlphaBar();
		Button btn = (Button) findViewById(R.id.btnOk);
		btn.setOnClickListener(this);

		content = findViewById(R.id.relcanvas);
		text = (TextView) findViewById(R.id.progresstext);

		DataManager dm = DataManager.getInstance();
		imgtop = (ImageView) findViewById(R.id.imgtop);
		imgbottom = (ImageView) findViewById(R.id.imgbottom);

		imgtop.setImageBitmap(dm.getFirstImageBmap());
		imgbottom.setImageBitmap(dm.getSecondImageBmap());


	}

	//Controls for the alpha bar
	public void initAlphaBar() {

		try {

			skbsettransparenty = (SeekBar) findViewById(R.id.skbSetTransparenty);
            alpha=255;

			skbsettransparenty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar arg0) {
				}

				@Override
				public void onStartTrackingTouch(SeekBar arg0) {
				}

				@Override
				public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
					if (progress >= 0 && progress <= skbsettransparenty.getMax()) {
						alpha =  progress ;
						text.setText(""+alpha);

						imgtop.setAlpha(((float)alpha)/255);
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onClick(View view) {
		DataManager dm = DataManager.getInstance();

		Bitmap bottomBmap = ((BitmapDrawable) imgbottom.getDrawable()).getBitmap();
		Bitmap topBmap = ((BitmapDrawable) imgtop.getDrawable()).getBitmap();


		//positions of bitmaps inside imageviews
		int[] locations0 = getBitmapPositionInsideImageView(imgbottom);// Need check if these functions work for every position
		int bottomPosX = locations0[0];
		int bottomPosY = locations0[1];

		int[] locations1 = getBitmapPositionInsideImageView(imgtop); // Need check if these functions work for every position
		int topPosX = locations1[0];
		int topPosY = locations1[1];


		//positions of imageviews
		int[] locations2 = new int[2];
		imgbottom.getLocationOnScreen(locations2);
		int x2 = locations2[0];
		int y2 = locations2[1];

		int[] locations3 = new int[2];
		imgtop.getLocationOnScreen(locations3);
		int x3 = locations3[0];
		int y3 = locations3[1];

		//Debug information
		Log.i(LOG_TAG, "inside imgview#\n");
		Log.i(LOG_TAG, "bottom:\ty.pos=" + bottomPosY + " x.pos=" + bottomPosX);
		Log.i(LOG_TAG, "top:\t\ty.pos=" + topPosY + " x.pos=" + topPosX);
		Log.i(LOG_TAG, "onscreen#\n");
		Log.i(LOG_TAG, "bottom:\ty.pos=" + y2 + " x.pos=" + x2);
		Log.i(LOG_TAG, "top:\t\ty.pos=" + y3 + " x.pos=" + x3);
		Log.i(LOG_TAG, "bottom\ty.size=" + bottomBmap.getHeight() + " x.size=" + bottomBmap.getWidth()+" dpi="+bottomBmap.getDensity());
		Log.i(LOG_TAG, "top:\t\ty.size=" + topBmap.getHeight() + " x.size=" + topBmap.getWidth()+" dpi="+topBmap.getDensity());
		Log.i(LOG_TAG, "canvas:\ty.size="+content.getHeight()+" x.size="+content.getWidth());


		//Create a new Bitmap and set it to OriginalImage for the next Activity
		BitmapOperations bm = BitmapOperations.getInstance();
		Bitmap result = bm.LayerFilter(bottomBmap, bottomPosX, bottomPosY,topBmap, topPosX, topPosY, content.getHeight(),content.getWidth(), alpha);
		dm.setOriginalImageBmap(result);

		//Start next activity

		Intent intent = new Intent(this, FilterActivity.class);
		startActivity(intent);/**/


	}

	public static int[] getBitmapPositionInsideImageView(ImageView imageView) {
		int[] ret = new int[4];

		if (imageView == null || imageView.getDrawable() == null)
			return ret;

		// Get image dimensions
		// Get image matrix values and place them in an array
		float[] f = new float[9];
		imageView.getImageMatrix().getValues(f);

		// Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
		final float scaleX = f[Matrix.MSCALE_X];
		final float scaleY = f[Matrix.MSCALE_Y];

		// Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
		final Drawable d = imageView.getDrawable();
		final int origW = d.getIntrinsicWidth();
		final int origH = d.getIntrinsicHeight();

		// Calculate the actual dimensions
		final int actW = Math.round(origW * scaleX);
		final int actH = Math.round(origH * scaleY);

		ret[2] = actW;
		ret[3] = actH;

		// Get image position
		// We assume that the image is centered into ImageView
		int imgViewW = imageView.getWidth();
		int imgViewH = imageView.getHeight();

		int top = (int) (imgViewH - actH) / 2;
		int left = (int) (imgViewW - actW) / 2;

		ret[0] = left;
		ret[1] = top;

		return ret;
	}
}
