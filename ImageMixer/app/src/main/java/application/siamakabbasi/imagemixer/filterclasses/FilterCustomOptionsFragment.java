package application.siamakabbasi.imagemixer.filterclasses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.icu.text.BreakIterator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import application.siamakabbasi.imagemixer.FilterActivity;
import application.siamakabbasi.imagemixer.R;
import application.siamakabbasi.imagemixer.commonclasses.BitmapOperations;
import application.siamakabbasi.imagemixer.commonclasses.DataManager;

/**
 * Created by Benutzer-Account on 23.11.2016.
 */


//used for RGB filtering:
//TODO Implement this
public class FilterCustomOptionsFragment extends Fragment implements View.OnClickListener{

	private Context parentContext;
	private String LOG_TAG="FilterCustomOptionsFrag";
	private View mView;


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		mView= inflater.inflate(R.layout.fragment_filter_customoptions,container, false);
		DataManager.getInstance().initMainCustom();

		//####
		SeekBar redBar= (SeekBar) mView.findViewById(R.id.seekBar0);
		SeekBar greenBar = (SeekBar) mView.findViewById(R.id.seekBar1);
		SeekBar blueBar = (SeekBar) mView.findViewById(R.id.seekBar2);
		new FilterSliderListener(redBar,greenBar,blueBar, (ImageView)getActivity().findViewById(R.id.big_image));
        //####
		Button btn = (Button)mView.findViewById(R.id.backbutton);

		btn.setOnClickListener(this);
		return mView;
	}

	@Override
	public void onClick(View view){
		FragmentManager fm= FilterActivity.getFragmentManager_();
		FragmentTransaction ft = fm.beginTransaction();
		ft.addToBackStack(null);
		ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
				.hide(FilterActivity.getFragments().get(1))
				.show(FilterActivity.getFragments().get(0))
				.commit();

	}

}
