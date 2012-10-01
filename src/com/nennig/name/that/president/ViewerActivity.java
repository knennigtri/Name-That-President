package com.nennig.name.that.president;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewerActivity extends Activity {

	private static final String TAG = "ViewerActivity";
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 12;
	private static final String VIEWER_FIRST_USE = "name.that.viewer.first.use";
	private int assetIndex = 0; //Index of the current photo
	private String[] assetPaths; //array of all the paths to the photos
	private ArrayList<String> wrongAnswers; //List of all the wrong answers
	private int correctCount = 0; //Count of number correct
	private int wrongCount = 0; //Count of number incorrect
    private Bitmap bitmapImage;
    private boolean isCorrect = false; //Value that holds if the correct answer has been given
    private boolean giveUp = false;
    private boolean firstCorrect = true; //Value to show toast for first correct answer
    private boolean firstWrong = true;	//Value to show toast for first incorrect answer
    EditText answerText;
    private boolean _firstUse;
    
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_viewer);
        
        SharedPreferences settings = getSharedPreferences(MainActivity.NAME_THAT_PREFS,MODE_PRIVATE);
        _firstUse = settings.getBoolean(VIEWER_FIRST_USE, true);
        wrongAnswers = new ArrayList<String>(); //Initialize the wrongAnswer array
        
        //Get All photos and put them into AssetPaths
        try {
			assetPaths = AssetManagement.getShuffledAssetPhotos(this);
			for(String name:assetPaths){
			     System.out.println(name);    
			}
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		}

        nextPhoto(); //Get the first photo

        if(_firstUse){
        	touchInfoAlert();
        }

        final Button nextButton = (Button) findViewById(R.id.viewer_next_button);
        nextButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//updates the counters
				if(isCorrect)
				{
					correctCount++;
				}
				else
				{
					Log.d(TAG, "AssetIndex: " + assetIndex);
					wrongAnswers.add(assetPaths[assetIndex]);
					wrongCount++;
				}
 				assetIndex++;
 				//Gets the next photo
				nextPhoto();
				//Sets the counters on the top of the screen
				TextView memorized = (TextView) findViewById(R.id.viewer_memorized);
				memorized.setText("" + correctCount);
				TextView wrong = (TextView) findViewById(R.id.viewer_wrong);
				wrong.setText("" + wrongCount);
				Log.d(TAG,"Next Memorized. PhotoIndex=" + assetIndex);
				
				if(_firstUse){
					statInfoAlert();
				}
			}
        });
        
        final Button showMeButton = (Button) findViewById(R.id.viewer_answer_button);
        showMeButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				giveUp = true;
				TextView tv = (TextView) findViewById(R.id.viewer_photoName);
				tv.setText(AssetManagement.getPhotoName(assetPaths[assetIndex]));
			}
        });
    }
    
	private void nextPhoto(){
		isCorrect = false;
		giveUp = false;
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
				
				bitmapImage = AssetManagement.drawNextPhoto(iStream, 500, 500);
				
				photoView.setImageBitmap(bitmapImage);
				photoView.setOnTouchListener(new OnTouchListener() {
		 			@Override
		 			public boolean onTouch(View arg0, MotionEvent arg1) {
		 				if(!isCorrect && !giveUp)
		 				{
		 					photoNameInputAlert();
		 				}
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
			Intent intent = new Intent(ViewerActivity.this, ScoreActivity.class);
			intent.putExtra(MainActivity.NAME_THAT_CORRECT, correctCount);
			intent.putExtra(MainActivity.NAME_THAT_WRONG, wrongCount);
			intent.putExtra(MainActivity.NAME_THAT_WRONG_PHOTOS, wrongAnswers);
			startActivity(intent);
			finish();
		}
	}

	 public void photoNameInputAlert(){
	    	AlertDialog.Builder alert = new AlertDialog.Builder(this); 
	        alert.setTitle(getString(R.string.app_name)); 
	        
	        answerText = new EditText(this);
	        answerText.setHint("Answer Here");
	        answerText.setTextSize(20);
	   	    answerText.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
	   	    answerText.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
	        
	        alert.setView(answerText);
	        
	        alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() { 
	            public void onClick(DialogInterface dialog, int whichButton) { 
	            	checkAnswer(answerText.getText().toString());
	            } 
	        }); 
	        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
	            public void onClick(DialogInterface dialog, int whichButton) { 
	              // Canceled. 
	            } 
	      }); 
	        alert.setNeutralButton("Say It!", new DialogInterface.OnClickListener() { 
	            public void onClick(DialogInterface dialog, int whichButton) { 
	              startVoiceRecognitionActivity(); 
	            } 
	      });
	      alert.show();
    }
	 
     /**
      * Fire an intent to start the speech recognition activity.
      */
     private void startVoiceRecognitionActivity() {
         Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
         intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                 RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
         intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
         startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
     }
	 
	 public void checkAnswer(String str){
		 Button nextButton = (Button) findViewById(R.id.viewer_next_button);
		 Button showMeButton = (Button) findViewById(R.id.viewer_answer_button);
     	if(isCorrectAnswer(str)){
     		showMeButton.setBackgroundResource(R.drawable.green_button);
     		nextButton.setBackgroundResource(R.drawable.green_button);
     		nextButton.setText("Continue");
     		TextView tv = (TextView) findViewById(R.id.viewer_photoName);
				tv.setText(AssetManagement.getPhotoName(assetPaths[assetIndex]));
     		isCorrect = true;
     		if(firstCorrect){
     			Toast.makeText(ViewerActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
     			firstCorrect = false;
     		}
     	}
     	else
     	{
     		showMeButton.setBackgroundResource(R.drawable.red_button);
     		nextButton.setBackgroundResource(R.drawable.red_button);
     		nextButton.setText("Skip");
     		TextView tv = (TextView) findViewById(R.id.viewer_photoName);
				tv.setText("");
				isCorrect = false;
				if(firstWrong){
					Toast.makeText(ViewerActivity.this, "Touch photo for another try!", Toast.LENGTH_SHORT).show();
					firstWrong = false;
				}
			}
			LinearLayout ll = (LinearLayout) findViewById(R.id.viewer_controlsFrame);
			ll.setVisibility(View.VISIBLE);
	 }

	 /**
	  * This is the main decision module for this application. This breaks apart the answer string and then compares
	  * it to the inputed answer
	  * @param answer
	  * @return
	  */
	 //TODO Cover Spoken last names
	 public boolean isCorrectAnswer(String answer){
		//Converts the actual to lowercase
		 String actual = assetPaths[assetIndex].toLowerCase();
		//Gets rid of the extension
		 actual = actual.substring(0, actual.length()-4);
		 String[] split = actual.split(" ");
		 
		 //Converts the answer to lowercase
		 answer = answer.toLowerCase();
		 
		 for(int i = 0; i<split.length;i++){
			 Log.d(TAG,"Index = " + i + " >> '" + split[i] + "'");
		 }
		 Log.d(TAG, "Actual: '" + actual + "'");
		 Log.d(TAG,"Answer: '" + answer + "'");
		 
		 if(answer.equals(actual) ||
				 answer.contains(actual) ||
				 answer.endsWith(split[split.length-1]) ||
				 answer.startsWith(split[0])){
			 return true;
		 }
		 return false;
	 }
    
	 private void touchInfoAlert(){
		 AlertDialog.Builder alert = new AlertDialog.Builder(this); 
	        alert.setTitle("First Time?"); 
	        alert.setMessage("When you are ready to guess, just touch the photo!");      
	        alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() { 
	            public void onClick(DialogInterface dialog, int whichButton) { 
	            	
	            } 
	        }); 
	        alert.show();
	 }
	 
	 private void statInfoAlert(){
		 AlertDialog.Builder alert = new AlertDialog.Builder(this); 
	        alert.setTitle("First Time?"); 
	        alert.setMessage("The numbers under the photo are for the current photo, right, and wrong answers");
	        
	        _firstUse = false;
	        
	        SharedPreferences settings = getSharedPreferences(MainActivity.NAME_THAT_PREFS,MODE_PRIVATE);
	    	SharedPreferences.Editor e = settings.edit();
	    	e.putBoolean(VIEWER_FIRST_USE, _firstUse);
	    	e.commit();
	        
	        alert.setPositiveButton("Sweet!", new DialogInterface.OnClickListener() { 
	            public void onClick(DialogInterface dialog, int whichButton) { 
	            	
	            } 
	        }); 
	        alert.show();
	 }
	 
     /**
      * Handle the results from the recognition activity.
      */
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
             // Fill the list view with the strings the recognizer thought it could have heard
             ArrayList<String> matches = data.getStringArrayListExtra(
                     RecognizerIntent.EXTRA_RESULTS);
             String sAnswer = "";
             for(int i = 0; i<matches.size();i++)
            	 sAnswer = sAnswer + matches.get(i) + " ";
           //  Log.d(TAG, "Spoken Answer: "+sAnswer);
             checkAnswer(sAnswer);
         }

         super.onActivityResult(requestCode, resultCode, data);

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
        	intent = new Intent(ViewerActivity.this,ViewerActivity.class);  
        	startActivity(intent);
        	Toast.makeText(this, "Good Luck!", Toast.LENGTH_LONG).show();
    		finish();
    		return true;
    	case R.id.menu_main_menu:
        	intent = new Intent(ViewerActivity.this,MainActivity.class);     
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
            	ViewerActivity.this.startActivity(i);
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
