package application.siamakabbasi.imagemixer.commonclasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import application.siamakabbasi.imagemixer.R;
import application.siamakabbasi.imagemixer.filterclasses.FilterType;

import static android.R.attr.bitmap;
import static android.graphics.Bitmap.Config.ARGB_8888;

/**
 * Created by Benutzer-Account on 25.11.2016.
 */

public class DataManager {
    private Bitmap originalImageBmap;
    private Bitmap smallImageBmaps[];
    private Bitmap mainImageBmaps[];
    private Bitmap firstImageBmap;
    private Bitmap secondImageBmap;
    private Bitmap mainImageCustom;
    private BitmapFactory.Options options;
    private static DataManager instance;
    private FilterType selected; //used by FilterListFragment for saving Filter Selection
    private String LOG_TAG= "Bitmapoperations";

    //There is only one instance of DataManager
    private DataManager() {
    }

    static {
        instance = new DataManager();

    }

	public void setSelected(FilterType type){
		selected = type;
	}
	public FilterType getSelected(){
		return selected;
	}
    public static DataManager getInstance() {
        return instance;
  }


    //Used by FilterListFragment
    public void initBitmaps(int number) {
        smallImageBmaps = new Bitmap[number];
        mainImageBmaps = new Bitmap[number];
	    selected= FilterType.NOFILTER;
     }

    //Used by FilterCustomOptionsFragment
    public void initMainCustom(){
        mainImageCustom = mainImageBmaps[0].copy(ARGB_8888,true);
    }

    public Bitmap getMainImageCustom(){
        return mainImageCustom;
    }

	public void setMainImageCustom(Bitmap bitmap){
        Log.i(LOG_TAG,"setMainImageCustommutable="+bitmap.isMutable());
		mainImageCustom=bitmap;
	}

    public void setSmallBitmap(Bitmap bitmap, int index) {
        Log.i(LOG_TAG,"setSmllBitmapmutable="+bitmap.isMutable()+" "+index);
        smallImageBmaps[index] = bitmap;

    }
    public void setFirstImageBmap(Bitmap bitmap){
        Log.i(LOG_TAG,"setFirstBitmapmutable="+bitmap.isMutable());
        firstImageBmap=bitmap;
    }
    public void setSecondImageBmap(Bitmap bitmap){
        Log.i(LOG_TAG,"setSecondBitmapmutable="+bitmap.isMutable());
        secondImageBmap=bitmap;
    }
    public Bitmap getFirstImageBmap(){
        return firstImageBmap;
    }
    public Bitmap getSecondImageBmap(){
        return secondImageBmap;
    }

    //Loads First Image only for testing
    public Bitmap loadImage1(String PATH,Context context){
        //TODO method for testing. Custom gallery should load the images and then you assign them to firstImageBmap and secondImageBmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.test_image, options);
        bitmap = bitmap.copy(ARGB_8888 ,true);
        return bitmap;
    }

    //Loads Second Image only for testing
    public Bitmap loadImage2(String PATH,Context context){
        //TODO method for testing. Custom gallery should load the images and then you assign them to firstImageBmap and secondImageBmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.test_image2, options);
        bitmap = bitmap.copy(ARGB_8888 ,true);
        return bitmap;
    }

    //Used by FilterListFragment to set filtered main images
    public  void setMainBitmap(Bitmap bitmap, int index) {
        Log.i(LOG_TAG,"setMainBitmapmutable="+bitmap.isMutable()+" "+index);
        mainImageBmaps[index] = bitmap;

    }

    //Used by FilterListFragment
    public  Bitmap getSmallBitmap(int index)  {
        while(true) {
            if (smallImageBmaps[index] == null)
	            try{
                Thread.sleep(1);
	            }catch(Exception e){

	            }
             else
                break;
        }
        return smallImageBmaps[index];
    }
    //Used by FilterListFragment
    public  Bitmap getMainBitmap(int index)  {
        while(true) {
            if (mainImageBmaps[index] == null)
	            try{
		            Thread.sleep(1);
	            }catch(Exception e){

	            }

            else
                break;
        }
        return mainImageBmaps[index];
    }

    //Init options for loading images
    public void init(Context context) {
        options = new BitmapFactory.Options();
		options.inScaled = false;
        options.inMutable= true;


        options.inPreferredConfig= ARGB_8888;

    }

    //Loads OriginalImage: only for testing
    public void loadOriginalImageBmap(Context context) {
        originalImageBmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.test_image2, options);
        originalImageBmap= originalImageBmap.copy(ARGB_8888,true);

    }

    //Used by FilterListFragment
    public Bitmap getOriginalImageBmap() {
        return originalImageBmap;
    }

    //Used by ManipulationActivity
    public void setOriginalImageBmap(Bitmap bitmp) {
        originalImageBmap = bitmp;
    }



}
