package com.nennig.name.that.president;

import java.util.ArrayList;

import com.nennig.constants.*;
import com.nennig.name.that.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends BaseActivity {
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
        _numCorrect = getIntent().getIntExtra(AppPrefsConstants.NAME_THAT_CORRECT, 0);
        _numWrong = getIntent().getIntExtra(AppPrefsConstants.NAME_THAT_WRONG, 0);
        _wrongAnswers = getIntent().getExtras().getStringArrayList(AppPrefsConstants.NAME_THAT_WRONG_PHOTOS);
        
        //Get Saved Values
        SharedPreferences settings = getSharedPreferences(AppPrefsConstants.NAME_THAT_PREFS,MODE_PRIVATE);
        _bAttempt = settings.getInt(AppPrefsConstants.NAME_THAT_MOST_CORRECT, 0);
    	_numAttempts = settings.getInt(AppPrefsConstants.NAME_THAT_NUM_TRIES, 0);
        
        TextView results = (TextView) findViewById(R.id.score_text);
        results.setText(getResultsText());
        
        //Save stats and reset game state
    	SharedPreferences.Editor e = settings.edit();
        savePreferences(e);
        resetGameState(e);
        
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
					intent.putExtra(AppPrefsConstants.NAME_THAT_WRONG_PHOTOS, _wrongAnswers);
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
    
    /**
     * Saves the app statistics
     * @param e 
     */
    public void savePreferences(SharedPreferences.Editor e){
    	if(_numCorrect > _bAttempt)
    		e.putInt(AppPrefsConstants.NAME_THAT_MOST_CORRECT, _numCorrect);
	    	e.putInt(AppPrefsConstants.NAME_THAT_NUM_TRIES, _numAttempts+1);  
	    	e.putString(AppPrefsConstants.NAME_THAT_WRONG_PHOTOS, createPrefSaveString(_wrongAnswers));
	    	e.commit();
    }
    /**
     * resets the current game state
     * @param e
     */
    private void resetGameState(SharedPreferences.Editor e){
		 e.putInt(AppPrefsConstants.NAME_THAT_CORRECT, 0);
		 e.putInt(AppPrefsConstants.NAME_THAT_WRONG, 0);
		 e.putString(AppPrefsConstants.NAME_THAT_SAVED_GAME, ""); 
		 e.putInt(AppPrefsConstants.NAME_THAT_CUR_INDEX, 0);
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
}
