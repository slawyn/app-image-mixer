package application.siamakabbasi.imagemixer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Vector;

import application.siamakabbasi.imagemixer.commonclasses.BitmapOperations;
import application.siamakabbasi.imagemixer.filterclasses.FilterCustomOptionsFragment;
import application.siamakabbasi.imagemixer.filterclasses.FilterListFragment;
import application.siamakabbasi.imagemixer.commonclasses.DataManager;


/**
 * Created by alex on 20.11.16.
 */

public class FilterActivity extends AppCompatActivity {


	private static Vector<Fragment> fragments;
	private static FragmentManager fm;
	private String LOG_TAG = "FilterActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);

		////for testing when you use only this class
		DataManager dInst = DataManager.getInstance();
		//BitmapOperations bInst = BitmapOperations.getInstance();
		//dInst.init(this);
		//bInst.init(this);
		dInst.loadOriginalImageBmap(this);


		//Add fragments to the list: 0 - ListView  1 - Custom Options
		fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.addToBackStack(null);

		fragments = new Vector<Fragment>();
		fragments.add(new FilterListFragment());
		fragments.add(new FilterCustomOptionsFragment());

		ft.add(R.id.fragment_filter, fragments.get(0));
		ft.add(R.id.fragment_filter, fragments.get(1));
		ft.show(fragments.get(0));
		ft.hide(fragments.get(1));
		ft.commit();

	}
	public static FragmentManager getFragmentManager_() {
		return fm;
	}

	public static Vector<Fragment> getFragments() {
		return fragments;
	}

}
