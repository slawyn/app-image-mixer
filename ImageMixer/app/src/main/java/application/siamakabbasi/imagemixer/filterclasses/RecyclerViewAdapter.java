package application.siamakabbasi.imagemixer.filterclasses;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import application.siamakabbasi.imagemixer.R;

import application.siamakabbasi.imagemixer.commonclasses.BitmapOperations;
import application.siamakabbasi.imagemixer.commonclasses.DataManager;


public class RecyclerViewAdapter extends RecyclerView
        .Adapter<RecyclerViewAdapter
        .DataObjectHolder> {


    private String LOG_TAG = "RecyclerViewAdapter";
    private Activity mFilterActivity;
    private View mFragmentView;
    private ArrayList<RecyclerViewAdapter.DataObject> mDataset;
    private String[] mFilterNames = {"No Filter", "EarlyBird", "Gaussian", "Filter1", "Filter1"}; //Filter names

    public RecyclerViewAdapter(Activity activity, View view) {

        mFilterActivity = activity;
        mFragmentView = view;
        mDataset = getDataSet();
    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter, parent, false);


        DataObjectHolder dataObjectHolder = new DataObjectHolder(view, this);

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).mFilterName);
        holder.image.setImageBitmap(mDataset.get(position).mBitmap);

    }

    //Create filtered small  for RecycleView and big images for swapping
    private ArrayList<DataObject> getDataSet() {
        DataManager dm = DataManager.getInstance();
        BitmapOperations bm = BitmapOperations.getInstance();
        dm.initBitmaps(mFilterNames.length);


        //Get resized main and small images
        Bitmap small = BitmapOperations.getInstance().resizeToScreen(dm.getOriginalImageBmap(), 5, false, mFilterActivity);
        dm.setSmallBitmap(small, 0);
        Bitmap main = BitmapOperations.getInstance().resizeToScreen(dm.getOriginalImageBmap(), 1, false, mFilterActivity);
        dm.setMainBitmap(main, 0);

        //Show main image on the canvas
        ((ImageView) mFilterActivity.findViewById(R.id.big_image)).setImageBitmap(main);


        //Filter images asynchronously small and main images
        new AsyncBitmapFilter(small, main, FilterType.EARLYBIRD, 1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object) null);
        new AsyncBitmapFilter(small, main, FilterType.GAUSSIAN, 2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object) null);
        new AsyncBitmapFilter(small, main, FilterType.FILTER1, 3).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object) null);
        new AsyncBitmapFilter(small, main, FilterType.FILTER2, 4).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object) null);
        //TODO Add more Filters here

        //Create list of filters
        ArrayList<DataObject> results = new ArrayList<DataObject>();

        for (int i = 0; i < mFilterNames.length; i++) {
            Bitmap bitmap = null;

            bitmap = dm.getSmallBitmap(i);

            DataObject obj = new DataObject(mFilterNames[i], bitmap);
            results.add(i, obj);
        }
        return results;
    }


    //Filter selection handler
    public void swapImage(int position) {

        Log.i(LOG_TAG, "AdapterPosition:" + position);
        //TODO ADD Filter Image OnClick actions
        ImageView iv = (ImageView) mFilterActivity.findViewById(R.id.big_image);
        switch (position) {

            case 0:

                iv.setImageBitmap(DataManager.getInstance().getMainBitmap(0));
                DataManager.getInstance().setSelected(FilterType.NOFILTER);
                break;
            case 1:

                iv.setImageBitmap(DataManager.getInstance().getMainBitmap(1));
                DataManager.getInstance().setSelected(FilterType.EARLYBIRD);
                break;
            case 2:

                iv.setImageBitmap(DataManager.getInstance().getMainBitmap(2));
                DataManager.getInstance().setSelected(FilterType.GAUSSIAN);
                break;
            case 3:
                iv.setImageBitmap(DataManager.getInstance().getMainBitmap(3));
                DataManager.getInstance().setSelected(FilterType.FILTER1);
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            default:
                Log.i(LOG_TAG, "Unknown Position");
        }

    }

    //Used locally for saving data
    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView label;
        ImageView image;
        private String LOG_TAG = "DataObjectHolder";
        private RecyclerViewAdapter mRecycler;

        public DataObjectHolder(View itemView, RecyclerViewAdapter recycler) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.filtertext);
            image = (ImageView) itemView.findViewById(R.id.filterimage);
            mRecycler = recycler;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mRecycler.swapImage(getAdapterPosition());


        }
    }

    //Used locally for saving data
    public class DataObject {

        String mFilterName;
        Bitmap mBitmap;

        public DataObject(String text, Bitmap bitmap) {
            mFilterName = text;
            mBitmap = bitmap;
        }

    }


}


