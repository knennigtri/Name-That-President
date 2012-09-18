package com.nennig.name.that.president;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class AssetManagement {
	private static final String TAG = "FileManagement";
	private static String[] acceptedExtensions = {".jpg",".png"};
	public static final String ROOT_FOLDER = Environment.getExternalStorageDirectory().toString();
	//public static final String ROOT_FOLDER1 = 
	
	private Activity _activity;
	
    public static String getPhotoName(String name){
    	
    	for(int i = 0; i<acceptedExtensions.length;i++){
    		if(name.contains(acceptedExtensions[i]))
    		{
    			return name.substring(0, name.length()-4);
    		}
    	}   	
    	return name;
    }
	
	public AssetManagement(Activity a){
		_activity = a;
	}
	
    private Object[] getAssetPhotos() throws IOException{
    	String[] arr = _activity.getAssets().list("");
    	ArrayList<String> al = new ArrayList<String>();
    	for(int i = 0; i<arr.length; i++)
    	{
    		for(int j = 0; j<acceptedExtensions.length;j++)
    		{
    			if(arr[i].contains(acceptedExtensions[j]))
    			{
    				al.add(arr[i]);
    				j = acceptedExtensions.length;
    			}	
    		}
    	}
    	String[] assets = new String[al.size()];
    	
    	for(int i = 0 ;i< assets.length;i++){
    		assets[i] = al.get(i);
    	}
    	
    	Log.d(TAG, "ArrayList finished.");
    	return assets;
    }
    
    public String[] getShuffledAssetPhotos() throws IOException{
    	String[] photos = (String[]) getAssetPhotos();
    	shuffleArray(photos);
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
    
    public Bitmap drawNextPhoto(InputStream iStream, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
    //    BitmapFactory.decodeStream(iStream, null, options);

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
}
