package com.nennig.name.that.president;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends Activity {
	private static final String TAG = "ScoreActivity";
	private int _numCorrect;
	private int _numWrong;
	private ArrayList<String> _wrongAnswers;
	
	private int _bAttempt;
	private int _numAttempts;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        
        //Get values from current Attempt
        _numCorrect = getIntent().getIntExtra(MainActivity.NAME_THAT_CORRECT, 0);
        _numWrong = getIntent().getIntExtra(MainActivity.NAME_THAT_WRONG, 0);
        _wrongAnswers = getIntent().getExtras().getStringArrayList(MainActivity.NAME_THAT_WRONG_PHOTOS);
        
        //Get Saved Values
        SharedPreferences settings = getSharedPreferences(MainActivity.NAME_THAT_PREFS,MODE_PRIVATE);
        _bAttempt = settings.getInt(MainActivity.NAME_THAT_MOST_CORRECT, 0);
    	_numAttempts = settings.getInt(MainActivity.NAME_THAT_NUM_TRIES, 0);
        
        TextView results = (TextView) findViewById(R.id.score_text);
        results.setText(getResultsText());
        
        savePreferences();
        
        final Button backButton = (Button) findViewById(R.id.score_back_button);
        backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(ScoreActivity.this, MainActivity.class));
				finish();
			}
        });
        final Button reviewWrongButton = (Button) findViewById(R.id.score_review_button);
        reviewWrongButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(_wrongAnswers.size() > 0)
				{
					Intent intent = new Intent(ScoreActivity.this, PracticeActivity.class);
					for(int i = 0;i<_wrongAnswers.size(); i++)
						Log.d(TAG, "Wrong: " + _wrongAnswers.get(i));
					intent.putExtra(MainActivity.NAME_THAT_WRONG_PHOTOS, _wrongAnswers);
					startActivity(intent);
				}
				else
					Toast.makeText(ScoreActivity.this, "You had all answers correct.", Toast.LENGTH_SHORT).show();
			}
        });
        
    }

    private String getResultsText(){
    	int total = _numCorrect + _numWrong;
    	float percentRight = _numCorrect / total;
    	String result = "";
    	
    	if(percentRight < .25)
    		result = result + "You need more practice\n";
    	else if(percentRight < .50)
    		result = result + "Almost halfway there!\n";
    	else if(percentRight < .75)
    		result = result + "Not a bad score\n";
    	else if(percentRight < .90)
    		result = result + "You're good at this\n";
    	else if(percentRight < .95)
    		result = result + "Almost Mastered!\n";
    	else if(_numCorrect == total)
    		result = result + "Perfect Score!\n";
    	
    	result = result + "\n\n";
    	if(_numCorrect != total)
    		result = result + "Right: " + _numCorrect + "\n" + "Wrong: " + _numWrong + "\n";
    	result = result + "Score: " + _numCorrect + "/" + total + "\n";
    	return result;
    }
    
    public void savePreferences(){
    	SharedPreferences settings = getSharedPreferences(MainActivity.NAME_THAT_PREFS,MODE_PRIVATE);
    	SharedPreferences.Editor e = settings.edit();
    	
    	if(_numCorrect > _bAttempt)
    		e.putInt(MainActivity.NAME_THAT_MOST_CORRECT, _numCorrect);
    	
    	e.putInt(MainActivity.NAME_THAT_NUM_TRIES, _numAttempts+1);    	
    	e.commit();
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
        	intent = new Intent(ScoreActivity.this,ViewerActivity.class);   
        	startActivity(intent);
        	Toast.makeText(this, "Good Luck!", Toast.LENGTH_LONG).show();
    		finish();
    		return true;
    	case R.id.menu_main_menu:
        	intent = new Intent(ScoreActivity.this,MainActivity.class);       
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
            	ScoreActivity.this.startActivity(i);
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
