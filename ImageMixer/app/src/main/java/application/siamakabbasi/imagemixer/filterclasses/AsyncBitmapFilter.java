package application.siamakabbasi.imagemixer.filterclasses;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import application.siamakabbasi.imagemixer.commonclasses.BitmapOperations;
import application.siamakabbasi.imagemixer.commonclasses.DataManager;

/**
 * Created by Benutzer-Account on 27.11.2016.
 */

//Used for filtering small and main images asynchronously
public class AsyncBitmapFilter extends AsyncTask {

	private Bitmap mSmallBitmap;
	private Bitmap mMainBitmap;
	private FilterType mType;
	private int mIndex;

	public AsyncBitmapFilter(Bitmap small, Bitmap main, FilterType ftype,int index) {
		super();
		mSmallBitmap = small;
		mMainBitmap = main;
		mIndex = index;
		mType = ftype;
	}

	@Override
	protected String doInBackground(Object[] objects) {
		DataManager dm = DataManager.getInstance();
		BitmapOperations bm = BitmapOperations.getInstance();
		dm.setSmallBitmap(bm.filter(mSmallBitmap, mType), mIndex);
		dm.setMainBitmap(bm.filter(mMainBitmap, mType), mIndex);
		return null;

	}


}
