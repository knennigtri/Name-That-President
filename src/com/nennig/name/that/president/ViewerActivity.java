package com.nennig.name.that.president;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Random;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewerActivity extends Activity {

	private static final String TAG = "ViewerActivity";
	private int assetIndex = 0;
	private String[] assetPaths;
	private int memorizedCount = 0;
	private int wrongCount = 0;
    private Bitmap bitmapImage;
    private String _current_mem;
    private AssetManagement aManagement;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_viewer);

//        if(savedInstanceState !=null)
//        {
//        	_current_mem = savedInstanceState.getString(MainActivity.NAME_THAT_FOLDER);
//            fManagement = new FileManagement(this, _current_mem);
//            photoIndex = savedInstanceState.getInt("photoIndex");
//            photoPaths = savedInstanceState.getStringArray("photoPaths");
//        }
//        else
//        {
//        	_current_mem =getIntent().getStringExtra(MainActivity.NAME_THAT_FOLDER);
//	        fManagement = new FileManagement(this, getIntent().getExtras().getString(MainActivity.NAME_THAT_FOLDER));
//	        
//        	photoPaths = fManagement.getShuffledMemPhotos();
//        }
        
        aManagement = new AssetManagement(ViewerActivity.this);
        
        try {
			assetPaths = aManagement.getShuffledAssetPhotos();
			for(String name:assetPaths){
			     System.out.println(name);    
			}
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		}

        nextPhoto();
        
        final Button nextButton = (Button) findViewById(R.id.viewer_next_button);
        nextButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TextView memorized = (TextView) findViewById(R.id.viewer_memorized);
				memorized.setText("" + memorizedCount);
				TextView wrong = (TextView) findViewById(R.id.viewer_wrong);
				wrong.setText("" + wrongCount);
				
				Log.d(TAG,"Next Memorized. PhotoIndex=" + assetIndex);
				nextPhoto();
			}
        });
    }
    
	private void nextPhoto(){
		if(assetIndex < assetPaths.length)
		{
			TextView counter = (TextView) findViewById(R.id.viewer_picture_counter);
			counter.setText((assetIndex + 1) + "/" + assetPaths.length);
			
			LinearLayout ll = (LinearLayout) findViewById(R.id.viewer_controlsFrame);
			ll.setVisibility(View.INVISIBLE);
			ImageView photoView = (ImageView) findViewById(R.id.viewer_imageView);
			
			InputStream iStream = null;
			try {
				Log.d(TAG, "Index: " + assetIndex);
				Log.d(TAG, "Next Asset: " + assetPaths[assetIndex]);
				iStream = ViewerActivity.this.getAssets().open(assetPaths[assetIndex]);
				
				bitmapImage = aManagement.drawNextPhoto(iStream, 500, 500);
				
				photoView.setImageBitmap(bitmapImage);
				photoView.setOnTouchListener(new OnTouchListener() {
		 			@Override
		 			public boolean onTouch(View arg0, MotionEvent arg1) {
		 				photoNameInputAlert();
		 				return false;
		 			}
		         	
		         });
			} catch (IOException e) {
				Log.d(TAG, "Exception Was Thrown.");
				Log.d(TAG, e.toString());
			}
		}
		else
		{
			Toast.makeText(ViewerActivity.this, "Now more Photos", Toast.LENGTH_LONG).show();
		}
	}

	 public void photoNameInputAlert(){
	    	AlertDialog.Builder alert = new AlertDialog.Builder(this); 

	        alert.setTitle(getString(R.string.app_name)); 
	        final TextView answerText = new TextView(this);
	        answerText.setHint("Answer Here");
	        
	        alert.setView(answerText);
	        alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() { 
	            public void onClick(DialogInterface dialog, int whichButton) { 
	            	Button nextButton = (Button) findViewById(R.id.viewer_next_button);
	            	if(isCorrectAnswer(answerText.getText().toString())){
	            		nextButton.setBackgroundResource(R.drawable.green_button);
	            		nextButton.setText("Correct!");
	            		memorizedCount++;
	            	}
	            	else
	            	{
	            		nextButton.setBackgroundResource(R.drawable.red_button);
	            		nextButton.setText("Not Quite!");
	            		wrongCount++;
	            	}
	            	TextView tv = (TextView) findViewById(R.id.viewer_photoName);
	 				tv.setText(AssetManagement.getPhotoName(assetPaths[assetIndex]));
	 				LinearLayout ll = (LinearLayout) findViewById(R.id.viewer_controlsFrame);
	 				ll.setVisibility(View.VISIBLE);
	 				assetIndex++;
	            } 
	        }); 
	        
	        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
	            public void onClick(DialogInterface dialog, int whichButton) { 
	              // Canceled. 
	            } 
	      }); 
	        
	      alert.show();
    }
	 
	 public boolean isCorrectAnswer(String answer){
		 String actual = assetPaths[assetIndex];
		 
		 
		 return true;
	 }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_viewer, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent;
    	switch(item.getItemId()){
    	case R.id.menu_start_over:
        	intent = new Intent(ViewerActivity.this,ViewerActivity.class);
        	intent.putExtra(MainActivity.NAME_THAT_FOLDER, _current_mem);   
        	startActivity(intent);
        	Toast.makeText(this, "Mem Restarted. Good Luck!", Toast.LENGTH_LONG).show();
    		finish();
    		return true;
    	case R.id.menu_main_menu:
        	intent = new Intent(ViewerActivity.this,MainActivity.class);
        	intent.putExtra(MainActivity.NAME_THAT_FOLDER, _current_mem);       
        	startActivity(intent);
    		finish();
    		return true;
    	case R.id.menu_about:
    		//TODO Do About inflater
    		return true;
    	case R.id.menu_rate_this:
    		//TODO DO Rate This inflater
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
    public void onSaveInstanceState(Bundle b){
    	//ToDO Viewer onSavedInstanceState
    	super.onSaveInstanceState(b);
    }
}
