package application.siamakabbasi.imagemixer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import application.siamakabbasi.imagemixer.commonclasses.BitmapOperations;
import application.siamakabbasi.imagemixer.commonclasses.DataManager;

public class ImageSelectionActivity extends AppCompatActivity implements View.OnClickListener{

	private String LOG_TAG = "SelectionActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imageselection);

		DataManager dm = DataManager.getInstance();
		BitmapOperations bm = BitmapOperations.getInstance();
		dm.init(this);
		bm.init(this);


		//TODO Custom Gallery Activity for selecting 2 images and saving them in Datamanager, also assigning them to img1 and img2
		//   https://github.com/luminousman/MultipleImagePick
		//Also need to resize them there to a known factor before assinging them
		Bitmap bmap1 = dm.loadImage1("", this);//Load these bitmaps with custom gallery
		Bitmap bmap2 = dm.loadImage2("", this);//Load these bitmaps with custom gallery


		//Scale the images at least to the screen width
		Bitmap imageOne = bm.resizeToScreen(bmap1,1,false,this);
		Bitmap imageTwo  = bm.resizeToScreen(bmap2,1,false,this);

		//Set First and Second for the next activity
		dm.setFirstImageBmap(imageOne);
		dm.setSecondImageBmap(imageTwo);


		//Set up images for this activity
		ImageView img1 = (ImageView) findViewById(R.id.img1);
		img1.setImageBitmap(bmap1);
		ImageView img2 = (ImageView) findViewById(R.id.img2);
		img2.setImageBitmap(imageTwo);

		Button btn = (Button) findViewById(R.id.doneselecting);
		btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		DataManager dm = DataManager.getInstance();
		//Both images need to be selected to proceed
		if(dm.getFirstImageBmap()!=null && dm.getSecondImageBmap()!=null) {
			Intent intent = new Intent(this, ManipulationActivity.class);
			startActivity(intent);
		}
		else
		{
			CharSequence text = "Please select both images";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(this, text, duration);
			toast.show();
		}
	}
}
