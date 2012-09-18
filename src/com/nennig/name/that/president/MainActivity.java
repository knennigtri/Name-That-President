package com.nennig.name.that.president;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	public static final String NAME_THAT_FOLDER = "";
	public static final String NAME_THAT_STATS = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Button startButton = (Button) findViewById(R.id.main_start_button);
        startButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, ViewerActivity.class));
				finish();
			}	
        });
        
        final Button moreGamesButton = (Button) findViewById(R.id.main_more_games_button);
        moreGamesButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d(TAG,"More Games Button pressed.");
			}	
        });
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
    		//TODO Do About inflater
    		return true;
    	case R.id.menu_rate_this:
    		//TODO DO Rate This inflater
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
