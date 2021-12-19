package application.siamakabbasi.imagemixer.filterclasses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import application.siamakabbasi.imagemixer.FilterActivity;
import application.siamakabbasi.imagemixer.R;
import application.siamakabbasi.imagemixer.commonclasses.BitmapOperations;
import application.siamakabbasi.imagemixer.commonclasses.DataManager;

/**
 * Created by Benutzer-Account on 23.11.2016.
 */

public class FilterListFragment extends Fragment implements View.OnClickListener {


	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private View mView;
	private Button mCustomButton;
	private Button mFinishButton;

	private static String LOG_TAG = "FilterFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_filter_list, container, false);
		DataManager inst = DataManager.getInstance();
		BitmapOperations bo = BitmapOperations.getInstance();

		//Setup for the scrollable RecyclerView
		Context context = getActivity();
		mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
		mRecyclerView.setHasFixedSize(true);

		//Make it horizontal
		mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setMinimumWidth(100);

		//Add space between the items
		mRecyclerView.addItemDecoration(new CustomItemDecoration((int)(bo.getScreenDPI()*20)));

		//Improve scrolling
		mRecyclerView.setNestedScrollingEnabled(false);

		//Create Adapter
		mAdapter = new RecyclerViewAdapter(getActivity(),mView);
		mRecyclerView.setAdapter(mAdapter);

		//Set up the buttons
		mCustomButton = (Button) mView.findViewById(R.id.custombutton);
		mCustomButton.setOnClickListener(this);

		mFinishButton = (Button) mView.findViewById(R.id.finishbutton);
		mFinishButton.setOnClickListener(new FinishOnClickListener());


		return mView;


	}



	@Override
	public void onClick(View view) {
		FragmentManager fm= FilterActivity.getFragmentManager_();
		FragmentTransaction ft = fm.beginTransaction();
		ft.addToBackStack(null);
		ft
				.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
				.hide(FilterActivity.getFragments().get(0))
				.show(FilterActivity.getFragments().get(1))
				.commit();
	}

	//Used for putting space between the filter images in the horizontal list
	public class CustomItemDecoration extends RecyclerView.ItemDecoration {

		private final int spaceBetweenItems;

		public CustomItemDecoration(int space) {
			this.spaceBetweenItems = space;
		}

		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
		                           RecyclerView.State state) {
			if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)
				outRect.right = spaceBetweenItems;
		}
	}



	//Listener for finishbutton
	public class FinishOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			DataManager dm = DataManager.getInstance();
			//TODO NEED TO MAKE SAVING FASTER OR LESS OBVIOUS
			Bitmap bitmap = BitmapOperations.getInstance().filter(dm.getOriginalImageBmap(),dm.getSelected());
			saveImage(bitmap);


		}
	}

	//Method to save Bitmap to ImageMixer folder and add it to gallery
	public int saveImage(Bitmap finalBitmap) {

		Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		if (isSDPresent) {


			String root = Environment.getExternalStorageDirectory().toString();
			String myImage = "Mixed" + currentDateTime() + ".png";

			//If ImageMixer doesn't exist create it
			File myDir = new File(root + "/ImageMixer");
			if (!myDir.exists()) {
				myDir.mkdirs();
			}

			//If there is an image with the same name delete it
			File file = new File(myDir, myImage);
			if (file.exists()) {
				file.delete();
			}
			try {


				//Toast text output
				CharSequence text = "Saving Image" + myImage;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

				//Write Bitmap to file
				FileOutputStream out = new FileOutputStream(file);
				finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();


				//Some devices require this for the Image to be seen in the gallery
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, myImage);
				values.put(MediaStore.Images.Media.DESCRIPTION, "ImageMixer Image");
				values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
				values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
				values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
				values.put("_data", file.getAbsolutePath());

				//Add values to create content in the gallery
				ContentResolver cr = getActivity().getContentResolver();
				cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				return 0;

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		//Something went wrong
		//TODO add saving to internal phone storage
		CharSequence text = "Couldn't save Image";
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		return -1;
	}

	//Get timestamp for image
	public String currentDateTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
		String myDate = format.format(new Date());
		return myDate;
	}


}
