package com.nennig.name.that.president;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PracticeActivity extends Activity {
    	private static final String TAG = "PracticeActivity";
    	private int assetIndex = 0; //Index of the current photo
    	private String[] assetPaths; //array of all the paths to the photos
        private AssetManagement aManagement;
        private Bitmap bitmapImage;
        
    	@Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_practice);

     //       ArrayList<String> wrongPhotoPaths = getIntent().getExtras().getStringArrayList(MainActivity.NAME_THAT_WRONG_PHOTOS);
            
            //If there is a wrong photo list, then load that into the AssetManager
            //TODO GetWrongPaths working in Practice Mode
//            if(wrongPhotoPaths != null)
//            {
//            	for(int i = 0;i<wrongPhotoPaths.size(); i++)
//    				Log.d(TAG, "Wrong: " + wrongPhotoPaths.get(i));
//            	aManagement = new AssetManagement(PracticeActivity.this,wrongPhotoPaths);
//            }
//            else
            	aManagement = new AssetManagement(PracticeActivity.this);
            
            //Get All photos and put them into AssetPaths
            try {
    			assetPaths = aManagement.getShuffledAssetPhotos();
    			for(String name:assetPaths){
    			     System.out.println(name);    
    			}
    		} catch (IOException e) {
    			Log.d(TAG, e.toString());
    		}
            
            nextPhoto();
                        
            final Button nextButton = (Button) findViewById(R.id.practice_nextButton);
            nextButton.setOnClickListener(new Button.OnClickListener() {
    			@Override
    			public void onClick(View arg0) {
    				assetIndex++;
    				nextPhoto();
    			}
            	
            });
        }
        
    	private void nextPhoto(){
    		if(assetIndex < assetPaths.length)
    		{
				Log.d(TAG, "index after next iteration: "+assetIndex);
				LinearLayout ll = (LinearLayout) findViewById(R.id.practice_controlsFrame);
 				ll.setVisibility(0);
 				
 				TextView tv = (TextView) findViewById(R.id.practice_photoName);
 				tv.setText(AssetManagement.getPhotoName(assetPaths[assetIndex]));
 					
 				ImageView photoView = (ImageView) findViewById(R.id.practice_imageView);
 				InputStream iStream = null;
 				try {
	 				iStream = PracticeActivity.this.getAssets().open(assetPaths[assetIndex]);
	 				bitmapImage = aManagement.drawNextPhoto(iStream, 500, 500);	
	 				photoView.setImageBitmap(bitmapImage);
 				} catch (IOException e) {
 					Log.d(TAG, "Exception Was Thrown.");
 					Log.d(TAG, e.toString());
 				}
 				
 				Log.d(TAG,"PhotoIndex=" + assetIndex);
			}
			else
			{
				finish();
			}
    	}
    	
        
        
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.general, menu);
            return true;
        }
        
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
        	Intent intent;
        	switch(item.getItemId()){
        	case R.id.menu_start_over:
            	intent = new Intent(PracticeActivity.this,ViewerActivity.class);  
            	startActivity(intent);
        		finish();
        		return true;
        	case R.id.menu_main_menu:
            	intent = new Intent(PracticeActivity.this,MainActivity.class);     
            	startActivity(intent);
        		finish();
        		return true;
        	case R.id.menu_about:
        		aboutAlert(this);
        		return true;
        	case R.id.menu_rate_this:
        		String str ="https://play.google.com/store/apps/details?id=" + getString(R.string.app_package);
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        	}
        }
        public void aboutAlert(Context c){
        	AlertDialog.Builder alert = new AlertDialog.Builder(c); 

            alert.setTitle("About"); 
            alert.setMessage("Copywrite @ 2012 Kevin Nennig");
            
            alert.setPositiveButton("View Site", new DialogInterface.OnClickListener() { 
                public void onClick(DialogInterface dialog, int whichButton) { 
                	String url = "https://sites.google.com/site/nennigk/personal-projects/photomem-app";
                	Intent i = new Intent(Intent.ACTION_VIEW);
                	i.setData(Uri.parse(url));
                	PracticeActivity.this.startActivity(i);
                } 
            }); 
            
            alert.setNegativeButton("Close", new DialogInterface.OnClickListener() { 
                public void onClick(DialogInterface dialog, int whichButton) { 
                  // Canceled. 
                } 
          }); 
          alert.show();
        }
    }
