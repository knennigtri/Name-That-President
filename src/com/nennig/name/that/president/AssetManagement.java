/**
 * This class manages all of the asset files that the Name That Series works with. It can get a file by name, create
 * a list of asset files according to the accepted extensions. This will also mix up the list of assets as well as
 * create a bitmap for the required requested size for the app.
 */


package com.nennig.name.that.president;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

@SuppressLint("NewApi")
public class AssetManagement {
	private static final String TAG = "AssetManagement";
	private final static String[] acceptedExtensions = {".jpg",".png"};
	public static final String ROOT_FOLDER = Environment.getExternalStorageDirectory().toString();
	
    public static String getPhotoName(String name){
    	
    	for(String ext : acceptedExtensions){
    		if(name.contains(ext))
    		{
    			return name.substring(0, name.length() - ext.length());
    		}
    	}   	
    	return name;
    }
	
	 public static int getNumberOfAssets(Context c) throws IOException{
    		return getAssetPhotos(c).length;
	 }
	
	 private static String[] getAssetPhotos(Context c) throws IOException{
		 return getAssetPhotos(c,new ArrayList<String>());
	 }
	 
    private static String[] getAssetPhotos(Context c, ArrayList<String> arrList) throws IOException{
    	String[] assets;
    	if(!arrList.isEmpty())
    	{
    		assets = arrList.toArray(new String[arrList.size()]);
    	}
    	else
    	{
    		//If this method is used and the arraylist is still empty
    		String[] arr = c.getAssets().list("");
        	ArrayList<String> tempAL = new ArrayList<String>();
        	for(String name : arr)
        	{
        		for(int j = 0; j<acceptedExtensions.length;j++)
        		{
        			if(name.contains(acceptedExtensions[j]))
        			{
        				tempAL.add(name);
        				j = acceptedExtensions.length;
        			}	
        		}
        	}
        	assets = new String[tempAL.size()];
	    	for(int i = 0 ;i< assets.length;i++){
	    		assets[i] = tempAL.get(i);
	    		Log.d(TAG, "ALL AssetPath: " + assets[i]);
	    	}
    	}

    	Log.d(TAG, "Asset ArrayList finished.");
    	return assets;
    }
    
    public static String[] getShuffledAssetPhotos(Context c) throws IOException{
    	return getShuffledAssetPhotos(c, new ArrayList<String>());
    }
    
    public static String[] getShuffledAssetPhotos(Context c, ArrayList<String> pathList) throws IOException{
    	String[] oArr = getAssetPhotos(c,pathList);
    	String[] photos = Arrays.copyOf(oArr,oArr.length, String[].class);
    	shuffleArray((String[]) photos);
    	Log.d(TAG, "Finished Shuffling");
    	return photos;
    }
    
    /**
     * This is a simple method to randomize the array
     * @param arr - Array to be randomized
     */
    private static void shuffleArray(String[] arr) {
        int n = arr.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
          int change = i + random.nextInt(n - i);
          swap(arr, i, change);
        }
      }

    /**
     * Simple swapping method
     * @param arr - Array being randomized
     * @param i - position of one element
     * @param change - position of second element
     */
      private static void swap(String[] a, int i, int change) {
        String helper = a[i];
        a[i] = a[change];
        a[change] = helper;
      }
    
    public static Bitmap drawNextPhoto(InputStream iStream, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(iStream, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(iStream, null, options);
    }
    
    public static int calculateInSampleSize(BitmapFactory.Options o, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = o.outHeight;
	    final int width = o.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	        if (width > height) {
	            inSampleSize = Math.round((float)height / (float)reqHeight);
	        } else {
	            inSampleSize = Math.round((float)width / (float)reqWidth);
	        }
	    }
	    return inSampleSize;
    }

	public static ArrayList<String> getPhotosLeft(int assetIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
