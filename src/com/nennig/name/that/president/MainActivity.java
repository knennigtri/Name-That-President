package com.nennig.name.that.president;

import com.nennig.constants.*;
import com.nennig.name.that.*;

import java.io.IOException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
	private static final String TAG = "MainActivity";
	
	private static final String MAIN_FIRST_USE = "name.that.main.first.use";
	private static final String DEFAULT_MAIN_IMAGE = "Abraham Lincoln.jpg";
	
	private int _most_correct;
	private int _total_assets;
	private int _numTries;
	private boolean _firstUse;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Rate this app
        AppManager.app_launched(this);
        
        SharedPreferences settings = getSharedPreferences(AppPrefsConstants.NAME_THAT_PREFS,MODE_PRIVATE);
        _firstUse = settings.getBoolean(MAIN_FIRST_USE, true);

        if(savedInstanceState != null)
        {
	        _most_correct = savedInstanceState.getInt(AppPrefsConstants.NAME_THAT_MOST_CORRECT);
	        _numTries = savedInstanceState.getInt(AppPrefsConstants.NAME_THAT_NUM_TRIES);
        }
        else
        {
        	loadPreferences();
        }
        
        try
        {
        	_total_assets = AssetManagement.getNumberOfAssets(this);
        }
        catch (IOException e) {
			Log.d(TAG, e.toString());
			_total_assets = 0;
		}
        
        setMainScreenText();
        drawImage();
        
        final Button startButton = (Button) findViewById(R.id.main_start_button);
        if(_firstUse)
        {
        	startButton.setText("Start");
        }
        startButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!_firstUse)
				{
					continueInflator();
				}
				else
				{
					 _firstUse = false;
			        SharedPreferences settings = getSharedPreferences(AppPrefsConstants.NAME_THAT_PREFS,MODE_PRIVATE);
			    	SharedPreferences.Editor e = settings.edit();
			    	e.putBoolean(MAIN_FIRST_USE, _firstUse);
			    	e.commit();
					startActivity(new Intent(MainActivity.this, ViewerActivity.class));
				}
			}	
        });  
        final Button reviewButton = (Button) findViewById(R.id.main_review_button);
        reviewButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				typeOfReviewAlert();
			}	
        }); 
        
        final Button moreGamesButton = (Button) findViewById(R.id.main_more_games_button);
        moreGamesButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String str ="https://play.google.com/store/search?q=Name+That&c=apps&price=1";
	    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
				Log.d(TAG,"More Games Button pressed.");
			}	
        });
    }

    public void continueInflator(){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this); 

        alert.setTitle("Continue?"); 
        alert.setMessage("Would you like to pick up where you left off last time?");
        
        alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	Intent i = new Intent(MainActivity.this, ViewerActivity.class);
            	i.putExtra(AppPrefsConstants.NAME_THAT_CONTINUE, true);
            	startActivity(i);
            } 
        }); 
        
        alert.setNegativeButton("Start Over", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	startActivity(new Intent(MainActivity.this, ViewerActivity.class));
            } 
      }); 
      alert.show();
    }
    
    private void drawImage(){
    	ImageView photoView = (ImageView) findViewById(R.id.main_imageView);
		Bitmap bitmapImage;
    	
//		TextView title = (TextView) findViewById(R.layout.activity_main);
//		int top = title.getBottom();
//		TextView stats = (TextView) findViewById(R.id.main_best_attempt);
//		int bottom = Math.round(stats.getTop());
//		int size = Math.abs(top-bottom);
//		Log.d(TAG, "Size of Frame: " + size + " " + top + " " + bottom);
		
		InputStream iStream = null;
		try {
			iStream = MainActivity.this.getAssets().open(DEFAULT_MAIN_IMAGE);
			
			bitmapImage = AssetManagement.drawNextPhoto(iStream, 140,140);
			photoView.setImageBitmap(bitmapImage);
		} catch (IOException e) {
			Log.d(TAG, "Exception Was Thrown.");
			Log.d(TAG, e.toString());
		}
    }
    
    private void typeOfReviewAlert(){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this); 

        alert.setTitle("Review Mode"); 
        alert.setMessage("Review your wrong answers only?");
        
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	Intent i = new Intent(MainActivity.this, PracticeActivity.class);
            	i.putExtra(AppPrefsConstants.LOAD_WRONG_ANSWERS, true);
            	startActivity(i);
            } 
        }); 
        
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	Intent i = new Intent(MainActivity.this, PracticeActivity.class);
            	i.putExtra(AppPrefsConstants.LOAD_WRONG_ANSWERS, false);
            	startActivity(i);
            } 
      }); 
      alert.show();
    }
    
    private void setMainScreenText(){
    	TextView bestAttemptsText = (TextView) findViewById(R.id.main_best_attempt);
    	TextView numAttemptsText = (TextView) findViewById(R.id.main_attempts);
    	
    	bestAttemptsText.setText("Best Attempt: " + _most_correct + "/" + _total_assets);
    	numAttemptsText.setText("Attempts: " + _numTries);
    }
    
   private void loadPreferences(){
	   SharedPreferences settings = getSharedPreferences(AppPrefsConstants.NAME_THAT_PREFS,MODE_PRIVATE);
       _most_correct = settings.getInt(AppPrefsConstants.NAME_THAT_MOST_CORRECT, 0);
       _numTries = settings.getInt(AppPrefsConstants.NAME_THAT_NUM_TRIES, 0);
   }
    
    protected void onResume()
    {
       super.onResume();
       loadPreferences();
       setMainScreenText();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_about:
    		AppManager.aboutAlert(this);
    		return true;
    	case R.id.menu_rate_this:
    		String str = DevConstants.GOOGLE_PLAY + getString(R.string.app_package);
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    		return true;
    	case R.id.menu_reset:
    		AlertDialog.Builder alert = new AlertDialog.Builder(this); 
	        alert.setTitle("Reset All Scores?");
	        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
	            public void onClick(DialogInterface dialog, int whichButton) { 
		    		SharedPreferences settings = getSharedPreferences(AppPrefsConstants.NAME_THAT_PREFS,MODE_PRIVATE);
		        	SharedPreferences.Editor e = settings.edit();
		        	e.putInt(AppPrefsConstants.NAME_THAT_MOST_CORRECT, 0);
		        	e.putInt(AppPrefsConstants.NAME_THAT_NUM_TRIES, 0);
		        	e.putString(AppPrefsConstants.NAME_THAT_WRONG_PHOTOS, "");
		        	e.commit();
		        	recreate();
	            }
	        });
	        alert.setNegativeButton("No", new DialogInterface.OnClickListener() { 
	            public void onClick(DialogInterface dialog, int whichButton) { 
	            	//Cancel
	            }
	        });
	        alert.show();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
